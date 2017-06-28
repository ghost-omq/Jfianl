package omq.main;

import com.jfinal.config.Routes;

import omq.helloTest.HelloJfinal;
import omq.my.favorite.FavoriteController;
import omq.my.feedback.MyFeedbackController;
import omq.my.project.MyProjectController;
import omq.my.share.MyShareController;

public class ConfigRoute extends Routes{

	public void config() {
		setBaseViewPath("/view");
		
		add("/index",HelloJfinal.class);
		add("/my/share",MyShareController.class);
		add("/my/feedback",MyFeedbackController.class);
		add("/my/project",MyProjectController.class);
		add("/my/favorite",FavoriteController.class);
	}

}
