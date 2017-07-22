package omq.user.feedback;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;

import omq.common.controller.BaseController;
import omq.common.model.Feedback;
import omq.my.friend.FriendInterceptor;
import omq.my.like.LikeInterceptor;
import omq.user.common.UserSpaceInterceptor;

@Before({UserSpaceInterceptor.class, FriendInterceptor.class, LikeInterceptor.class})
public class UserFeedbackController  extends BaseController {

	static final UserFeedbackService srv = UserFeedbackService.me;

	@ActionKey("/user/feedback")
	public void feedback() {
		List<Feedback> feedbackList = srv.findAll(getParaToInt());
		setAttr("feedbackList", feedbackList);
		render("index.html");
	}
}


