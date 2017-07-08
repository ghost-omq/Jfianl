package omq.common.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import omq.common.kit.IpKit;
import omq.common.model.Account;
import omq.common.model.Remind;
import omq.login.LoginService;
import omq.my.newsfeed.RemindService;

public class LoginSessionInterceptor implements Interceptor{
	
	public static final String remindKey = "_remind";

	public void intercept(Invocation inv) {
        Account loginAccount = null;
		Controller c = inv.getController();
		String sessionId = c.getCookie(LoginService.sessionIdName);
		if (sessionId != null) {
			loginAccount = LoginService.me.getLoginAccountWithSessionId(sessionId);
			if (loginAccount == null) {
				String loginIp = IpKit.getRealIp(c.getRequest());
				loginAccount = LoginService.me.loginWithSessionId(sessionId, loginIp);
			}
			if (loginAccount != null) {
				c.setAttr(LoginService.loginAccountCacheName, loginAccount);
			} else {
				c.removeCookie(LoginService.sessionIdName);
			}
		}

		inv.invoke();

        if (loginAccount != null) {
            Remind remind = RemindService.me.getRemind(loginAccount.getId());
            if (remind != null) {
                if (remind.getReferMe() > 0 || remind.getMessage() > 0 || remind.getFans() > 0) {
                    c.setAttr(remindKey, remind);
                }
            }
        }
	}

}
