package omq.my.setting;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;
import omq.common.controller.BaseController;
import omq.common.model.Account;
import omq.login.LoginService;

public class MySettingController extends BaseController{
	
	public static final MySettingService srv = new MySettingService().me;

	public void info() {
		render("info.html");
	}
	
	public void uploadAvatar(){
		UploadFile uf = null;
		try{
			uf = getFile("avatar",srv.getAvatarTempDir(),srv.getAvatarMaxSize());
			if(uf == null){
				renderJson(Ret.fail("msg","请选择上传文件"));
				return;
			}
		} catch(Exception e){
			if (e instanceof com.oreilly.servlet.multipart.ExceededSizeException) {
				renderJson(Ret.fail("msg", "文件大小超出范围"));
			} else {
				if (uf != null) {
					uf.getFile().delete();
				}
				renderJson(Ret.fail("msg", e.getMessage()));
			}
			return ;
		}
		
		Ret ret = srv.uploadAvatar(123456, uf);
		if(ret.isOk()){
			setSessionAttr("avatarUrl", ret.get("avatarUrl"));
		}
		renderJson(ret);
	}
	
	public void saveAvatar() {
		String avatarUrl = getSessionAttr("avatarUrl");
		int x = getParaToInt("x");
		int y = getParaToInt("y");
		int width = getParaToInt("width");
		int height = getParaToInt("height");
		Ret ret = srv.saveAvatar(getLoginAccount(), avatarUrl, x, y, width, height);
		renderJson(ret);
	}
	
	public void index() {
		render("password.html");
	}
	
	public void updatePassword() {
		Account loginAccount = getAttr(LoginService.loginAccountCacheName);
		Ret ret = srv.updatePassword(loginAccount.getId(), getPara("oldPassword"), getPara("newPassword"));
		renderJson(ret);
	}
}
