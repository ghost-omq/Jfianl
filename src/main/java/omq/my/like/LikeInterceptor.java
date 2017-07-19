package omq.my.like;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import omq.common.account.AccountService;
import omq.common.controller.BaseController;
import omq.common.model.Account;

public class LikeInterceptor implements Interceptor{

	public static final String likeNum = "_likeNum";

    public void intercept(Invocation inv) {
        inv.invoke();

        BaseController c = (BaseController) inv.getController();
        boolean isUserSpace = inv.getActionKey().startsWith("/user");
        if (isUserSpace) {
            handleUserSpaceLikeCount(c);
        } else {
            handleMySpaceLikeCount(c);
        }
    }

    private void handleUserSpaceLikeCount(BaseController c) {
        Account account = AccountService.me.getById(c.getParaToInt());
        c.setAttr(likeNum, account.getLikeCount());
    }

    private void handleMySpaceLikeCount(BaseController c) {
        Account account = AccountService.me.getById(c.getLoginAccountId());
        c.setAttr(likeNum, account.getLikeCount());
    }
}
