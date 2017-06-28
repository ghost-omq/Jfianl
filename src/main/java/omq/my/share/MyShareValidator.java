package omq.my.share;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class MyShareValidator extends Validator{

	protected void validate(Controller c) {
		validateRequiredString("share.title", "msg", "请输标题!");
		validateRequiredString("share.content", "msg", "请输入内容!");
		
		validateString("share.title", 3, 10, "msg", "标题长度要求在3到100个字符");
		validateString("share.content", 19, 65536, "msg", "正文内容太少啦，多写点蛤");
	}

	protected void handleError(Controller c) {
		c.renderJson();
	}

}
