package omq.reg;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class RegValidator extends Validator{

	protected void validate(Controller c) {
		validateRequired("nickName", "nickNameMsg", "昵称不能为空");
		//validateString("nickName", 1, 19, "nickNameMsg", "昵称不能超过19个字");
		if (RegService.me.isNickNameExists(c.getPara("nickName"))) {
			addError("nickNameMsg", "昵称已被注册");
		}
		
		validateRequired("userName", "userNameMsg", "邮箱不能为空");
		if(RegService.me.isUserNameExists(c.getPara("userName"))) {
			addError("userNameMsg", "邮箱已被注册");
		}
		
		validateString("password", 6, 100, "passwordMsg", "密码长度不能小于6");
		validateCaptcha("captcha", "captchaMsg", "验证码不正确");
	}

	protected void handleError(Controller c) {
		c.renderJson();
	}

}
