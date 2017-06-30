package omq.reg;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;

import omq.common.kit.IpKit;

public class RegController extends Controller{
	
	static final RegService srv = new RegService().me;

	public void index(){
		render("index.html");
	}
	
	@Before(RegValidator.class)
	public void save(){
		String ip = IpKit.getRealIp(getRequest());
		Ret reg = srv.reg(getPara("userName"), getPara("password"), getPara("nickName"), ip);
		if (reg.isOk()) {
			reg.set("regEmail", getPara("userName"));
		}
		
		renderJson(reg);
	}
	
	public void notActivated() {
		render("not_activated.html");
	}
	
	public void reSendActivateEmail(){
		Ret ret = srv.reSendActivateEmail(getPara("email"));
		renderJson(ret);
	}
	
	
	public void captcha() {
		renderCaptcha();
	}
}
