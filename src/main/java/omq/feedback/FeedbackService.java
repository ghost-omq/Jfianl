
package omq.feedback;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

import omq.common.model.Feedback;

public class FeedbackService {

	public static final FeedbackService me = new FeedbackService();
	private final Feedback dao = new Feedback().dao();

	public Page<Feedback> paginate(int pageNumber) {
		SqlPara sqlPara = dao.getSqlPara("feedback.paginate", Feedback.REPORT_BLOCK_NUM);
		Page<Feedback> feedbackPage = dao.paginate(pageNumber, 15, sqlPara);
		return feedbackPage;
	}

	public Feedback findById(int feedbackId) {
		SqlPara sqlPara = dao.getSqlPara("feedback.findById", feedbackId, Feedback.REPORT_BLOCK_NUM);
		return dao.findFirst(sqlPara);
	}

	public List<Feedback> getHotFeedback() {
		SqlPara sqlPara = dao.getSqlPara("feedback.getHotFeedback", Feedback.REPORT_BLOCK_NUM);
		return dao.find(sqlPara);
	}

}
