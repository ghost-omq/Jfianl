package omq.main;

import com.jfinal.config.Routes;

import omq.helloTest.HelloJfinal;
import omq.my.MyShareController;

public class ConfigRoute extends Routes{

	public void config() {
		setBaseViewPath("/view");
		
		add("/index",HelloJfinal.class);
		add("/my/share",MyShareController.class);
	}

}
