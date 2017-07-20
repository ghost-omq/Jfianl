package omq.my.project;

import java.util.List;

import com.jfinal.aop.Before;

import omq.common.controller.BaseController;
import omq.common.interceptor.FrontAuthInterceptor;
import omq.common.model.Project;
import omq.my.friend.FriendInterceptor;
import omq.my.like.LikeInterceptor;

@Before({LikeInterceptor.class,FriendInterceptor.class,FrontAuthInterceptor.class})
public class MyProjectController extends BaseController{
	
	static final MyProjectService srv = new MyProjectService().me;

	public void index(){
		List<Project> projectList = srv.findAll(getLoginAccountId());
		setAttr("projectList", projectList);
		render("index.html");
	}
	
	public void add(){
		render("add.html");
	}
	
	@Before(MyProjectValidator.class)
	public void save(){
		srv.save(getLoginAccountId(), getModel(Project.class));
		renderJson("isOk",true);
	}
	
	public void edit(){
		Project projectId = srv.findById(getLoginAccountId(), getParaToInt("id"));
		setAttr("project", projectId);
		render("edit.html");
	}
	
	@Before(MyProjectValidator.class)
	public void update(){
		srv.update(getLoginAccountId(), getModel(Project.class));
		renderJson("isOk",true);
	}
	
	public void delete(){
		srv.delete(getLoginAccountId(), getParaToInt("id"));
		redirect("/my/project");
	}
}
