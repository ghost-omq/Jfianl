package omq.common.pageView;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.plugin.ehcache.CacheKit;

public class PageViewService {

	public static final PageViewService me = new PageViewService();

	@SuppressWarnings("serial")
	private Map<String, String> actionKeyToCacheName = new HashMap<String, String>(){{
		put("/project/detail", "projectPageView");
		put("/share/detail", "sharePageView");
		put("/feedback/detail", "feedbackPageView");
	}};
	
	public void processPageView(String actionKey, Integer id, String ip) {
		if (id == null) {
			throw new IllegalArgumentException("id 值不能为 null.");
		}
		String cacheName = actionKeyToCacheName.get(actionKey);
		if (cacheName == null) {
			throw new RuntimeException("不支持的 actionKey： " + actionKey);
		}

		if (ip == null) {
			ip = "127.0.0.1";
		}

		String pageViewKey = actionKey + ip;
		Integer idInCache = CacheKit.get("pageViewIp", pageViewKey);

		if (idInCache == null || !id.equals(idInCache)) {
			Integer visitCount = CacheKit.get(cacheName, id);
			visitCount = (visitCount != null ? visitCount + 1 : 1);
			CacheKit.put(cacheName, id, visitCount);

			CacheKit.put("pageViewIp", pageViewKey, id);
		}
	}
}
