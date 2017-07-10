package omq.my.feedback;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import omq.common.controller.BaseController;
import omq.common.model.Feedback;

public class MyFeedbackController extends BaseController{
	
	static final MyFeedbackService srv = new MyFeedbackService().me;

	public void index(){
		List<Feedback> feedback = srv.findAll(getLoginAccountId());
		setAttr("feedbackList", feedback);
		render("index.html");
	}
	
	public void add(){
		render("add.html");
	}
	
	@Before(MyFeedbackValidator.class)
	public void save(){
		srv.save(getLoginAccountId(),getModel(Feedback.class));
		renderJson("isOk",true);
	}
	
	public void edit(){
		Feedback findById = srv.findById(1, getParaToInt("id"));
		setAttr("feedback", findById);
		render("edit.html");
	}
	
	@Before(MyFeedbackValidator.class)
	public void update(){
		srv.update(1, getModel(Feedback.class));
		renderJson("isOk",true);
	}
	
	public void delete(){
		srv.delete(1, getParaToInt("id"));
		redirect("/my/feedback");
	}
}
