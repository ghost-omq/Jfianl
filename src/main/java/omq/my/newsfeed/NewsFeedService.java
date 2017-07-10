package omq.my.newsfeed;

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
        //loadRefData(newsFeedPage);
        CacheKit.put(cacheName, cacheKey, newsFeedPage);
		return newsFeedPage;
	}
	
	public Page<NewsFeed> paginate(int accountId, int pageNum) {
        String cacheKey =  accountId + "_" + pageNum;
        Page<NewsFeed> newsFeedPage = CacheKit.get(newsFeedPageCacheName, cacheKey);
        if (newsFeedPage == null) {
            String select = "select nf.*";
            StringBuilder from = new StringBuilder()
                    .append("from ( ")
                    .append("       select ft.accountId, ft.friendId from friend ft union all (select ").append(accountId).append(", ").append(accountId).append(") ")
                    .append(") as f inner join news_feed nf on f.friendId=nf.accountId and f.accountId=? order by id desc");
            newsFeedPage = doPaginate(newsFeedPageCacheName, cacheKey, pageNum, select, from.toString(), accountId);
        }
        return newsFeedPage;
    }
	
}
