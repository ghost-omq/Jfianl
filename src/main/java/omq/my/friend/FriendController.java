package omq.my.friend;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.plugin.activerecord.Page;

import omq.common.controller.BaseController;
import omq.common.interceptor.FrontAuthInterceptor;
import omq.common.model.Account;
import omq.my.like.LikeInterceptor;

@Before({FrontAuthInterceptor.class, FriendInterceptor.class, LikeInterceptor.class})
public class FriendController extends BaseController{
	
	static final FriendService srv = FriendService.me;
																								
	@ActionKey("/my/follow")
	public void follow() {
		Page<Account> followPage = srv.getFollowPage(getLoginAccountId(), getParaToInt("p", 1));
		setAttr("followPage", followPage);
		render("follow.html");
	}
	
	@ActionKey("/my/fans")
	public void fans() {
		Page<Account> fansPage = srv.getFansPage(getLoginAccountId(), getParaToInt("p", 1));
		setAttr("fansPage", fansPage);
		render("fans.html");
	}
}
