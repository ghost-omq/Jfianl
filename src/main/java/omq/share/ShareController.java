package omq.share;

import com.jfinal.aop.Before;

import omq.common.controller.BaseController;
import omq.common.model.Share;
import omq.common.pageView.PageViewInterceptor;

public class ShareController extends BaseController{

	static ShareService srv = ShareService.me;

	public void index() {
		setAttr("sharePage", srv.paginate(getParaToInt("p", 1)));
		//setAttr("hotShare", srv.getHotShare());
		render("index.html");
	}

	@Before(PageViewInterceptor.class)
	public void detail() {
		Share share = srv.findById(getParaToInt());
		setAttr("share", share);
		//setAttr("hotShare", srv.getHotShare());
		render("detail.html");

	}
}
