package omq.my.newsfeed;

import com.jfinal.plugin.activerecord.Page;

import omq.common.controller.BaseController;
import omq.common.model.NewsFeed;

public class NewsFeedController extends BaseController{
	
	static NewsFeedService srv = NewsFeedService.me;

	//@ActionKey("/my")
	public void newsFeed() {
		Page<NewsFeed> newsFeedPage = srv.paginate(getLoginAccountId(), getParaToInt("p", 1));
		setAttr("newsFeedPage", newsFeedPage);
		setAttr("paginateLink", "/my?p=");
		render("index.html");
	}
	
	public void referMe() {
		Page<NewsFeed> newsFeedPage = ReferMeService.me.paginate(getLoginAccountId(), getParaToInt("p", 1));
		setAttr("newsFeedPage", newsFeedPage);
		setAttr("paginateLink", "/my/referMe?p=");
		render("index.html");
	}
}
