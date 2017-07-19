package omq.my.friend;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import omq.common.controller.BaseController;

public class FriendInterceptor implements Interceptor{

	public void intercept(Invocation inv) {
        inv.invoke();

        BaseController c = (BaseController) inv.getController();
        boolean isUserSpace = inv.getActionKey().startsWith("/user");
    }

}
