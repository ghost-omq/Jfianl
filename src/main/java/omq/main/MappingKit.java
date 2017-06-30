package omq.main;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

import omq.common.model.Account;
import omq.common.model.AuthCode;
import omq.common.model.Document;
import omq.common.model.Download;
import omq.common.model.DownloadLog;
import omq.common.model.Favorite;
import omq.common.model.Feedback;
import omq.common.model.FeedbackReply;
import omq.common.model.Message;
import omq.common.model.NewsFeed;
import omq.common.model.Project;
import omq.common.model.ReferMe;
import omq.common.model.Remind;
import omq.common.model.Session;
import omq.common.model.Share;
import omq.common.model.ShareReply;
import omq.common.model.TaskList;

public class MappingKit {
	
	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("account", "id", Account.class);
		arp.addMapping("auth_code", "id", AuthCode.class);
		arp.addMapping("document", "mainMenu,subMenu", Document.class);
		arp.addMapping("download", "id", Download.class);
		arp.addMapping("download_log", "id", DownloadLog.class);
		arp.addMapping("favorite", "id", Favorite.class);
		arp.addMapping("feedback", "id", Feedback.class);
		arp.addMapping("feedback_reply", "id", FeedbackReply.class);
		arp.addMapping("message", "id", Message.class);
		arp.addMapping("news_feed", "id", NewsFeed.class);
		arp.addMapping("project", "id", Project.class);
		arp.addMapping("refer_me", "id", ReferMe.class);
		arp.addMapping("remind", "accountId", Remind.class);
		arp.addMapping("session", "id", Session.class);
		arp.addMapping("share", "id", Share.class);
		arp.addMapping("share_reply", "id", ShareReply.class);
		arp.addMapping("task_list", "id", TaskList.class);
		
	}

}
