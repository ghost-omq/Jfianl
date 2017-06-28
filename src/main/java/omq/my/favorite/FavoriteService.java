package omq.my.favorite;

import java.sql.SQLException;
import java.util.List;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import omq.common.model.Favorite;

public class FavoriteService {

	public static final FavoriteService me = new FavoriteService();
    final Favorite dao = new Favorite().dao();
    
    public List<Favorite> findAll(int accountId) {
    	return dao.find("select * from favorite where accountId=? order by id desc", accountId);
    }
    
    private Ret delete(final int myId, final String refTypeTable, final int refId) {
        boolean isOk = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                int n = Db.update("delete from favorite where accountId=? and refType=? and refId=? limit 1", myId, Favorite.getRefType(refTypeTable), refId);
                if (n > 0) {
                    n = Db.update("update " + refTypeTable + " set favoriteCount=favoriteCount-1 where id=? and favoriteCount>0 limit 1", refId);
                }
                return n > 0;
            }
        });
        return isOk ? Ret.ok() : Ret.fail("msg", "取消收藏失败");
    }
    
    public void deleteByFavoriteId(int accountId, int favoriteId) {
        Favorite f = dao.findById(favoriteId);
        if (f != null) {
            delete(accountId, Favorite.getRefTable(f.getRefType()), f.getRefId());
        }
    }
}
