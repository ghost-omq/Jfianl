package omq.login;

import com.jfinal.club.common.kit.IpKit;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;

public class LoginController extends Controller{

	static final LoginService srv = new LoginService().me;
	
	public void index(){
		keepPara("returnUrl");
		render("index.html");
	}
	
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
	
	public void captcha() {
		renderCaptcha();
	}
}
