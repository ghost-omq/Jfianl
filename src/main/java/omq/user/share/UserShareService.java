package omq.user.share;

import java.util.List;

import omq.common.model.Share;

public class UserShareService  {

	public static final UserShareService me = new UserShareService();
	private final Share dao = new Share().dao();

	public List<Share> findAll(int accountId) {
		return dao.find("select * from share where accountId=? order by createAt desc", accountId);
	}
}

