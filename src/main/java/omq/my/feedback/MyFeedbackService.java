package omq.my.feedback;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import omq.common.model.Feedback;

public class MyFeedbackService {
	
	static final MyFeedbackService me = new MyFeedbackService();
	private final Feedback dao = new Feedback().dao(); 

	public List<Feedback> findAll(int accountId){
		return dao.find("select * from feedback where accountId=? order by createAt desc",accountId);
	}
	
	public Feedback findById(int accountId, int feedbackId){
		return dao.findFirst("select * from feedback where accountId=? and id=?",accountId,feedbackId);
	}
	
	public void save(int accountId, Feedback feedback){
		feedback.setAccountId(accountId);
		feedback.setCreateAt(new Date());
		feedback.setClickCount(0);
		feedback.save();
	}
	
	public void update(int accountId, Feedback feedback){
		Db.queryInt("select accountId from feedback where id=?",feedback.getId());
		feedback.update();
	}
	
	public void delete(final int accountId, final int feedbackId){
		Db.tx(new IAtom(){
			
			public boolean run() throws SQLException {
				return Db.update("delete from feedback where accountId=? and id=?", accountId, feedbackId) > 0;
			}
			
			
		});
	}
}
