package omq.main;

import com.jfinal.config.Routes;

import omq.feedback.FeedbackController;
import omq.index.IndexController;
import omq.login.LoginController;
import omq.my.favorite.FavoriteController;
import omq.my.feedback.MyFeedbackController;
import omq.my.message.MessageController;
import omq.my.newsfeed.NewsFeedController;
import omq.my.project.MyProjectController;
import omq.my.setting.MySettingController;
import omq.my.share.MyShareController;
import omq.project.ProjectController;
import omq.reg.RegController;
import omq.share.ShareController;

public class ConfigRoute extends Routes{

	public void config() {
		setBaseViewPath("/view");
		
		add("/",IndexController.class,"/index");
		add("/project",ProjectController.class);
		add("/share",ShareController.class);
		add("/feedback",FeedbackController.class);
		
		add("/my",NewsFeedController.class,"/my/newsfeed");
		add("/my/favorite",FavoriteController.class);
		add("/my/setting", MySettingController.class);
		add("/my/share", MyShareController.class);
		add("/my/project", MyProjectController.class);
		add("/my/feedback", MyFeedbackController.class);
		add("/my/newsfeed", NewsFeedController.class);
		add("/my/message", MessageController.class);
		//add("/my/setting/password", MySettingController.class);
		
		
		add("/reg",RegController.class);
		add("/login",LoginController.class);
	}

}
