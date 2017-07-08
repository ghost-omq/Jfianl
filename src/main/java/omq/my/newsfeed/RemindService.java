package omq.my.newsfeed;

import com.jfinal.plugin.ehcache.CacheKit;

import omq.common.model.Remind;

public class RemindService {

	public static final RemindService me = new RemindService();
	private String remindCacheName = "remind";
	private final Remind dao = new Remind().dao();
	
	public Remind getRemind(int accountId) {
		Remind remind = CacheKit.get(remindCacheName, accountId);
		if (remind == null) {
			remind = dao.findById(accountId);
			if (remind == null) {
				remind = new Remind();
				remind.setAccountId(null);
			}
			CacheKit.put(remindCacheName, accountId, remind);
		}
		return remind.getAccountId() != null ? remind : null;
	}
}
