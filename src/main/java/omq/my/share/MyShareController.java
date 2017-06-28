package omq.my.share;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import omq.common.model.Share;

public class MyShareController extends Controller{
	
	static final MyShareService srv = new MyShareService().me;

	public void index(){
		List<Share> shareList = srv.findAll(1);
		setAttr("shareList", shareList);
		render("index.html");
	}
	
	public void add(){
		render("add.html");
	}
	
	@Before(MyShareValidator.class)
	public void save(){
		srv.save(1, getModel(Share.class));
		renderJson("isOk",true);
	}
	
	public void edit(){
		Share share = srv.findById(1, getParaToInt("id"));
		setAttr("share", share);
		render("edit.html");
	}
	
	@Before(MyShareValidator.class)
	public void update(){
		srv.update(1, getModel(Share.class));
		renderJson("isOk", true);
	}
	
	
	public void delete(){
		srv.delete(1, getParaToInt("id"));
		redirect("/my/share");
	}
}
