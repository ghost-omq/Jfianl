package omq.my.like;

import com.jfinal.kit.Ret;

import omq.common.controller.BaseController;

public class LikeController extends BaseController{
	
	static LikeService srv = new LikeService().me;
	
	public void index(){
		if(notLogin()){
			renderJson(Ret.fail("msg", "请先登录"));
			return;
		}
		
		Ret ret = srv.like(getLoginAccountId(), getPara("refType"), getParaToInt("refId"), getParaToBoolean("isAdd")));
		renderJson(ret);
	}

}
