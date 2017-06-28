package omq.my.project;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import omq.common.model.Project;

public class MyProjectService {
	
	public static final MyProjectService me = new MyProjectService();
	public final Project dao = new Project().dao();

	public List<Project> findAll(int accountId){
		return dao.find("select * from project where accountId=? order by createAt desc",accountId);
	}
	
	public Project findById(int accountId, int projectId){
		return dao.findFirst("select * from project where accountId=? and id=?",accountId,projectId);
	}
	
	public boolean isProjectNameExists(String projectName){
		return Db.queryInt("select id from project where lower(name)=? ",projectName) != null;
	}
	
	public boolean isProjectNameExists(int existsProjectId, String projectName){
		return Db.queryInt("select id from project where id !=? and lower(name)=?",existsProjectId,projectName) !=null;
	}
	
	public void save(int accountId, Project project){
		project.setAccountId(accountId);
		project.setCreateAt(new Date());
		project.setClickCount(0);
		project.save();
	}
	
	public void update(int accountId ,Project project){
		Db.queryInt("select accountId from project where id=?",project.getId());
		project.update();
	}
	
	public void delete(final int accountId, final int feedbackId) {
		Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				return Db.update("delete from project where accountId=? and id=?", accountId, feedbackId) > 0;
			}
		});
	}

}
