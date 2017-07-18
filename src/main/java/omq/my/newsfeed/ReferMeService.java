package omq.my.newsfeed;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;

import omq.common.model.NewsFeed;
import omq.common.model.ReferMe;

public class ReferMeService {
	
	public static final ReferMeService me = new ReferMeService();
	
	final ReferMe dao = new ReferMe().dao();
	final NewsFeed newsFeedDao = new NewsFeed().dao();
	final String referMePageCacheName = "referMePage";
	final int pageSize = 15;

	public Page<NewsFeed> paginate(int accountId, int pageNum) {
		String cacheKey = accountId + "_" + pageNum;
		Page<NewsFeed> newsFeedPage = CacheKit.get(referMePageCacheName, cacheKey);
		if (newsFeedPage == null) {
			String s = "select newsFeedId";
			String f = "from refer_me where referAccountId=? order by id desc";
			Page<ReferMe> referMePage = dao.paginate(pageNum, pageSize, s, f, accountId);
			if (referMePage.getList().size() == 0) {
				newsFeedPage =  new Page<NewsFeed>(new ArrayList<NewsFeed>(), pageNum, pageSize, 0, 0);
				CacheKit.put(referMePageCacheName, cacheKey, newsFeedPage);
				return newsFeedPage;
			}

			StringBuilder sql = new StringBuilder("select nf.*, a.avatar, a.nickName ");
			sql.append("from news_feed nf inner join account a on nf.accountId=a.id where nf.id in(");
			apppendNewsFeedIds(referMePage.getList(), sql);
			sql.append(") order by id desc");

			List<NewsFeed> newsFeedList = newsFeedDao.find(sql.toString());
			newsFeedPage = new Page<NewsFeed>(newsFeedList, pageNum, pageSize, referMePage.getTotalPage(), referMePage.getTotalRow());

			CacheKit.put(referMePageCacheName, cacheKey, newsFeedPage);
		}
		return newsFeedPage;
	}
	
	private void apppendNewsFeedIds(List<ReferMe> list, StringBuilder ret) {
		for (int i=0, size=list.size(); i<size; i++) {
			if (i > 0) {
				ret.append(", ");
			}
			ret.append(list.get(i).getNewsFeedId());
		}
	}
}
