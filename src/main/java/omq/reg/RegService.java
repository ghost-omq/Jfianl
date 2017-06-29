package omq.reg;

import java.util.Date;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

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
		
		if(account.save()){
			return Ret.ok("msg", "注册成功，激活邮件已发送，请查收并激活账号：" + userName);
		} else {
			return Ret.fail("msg", "注册失败，account 保存失败，请告知管理员");
		}
	}

}
