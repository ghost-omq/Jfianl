package omq.main;

import com.jfinal.config.Routes;

import omq.index.IndexController;
import omq.login.LoginController;
import omq.my.favorite.FavoriteController;
import omq.my.feedback.MyFeedbackController;
import omq.my.project.MyProjectController;
import omq.my.share.MyShareController;
import omq.reg.RegController;

public class ConfigRoute extends Routes{

	public void config() {
		setBaseViewPath("/view");
		
		add("/",IndexController.class,"/index");
		add("/my/share",MyShareController.class);
		add("/my/feedback",MyFeedbackController.class);
		add("/my/project",MyProjectController.class);
		add("/my/favorite",FavoriteController.class);
		add("/reg",RegController.class);
		add("/login",LoginController.class);
	}

}
