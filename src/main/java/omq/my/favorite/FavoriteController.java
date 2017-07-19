package omq.my.favorite;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import omq.common.controller.BaseController;
import omq.common.model.Favorite;
import omq.my.like.LikeInterceptor;

@Before({LikeInterceptor.class})
public class FavoriteController extends BaseController{
	
	static FavoriteService srv = FavoriteService.me;

	public void index(){
		List<Favorite> favoriteList = srv.findAll(getLoginAccountId());
        setAttr("favoriteList", favoriteList);
        render("index.html");
	}
	
	public void delete() {
        srv.deleteByFavoriteId(getLoginAccountId(), getParaToInt("id"));
        redirect("/my/favorite");
    }
}
