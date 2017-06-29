package omq.login;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class LoginValidator extends Validator{
	protected void validate(Controller c) {
		setShortCircuit(true);

		validateRequired("userName", "userNameMsg", "邮箱不能为空");
		validateEmail("userName", "userNameMsg", "邮箱格式不正确");

		validateRequired("password", "passwordMsg", "密码不能为空");
		validateCaptcha("captcha", "captchaMsg", "验证码不正确");
	}

	protected void handleError(Controller c) {
		c.renderJson();
	}

}
