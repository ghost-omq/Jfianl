package omq.my.newsfeed;

import com.jfinal.core.ActionKey;
import com.jfinal.plugin.activerecord.Page;

import omq.common.controller.BaseController;
import omq.common.model.NewsFeed;

public class NewsFeedController extends BaseController{
	
	static NewsFeedService srv = NewsFeedService.me;

	@ActionKey("/my")
	public void newsFeed() {
		Page<NewsFeed> newsFeedPage = srv.paginate(getLoginAccountId(), getParaToInt("p", 1));
		setAttr("newsFeedPage", newsFeedPage);
		setAttr("paginateLink", "/my?p=");                  // 用于指定重用页面分页宏所使用的 link
		render("index.html");
	}
}
