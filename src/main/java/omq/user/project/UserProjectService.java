package omq.user.project;

import java.util.List;

import omq.common.model.Project;

public class UserProjectService {

	public static final UserProjectService me = new UserProjectService();
	private final Project dao = new Project().dao();

	public List<Project> findAll(int accountId) {
		return dao.find("select * from project where accountId=? order by createAt desc", accountId);
	}
}

