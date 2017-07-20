package omq.my.friend;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import omq.common.controller.BaseController;

public class FriendInterceptor implements Interceptor{
	
	public static final String followNum = "_followNum";
    public static final String fansNum = "_fansNum";
    public static final String friendRelation = "_friendRelation";

	public void intercept(Invocation inv) {
        inv.invoke();

        BaseController c = (BaseController) inv.getController();
        boolean isUserSpace = inv.getActionKey().startsWith("/user");
        if (isUserSpace) {
            handleUserSpaceFriend(c);
        } else {
            handleMySpaceFriend(c);
        }
    }
	
	private void handleUserSpaceFriend(BaseController c) {
        int userId = c.getParaToInt();
        if (c.isLogin()) {
            int myId = c.getLoginAccountId();
            int friendRelations = FriendService.me.getFriendRelation(myId, userId);
            c.setAttr(friendRelation, friendRelations);
        }
        else {
            int friendRelations = 0;
            c.setAttr(friendRelation, friendRelations);
        }

        int[] ret = FriendService.me.getFollowAndFansCount(userId);
        c.setAttr(followNum, ret[0]);
        c.setAttr(fansNum, ret[1]);
    }

    private void handleMySpaceFriend(BaseController c) {
        int myId = c.getLoginAccountId();

        int[] ret = FriendService.me.getFollowAndFansCount(myId);
        c.setAttr(followNum, ret[0]);
        c.setAttr(fansNum, ret[1]);
    }

}
