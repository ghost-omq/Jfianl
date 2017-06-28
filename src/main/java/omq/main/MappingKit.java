package omq.main;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

import omq.common.model.Favorite;
import omq.common.model.Feedback;
import omq.common.model.Project;
import omq.common.model.Share;

public class MappingKit {
	
	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("share", "id",Share.class);
		arp.addMapping("feedback", "id",Feedback.class);
		arp.addMapping("project", "id",Project.class);
		arp.addMapping("favorite", Favorite.class);
	}

}
