package omq.common.authcode;

import com.jfinal.kit.StrKit;

import omq.common.model.AuthCode;

public class AuthCodeService {
	
	public static final AuthCodeService me = new AuthCodeService();
	public final AuthCode dao = new AuthCode().dao();
	
	public String createRegActivateAuthCode(int accountId) {
		return createAuthCode(accountId, AuthCode.TYPE_REG_ACTIVATE, 3600);
	}
	
	private String createAuthCode(int accountId, int authType, int expireTime){
		long et = expireTime;
		long expireAt = System.currentTimeMillis() + (et * 1000);
		
		AuthCode ac = new AuthCode();
		ac.setId(StrKit.getRandomUUID());
		ac.setAccountId(accountId);
		ac.setType(authType);
		ac.setExpireAt(expireAt);
		
		if(ac.save()){
			return ac.getId();
		}else {
			throw new RuntimeException("保存 auth_code 记录失败，请联系管理员");
		}
	}

}
