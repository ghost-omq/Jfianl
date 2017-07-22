package omq.user.feedback;

import java.util.List;

import omq.common.model.Feedback;

public class UserFeedbackService {

	public static final UserFeedbackService me = new UserFeedbackService();
	private final Feedback dao = new Feedback().dao();

	public List<Feedback> findAll(int accountId) {
		return dao.find("select * from feedback where accountId=? order by createAt desc", accountId);
	}
}
