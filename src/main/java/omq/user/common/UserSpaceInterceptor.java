package omq.user.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import omq.common.account.AccountService;
import omq.common.controller.BaseController;
import omq.common.model.Account;

public class UserSpaceInterceptor implements Interceptor {

    public void intercept(Invocation inv) {
        BaseController c = (BaseController)inv.getController();

        if (c.isLogin() && c.getParaToInt() == c.getLoginAccountId()) {
            String newActionKey = inv.getActionKey().replaceFirst("/user", "/my");
            c.redirect(newActionKey, true);
            return ;
        }

        c.checkUrlPara(1);
        Account user = AccountService.me.getUsefulById(c.getParaToInt());
        if (user == null) {
            c.renderError(404);
            return ;
        }
        c.setAttr("user", user);

        inv.invoke();
    }
}
