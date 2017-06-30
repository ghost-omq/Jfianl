package omq.index;

import java.util.List;

import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.ehcache.CacheKit;

import omq.common.model.Feedback;
import omq.common.model.Project;
import omq.common.model.Share;

public class IndexService {

	public static final IndexService me = new IndexService();
	private String indexCacheName = "index";
	private Project projectDao = new Project().dao();
	private Share shareDao = new Share().dao();
	private Feedback feedbackDao = new Feedback().dao();
	
	public List<Project> getProjectList() {
		/*
		SqlPara sqlPara = projectDao.getSqlPara("index.getProjectList", Project.REPORT_BLOCK_NUM);
		List<Project> projectList =  projectDao.findByCache(indexCacheName, "projectList", sqlPara.getSql(), sqlPara.getPara());
		return projectList;
		*/
		SqlPara sqlPara = projectDao.getSqlPara("index.getProjectList", Project.REPORT_BLOCK_NUM);
		List<Project> projectList = projectDao.find(sqlPara);
		return projectList;
	}

	public List<Share> getShareList() {
		/*
		SqlPara sqlPara = shareDao.getSqlPara("index.getShareList", Share.REPORT_BLOCK_NUM);
		List<Share> shareList = shareDao.findByCache(indexCacheName, "shareList", sqlPara.getSql(), sqlPara.getPara());
		return shareList;
		*/
		SqlPara sqlPara = shareDao.getSqlPara("index.getShareList", Share.REPORT_BLOCK_NUM);
		List<Share> shareProject = shareDao.find(sqlPara);
		return shareProject;
	}

	public List<Feedback> getFeedbackList() {
		/*
		SqlPara sqlPara = feedbackDao.getSqlPara("index.getFeedbackList", Feedback.REPORT_BLOCK_NUM);
		List<Feedback> feedbackList = feedbackDao.findByCache(indexCacheName, "feedbackList", sqlPara.getSql(), sqlPara.getPara());
		return feedbackList;
		*/
		SqlPara sqlPara = feedbackDao.getSqlPara("index.getFeedbackList", Feedback.REPORT_BLOCK_NUM);
		List<Feedback> feedbackList = feedbackDao.find(sqlPara);
		return feedbackList;
	}

	public void clearCache() {
		CacheKit.removeAll(indexCacheName);
	}
}
