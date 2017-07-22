package omq.user.project;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;

import omq.common.controller.BaseController;
import omq.common.model.Project;
import omq.my.friend.FriendInterceptor;
import omq.my.like.LikeInterceptor;
import omq.user.common.UserSpaceInterceptor;

@Before({UserSpaceInterceptor.class, FriendInterceptor.class, LikeInterceptor.class})
public class UserProjectController  extends BaseController {

	static final UserProjectService srv = UserProjectService.me;

	@ActionKey("/user/project")
	public void project() {
		List<Project> projectList = srv.findAll(getParaToInt());
		setAttr("projectList", projectList);
		render("index.html");
	}
}