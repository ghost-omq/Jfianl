package omq.main;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

import omq.common.model.Share;

public class MappingKit {
	
	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("share", "id",Share.class);
	}

}
