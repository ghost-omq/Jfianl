package omq.my.share;

import java.util.List;

import com.jfinal.aop.Before;

import omq.common.controller.BaseController;
import omq.common.interceptor.FrontAuthInterceptor;
import omq.common.model.Share;
import omq.my.friend.FriendInterceptor;
import omq.my.like.LikeInterceptor;

@Before({LikeInterceptor.class,FriendInterceptor.class,FrontAuthInterceptor.class})
public class MyShareController extends BaseController{
	
	static final MyShareService srv = new MyShareService().me;

	public void index(){
		List<Share> shareList = srv.findAll(getLoginAccountId());
		setAttr("shareList", shareList);
		render("index.html");
	}
	
	public void add(){
		render("add.html");
	}
	
	@Before(MyShareValidator.class)
	public void save(){
		srv.save(getLoginAccountId(), getModel(Share.class));
		renderJson("isOk",true);
	}
	
	public void edit(){
		Share share = srv.findById(getLoginAccountId(), getParaToInt("id"));
		setAttr("share", share);
		render("edit.html");
	}
	
	@Before(MyShareValidator.class)
	public void update(){
		srv.update(getLoginAccountId(), getModel(Share.class));
		renderJson("isOk", true);
	}
	
	
	public void delete(){
		srv.delete(getLoginAccountId(), getParaToInt("id"));
		redirect("/my/share");
	}
}
