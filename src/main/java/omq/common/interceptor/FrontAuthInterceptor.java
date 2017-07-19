
package omq.common.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;

import omq.login.LoginService;

public class FrontAuthInterceptor implements Interceptor {
	public void intercept(Invocation inv) {
		if (inv.getController().getAttr(LoginService.loginAccountCacheName) != null) {
			inv.invoke();
		} else {
			String queryString = inv.getController().getRequest().getQueryString();
			if (StrKit.isBlank(queryString)) {
				inv.getController().redirect("/login?returnUrl=" + inv.getActionKey());
			} else {
				inv.getController().redirect("/login?returnUrl=" + inv.getActionKey() + "?" + queryString);
			}
		}
	}
}

