package omq.reg;

import java.util.Date;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

import omq.common.authcode.AuthCodeService;
import omq.common.kit.EmailKit;
import omq.common.model.Account;

public class RegService {
	
	public static final RegService me = new RegService();
	public final Account dao = new Account().dao();
	
	public boolean isUserNameExists(String userName){
		return Db.queryInt("select id from account where userName=?",userName) !=null;
	}
	
	public boolean isNickNameExists(String nickName){
		return Db.queryInt("select id from account where lower(nickName)=?",nickName) !=null;
	}
	
	public Ret reg(String userName, String password, String nickName, String ip){
		if(StrKit.isBlank(userName) || StrKit.isBlank(password) || StrKit.isBlank(nickName)){
			return Ret.fail("msg","用户名密码或邮箱不能为空");
		}
		
		if(isNickNameExists(nickName)){
			return Ret.fail("msg","用户名已经被注册");
		}
		if(isUserNameExists(userName)){
			return Ret.fail("msg","邮箱已近被注册");
		}
		
		String salt = HashKit.generateSaltForSha256();
		password = HashKit.sha256(salt + password);
		
		Account account = new Account();
		account.setUserName(userName);
		account.setPassword(password);
		account.setSalt(salt);
		account.setNickName(nickName);
		account.setStatus(Account.STATUS_REG);
		account.setCreateAt(new Date());
		account.setIp(ip);
		account.setAvatar(Account.AVATAR_NO_AVATAR);
		
		if (account.save()) {
			String authCode =  AuthCodeService.me.createRegActivateAuthCode(account.getInt("id"));
			if (sendRegActivateAuthEmail(authCode, account)) {
				return Ret.ok("msg", "注册成功，激活邮件已发送，请查收并激活账号：" + userName);
			} else {
				return Ret.fail("msg", "注册成功，但是激活邮件发送失败");
			}
		} else {
			return Ret.fail("msg", "注册失败，account 保存失败");
		}
	}
	
	private boolean sendRegActivateAuthEmail(String authCode, Account reg) {
		String title = "Jfinal 会员激活邮件";
		String content = "在浏览器地址栏里输入并访问下面激活链接即可完成账户激活：\n\n"
				+ " http://www.jfinal.com/reg/activate?authCode="
				+ authCode;
		
		String emailService = PropKit.get("emailService");
		String fromEmail = PropKit.get("fromEmail");
		String emailPass = PropKit.get("emailPass");
		String toEmail = reg.getStr("userName");
		
		try{
			EmailKit.sendEmail(emailService, fromEmail, emailPass, toEmail,title,content);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public Ret reSendActivateEmail(String userName){
		if(StrKit.isBlank(userName)){
			Ret.fail("msg","不能为空");
		}
		if(! isUserNameExists(userName)){
			Ret.fail("msg","邮箱没有被注册，无法获取激活邮件");
		}
		
		Account account = dao.findFirst("select * from account where userName=? and status=?",userName,Account.STATUS_REG);
		if(account == null){
			Ret.fail("msg", "邮箱已被激活,可以直接登录");
		}
		String authCode = AuthCodeService.me.createRegActivateAuthCode(account.getId());
		if(sendRegActivateAuthEmail(authCode,account)){
			return Ret.ok("msg","激活邮箱已经发送");
		}else{
			return Ret.fail("msg","激活失败");
		}
	}

}
