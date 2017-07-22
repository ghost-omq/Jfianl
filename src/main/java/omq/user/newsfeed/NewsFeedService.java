package omq.user.newsfeed;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;

import omq.common.account.AccountService;
import omq.common.model.NewsFeed;

public class NewsFeedService {
	
	public static final NewsFeedService me = new NewsFeedService();
	final String newsFeedPageCacheName = "newsFeedPage";
	final NewsFeed dao = new NewsFeed().dao();
	final int pageSize = 15;
	
	private Page<NewsFeed> doPaginate(String cacheName, Object cacheKey, int pageNum, String select, String from, Object... paras) {
        Page<NewsFeed> newsFeedPage = dao.paginate(pageNum, pageSize, select, from, paras);
        AccountService.me.joinNickNameAndAvatar(newsFeedPage.getList());
        CacheKit.put(cacheName, cacheKey, newsFeedPage);
		return newsFeedPage;
	}
	
	public Page<NewsFeed> paginateForUserSpace(int accountId, int pageNum) {
		String cacheKey =  accountId + "_user_" + pageNum;
        Page<NewsFeed> newsFeedPage = CacheKit.get(newsFeedPageCacheName, cacheKey);
        if (newsFeedPage == null) {
            String select = "select nf.*";
            String from = "from news_feed nf where nf.accountId=? order by id desc";
            newsFeedPage = doPaginate(newsFeedPageCacheName, cacheKey, pageNum, select, from, accountId);
        }
        return newsFeedPage;
	}

}
