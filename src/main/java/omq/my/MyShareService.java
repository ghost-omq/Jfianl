package omq.my;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import omq.common.model.Share;


public class MyShareService {
	
	public static final MyShareService me = new MyShareService();
	private final Share dao = new Share().dao();

	public List<Share> findAll(int accountId) {
		return dao.find("select * from share where accountId=? order by createAt desc", accountId);
	}
	
	public Share findById(int accountId, int shareId){
		return dao.findFirst("select * from share where accountId=? and id=?",accountId,shareId);
		
	}
	
	public void save(int accountId, Share share){
		share.setAccountId(accountId);
		share.setCreateAt(new Date());
		share.setClickCount(0);
		share.save();
	}
	
	public void update(int accoutId, Share share){
		Db.queryInt("select accountId from share where id=? limit 1", share.getId());
		share.update();
	}
	
	public void delete(final int accountId, final int shareId){
		Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				return Db.update("delete from share where accountId=? and id=?", accountId, shareId) > 0;
			}
		});
	}
}
