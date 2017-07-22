package omq.user.share;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;

import omq.common.controller.BaseController;
import omq.common.model.Share;
import omq.my.friend.FriendInterceptor;
import omq.my.like.LikeInterceptor;
import omq.user.common.UserSpaceInterceptor;

@Before({UserSpaceInterceptor.class, FriendInterceptor.class, LikeInterceptor.class})
public class UserShareController extends BaseController {

	static final UserShareService srv = UserShareService.me;

	@ActionKey("/user/share")
	public void share() {
		List<Share> shareList = srv.findAll(getParaToInt());
		setAttr("shareList", shareList);
		render("index.html");
	}
}


