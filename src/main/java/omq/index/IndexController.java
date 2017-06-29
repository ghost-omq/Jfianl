package omq.index;

import java.util.List;

import com.jfinal.core.Controller;

import omq.common.model.Feedback;
import omq.common.model.Project;
import omq.common.model.Share;

public class IndexController extends Controller{
	
	static IndexService srv = IndexService.me;

	public void index(){
		List<Project> projectList = srv.getProjectList();
		List<Share> shareList = srv.getShareList();
		List<Feedback> feedbackList = srv.getFeedbackList();

		setAttr("projectList", projectList);
		setAttr("shareList", shareList);
		setAttr("feedbackList", feedbackList);

		render("index.html");
	}
}
