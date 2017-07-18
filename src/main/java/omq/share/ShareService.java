package omq.share;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

import omq.common.model.Share;

public class ShareService {
	
	public static final ShareService me = new ShareService();
	private final Share dao = new Share().dao();

	
	public Page<Share> paginate(int pageNumber) {
		SqlPara sqlPara = dao.getSqlPara("share.paginate", Share.REPORT_BLOCK_NUM);
		Page<Share> sharePage = dao.paginate(pageNumber, 15, sqlPara);
		return sharePage;
	}
	
	public Share findById(int shareId) {
		SqlPara sqlPara = dao.getSqlPara("share.findById", shareId, Share.REPORT_BLOCK_NUM);
		return dao.findFirst(sqlPara);
	}
	
	public List<Share> getHotShare() {
		SqlPara sqlPara = dao.getSqlPara("share.getHotShare", Share.REPORT_BLOCK_NUM);
		return dao.find(sqlPara);
	}
}
