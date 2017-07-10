package omq.login;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;

import omq.common.controller.BaseController;
import omq.common.kit.IpKit;

public class LoginController extends BaseController{

	static final LoginService srv = new LoginService().me;
	
	public void index(){
		keepPara("returnUrl");
		render("index.html");
	}
	
	@Before(LoginValidator.class)
	public void doLogin(){
		boolean keepLogin = getParaToBoolean("keepLogin", false);
		String loginIp = IpKit.getRealIp(getRequest());
		Ret ret = srv.login(getPara("userName"), getPara("password"), keepLogin, loginIp);
		if (ret.isOk()) {
			String sessionId = ret.getStr(LoginService.sessionIdName);
			int maxAgeInSeconds = ret.getInt("maxAgeInSeconds");
			setCookie(LoginService.sessionIdName, sessionId, maxAgeInSeconds, true);
			setAttr(LoginService.loginAccountCacheName, ret.get(LoginService.loginAccountCacheName));

			ret.set("returnUrl", getPara("returnUrl", "/"));
		}
		renderJson(ret);
	}
	
	@Clear
	@ActionKey("/logout")
	public void logout(){
		srv.logout(LoginService.sessionIdName);
		removeCookie(LoginService.sessionIdName);
		redirect("/");
	}
	
	public void captcha() {
		renderCaptcha();
	}
}
