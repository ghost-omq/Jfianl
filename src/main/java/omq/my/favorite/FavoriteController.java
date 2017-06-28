package omq.my.favorite;

import java.util.List;

import com.jfinal.core.Controller;

import omq.common.model.Favorite;

public class FavoriteController extends Controller{
	
	static FavoriteService srv = FavoriteService.me;

	public void index(){
		List<Favorite> favoriteList = srv.findAll(1);
        setAttr("favoriteList", favoriteList);
        render("index.html");
	}
	
	public void delete() {
        srv.deleteByFavoriteId(1, getParaToInt("id"));
        redirect("/my/favorite");
    }
}
