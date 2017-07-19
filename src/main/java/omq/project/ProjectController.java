package omq.project;

import com.jfinal.aop.Before;

import omq.common.controller.BaseController;
import omq.common.model.Project;
import omq.common.pageView.PageViewInterceptor;

@Before(ProjectSeo.class)
public class ProjectController extends BaseController{
	
	static ProjectService srv = ProjectService.me;

	public void index() {
		setAttr("projectPage", srv.paginate(getParaToInt("p", 1)));
		setAttr("hotProject", srv.getHotProject());
		render("index.html");
	}
	
	@Before(PageViewInterceptor.class)
	public void detail() {
		Project project = srv.findById(getParaToInt());
			setAttr("project", project);
			setAttr("hotProject", srv.getHotProject());
			render("detail.html");
		
	}
}
