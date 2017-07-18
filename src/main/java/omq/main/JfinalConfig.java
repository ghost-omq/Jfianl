package omq.main;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;

import omq.common.handler.UrlSeoHandler;
import omq.common.interceptor.LoginSessionInterceptor;
import omq.helloTest.HelloJfinal;

public class JfinalConfig extends JFinalConfig{
	
	private static Prop p = loadConfig();
	private WallFilter wallFilter;
	
	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 8080, "/", 5);
	}
	
	
	
	private static Prop loadConfig() {
		try {
			return PropKit.use("jfinal_club_config_pro.txt");
		} catch (Exception e) {
			return PropKit.use("jfinal_club_config_dev.txt");
			
		}
	}
	

	public void configConstant(Constants me) {
		me.setDevMode(p.getBoolean("devMode", false));
		me.setJsonFactory(MixedJsonFactory.me());
	}

	public void configRoute(Routes me) {
		me.add(new ConfigRoute());
	}
	

	public void configEngine(Engine me) {
		me.setDevMode(p.getBoolean("engineDevMode", false));
    	me.addSharedFunction("/view/common/__layout.html");
    	me.addSharedFunction("/view/common/_paginate.html");
	    me.addSharedFunction("/view/_admin/common/__admin_layout.html");
	}
	
	public static DruidPlugin getDruidPlugin() {
		return new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p.get("password").trim());
	}

	public void configPlugin(Plugins me) {
		DruidPlugin druidPlugin = getDruidPlugin();
	    wallFilter = new WallFilter();
	    wallFilter.setDbType("mysql");
	    druidPlugin.addFilter(wallFilter);
	    druidPlugin.addFilter(new StatFilter());
	    me.add(druidPlugin);
	    
	    
	    ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
	    MappingKit.mapping(arp);
	    arp.setBaseSqlTemplatePath(PathKit.getRootClassPath() + "/sql");
        arp.addSqlTemplate("all_sqls.sql");
	    me.add(arp);
	    
	    me.add(new EhCachePlugin());
	}

	public void configInterceptor(Interceptors me) {
		me.add(new LoginSessionInterceptor());
	}

	public void configHandler(Handlers me) {
		me.add(new UrlSeoHandler());
	}

}
