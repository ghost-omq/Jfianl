package omq.login;

import java.util.Date;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;

import omq.common.model.Account;
import omq.common.model.Session;

public class LoginService {
	
	public static final String loginAccountCacheName = "loginAccount";
	public static final String sessionIdName = "jfinalId";
	
	static final LoginService me = new LoginService();
	private final Account dao = new Account().dao();
	
	public Ret login(String userName, String password, boolean keepLogin, String loginIp){
		Account loginAccount = dao.findFirst("select * from account where userName=?",userName);
		if(loginAccount == null){
			return Ret.fail("msg","用户名或密码不正确");
		}
		if (loginAccount.isStatusLockId()) {
			return Ret.fail("msg", "账号已被锁定");
		}
		if (loginAccount.isStatusReg()) {
			return Ret.fail("msg", "账号未激活，请先激活账号");
		}
		String salt = loginAccount.getSalt();
		String sha256 = HashKit.sha256(salt+password);
		if(loginAccount.getPassword().equals(sha256) == false){
			return Ret.fail("msg","用户名或密码不正确");
		}
		long liveSeconds =  keepLogin ? 3 * 365 * 24 * 60 * 60 : 120 * 60;
		int maxAgeInSeconds = (int)(keepLogin ? liveSeconds : -1);
		long expireAt = System.currentTimeMillis() + (liveSeconds * 1000);
		Session session = new Session();
		String sessionId = StrKit.getRandomUUID();
		session.setId(sessionId);
		session.setAccountId(loginAccount.getId());
		session.setExpireAt(expireAt);
		if ( ! session.save()) {
			return Ret.fail("msg", "保存 session 到数据库失败，请联系管理员");
		}

		loginAccount.removeSensitiveInfo();
		loginAccount.put("sessionId", sessionId);
		//CacheKit.put(loginAccountCacheName, sessionId, loginAccount);

		createLoginLog(loginAccount.getId(), loginIp);

		return Ret.ok(sessionIdName, sessionId)
						.set(loginAccountCacheName, loginAccount)
						.set("maxAgeInSeconds", maxAgeInSeconds);
	}
	private void createLoginLog(Integer accountId, String loginIp) {
		Record loginLog = new Record().set("accountId", accountId).set("ip", loginIp).set("loginAt", new Date());
		Db.save("login_log", loginLog);
	}

}
