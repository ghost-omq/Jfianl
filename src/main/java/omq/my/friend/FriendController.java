package omq.my.friend;

import com.jfinal.plugin.activerecord.Page;

import omq.common.controller.BaseController;
import omq.common.model.Account;

public class FriendController extends BaseController{
	
	static final FriendService srv = FriendService.me;

	public void follow() {
		Page<Account> followPage = srv.getFollowPage(getLoginAccountId(), getParaToInt("p", 1));
		setAttr("followPage", followPage);
		render("follow.html");
	}
	
	public void fans() {
		Page<Account> fansPage = srv.getFansPage(getLoginAccountId(), getParaToInt("p", 1));
		setAttr("fansPage", fansPage);
		render("fans.html");
	}
}
