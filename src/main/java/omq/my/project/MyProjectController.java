package omq.my.project;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import omq.common.model.Project;

public class MyProjectController extends Controller{
	
	static final MyProjectService srv = new MyProjectService().me;

	public void index(){
		List<Project> projectList = srv.findAll(1);
		setAttr("projectList", projectList);
		render("index.html");
	}
	
	public void add(){
		render("add.html");
	}
	
	@Before(MyProjectvalidator.class)
	public void save(){
		srv.save(1, getModel(Project.class));
		renderJson("isOk",true);
	}
	
	public void edit(){
		Project projectId = srv.findById(1, getParaToInt("id"));
		setAttr("project", projectId);
		render("edit.html");
	}
	
	@Before(MyProjectvalidator.class)
	public void update(){
		srv.update(1, getModel(Project.class));
		renderJson("isOk",true);
	}
	
	public void delete(){
		srv.delete(1, getParaToInt("id"));
		redirect("/my/project");
	}
}
