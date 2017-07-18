package omq.project;

import com.jfinal.core.Controller;

import omq.common.interceptor.BaseSeoInterceptor;
import omq.common.model.Project;

public class ProjectSeo extends BaseSeoInterceptor {
	
	@Override
	public void indexSeo(Controller c) {
		setSeoTitle(c, "JFinal 极速开发项目");
		setSeoKeywords(c, "JFinal, JFinal Weixin, JFinal demo, JFinal 微信, JFinal 项目, JFinal 案列, JFinal 插件, JFinal 教程");
		setSeoDescr(c, "JFinal 极速开发项目集合, JFinal 优秀项目收录, JFinal 学习资源, JFinal 教程, JFinal 案例, JFinal 实战");
	}

	@Override
	public void detailSeo(Controller c) {
		Project project = c.getAttr("project");
		setSeoTitle(c, project.getTitle());
		setSeoKeywords(c, project.getName() + "," + project.getTitle());
		setSeoDescr(c, project.getName() + "," + project.getTitle());
	}

	@Override
	public void othersSeo(Controller c, String method) {

	}

}
