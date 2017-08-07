package omq.user.newsfeed;

import com.jfinal.core.ActionKey;
import com.jfinal.plugin.activerecord.Page;

import omq.common.controller.BaseController;
import omq.common.model.NewsFeed;

public class UserNewsFeedController extends BaseController{
	
	static NewsFeedService srv = new NewsFeedService().me;
	
	@ActionKey("/user")
	public void newsfeed(){
		Integer paraToInt = getParaToInt();
		Integer paraToInt2 = getParaToInt("p", 1);
		Page<NewsFeed> newsFeedPage = srv.paginateForUserSpace(paraToInt, paraToInt2);
		setAttr("newsFeedPage", newsFeedPage);
        setAttr("paginateLink", "/user/" + getParaToInt() + "?p=");
        render("index.html");
	}

	
}
