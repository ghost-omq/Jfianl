package omq.my.feedback;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import omq.my.project.MyProjectService;

public class MyFeedbackValidator extends Validator{

	protected void validate(Controller c) {
		validateRequiredString("feedback.title", "msg", "请输标题!");
		validateRequiredString("feedback.content", "msg", "请输入内容!");
		
		String projectName = c.getPara("project.name");
		
		if("save".equals(getActionMethod().getName())){
			if(MyProjectService.me.isProjectNameExists(projectName)){
				addError("msg", "项目名称已经存在");
			}
		}
		else if("update".equals(getActionMethod().getName())){
			int projectId = c.getParaToInt("project.id");
			if(MyProjectService.me.isProjectNameExists(projectId, projectName)){
				addError("msg","项目名称已经存在");
			}
		}
		
		validateString("feedback.title", 3, 10, "msg", "标题长度要求在3到100个字符");
		validateString("feedback.content", 19, 65536, "msg", "正文内容太少啦，多写点蛤");
	}

	protected void handleError(Controller c) {
		c.renderJson();
	}
}
