package omq.common.pageView;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import omq.common.kit.IpKit;

public class PageViewInterceptor implements Interceptor{

	public void intercept(Invocation inv) {
		inv.invoke();

		Controller c = inv.getController();

		if (c.isParaExists(0)) {
			String actionKey = inv.getActionKey();
			Integer id = c.getParaToInt();
			String ip = IpKit.getRealIp(c.getRequest());
			PageViewService.me.processPageView(actionKey, id, ip);
		}
	}
}
