package omq.project;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

import omq.common.model.Project;

public class ProjectService {
	
	public static final ProjectService me = new ProjectService();
	private final Project dao = new Project().dao();

	
	public Page<Project> paginate(int pageNumber) {
		SqlPara sqlPara = dao.getSqlPara("project.paginate", Project.REPORT_BLOCK_NUM);
		Page<Project> projectPage = dao.paginate(pageNumber, 15, sqlPara);
		return projectPage;
	}
	
	public List<Project> getHotProject() {
		SqlPara sqlPara = dao.getSqlPara("project.getHotProject", Project.REPORT_BLOCK_NUM);
		return dao.findByCache("hotProject", "hotProject", sqlPara.getSql(), sqlPara.getPara());
	}
	
	public Project findById(int projectId) {
		SqlPara sqlPara = dao.getSqlPara("project.findById", projectId, Project.REPORT_BLOCK_NUM);
		return dao.findFirst(sqlPara);
	}
}
