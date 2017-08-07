package omq.my.like;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import omq.common.account.AccountService;
import omq.common.model.Account;
import omq.my.message.MessageService;

public class LikeMessageLogService {
	
	static final LikeMessageLogService me = new LikeMessageLogService();
	
	public static final int REF_TYPE_PROJECT = 1;
    public static final int REF_TYPE_SHARE = 3;
    public static final int REF_TYPE_FEEDBACK = 5;
	
	private static Map<String, Integer> map = new HashMap<String, Integer>(){{
        put("project", REF_TYPE_PROJECT);
        put("share", REF_TYPE_SHARE);
        put("feedback", REF_TYPE_FEEDBACK);
    }};

	private void doSendSystemMessage(int myId, int userId, String tableName, int refId) {
        String sql = "select accountId from like_message_log where accountId=? and refType=? and refId=?";
        Integer refType = getRefTypeValue(tableName);
        if (Db.queryInt(sql, myId, refType, refId) == null) {
            Record r = new Record()
                    .set("accountId", myId)
                    .set("refType", refType)
                    .set("refId", refId)
                    .set("createAt", new Date());
            Db.save("like_message_log", r);

            saveSystemMessage(myId, userId, tableName, refType, refId);
        }
    }
	
	public void sendSystemMessage(int myId, int userId, String refType, int refId) {
        try {
            doSendSystemMessage(myId, userId, refType, refId);
        } catch (Exception e) {
            LogKit.error(e.getMessage(), e);
        }
    }

	
	private Integer getRefTypeValue(String tableName) {
        Integer refType = map.get(tableName);
        if (refType == null) {
            throw new IllegalArgumentException("tableName 不正确");
        }
        return refType;
    }
	
	private void saveSystemMessage(int myId, int userId, String tableName, int refType, int refId) {
        Record ref = Db.findFirst("select id, title, likeCount from " + tableName + " where id=? limit 1", refId);
        if (ref == null) {
            return ;
        }

        Account my = AccountService.me.getById(myId);
        String msg = "@" + my.getNickName() + " 刚刚赞了你的";
        if (refType == REF_TYPE_PROJECT) {
            msg = msg + "项目：<a href='/project/" + ref.getInt("id") +"' target='_blank'>" + ref.getStr("title");
        } else if (refType == REF_TYPE_SHARE) {
            msg = msg + "分享：<a href='/share/" + ref.getInt("id") +"' target='_blank'>" + ref.getStr("title");
        } else if (refType == REF_TYPE_FEEDBACK) {
            msg = msg + "反馈：<a href='/feedback/" + ref.getInt("id") +"' target='_blank'>" + ref.getStr("title");
        } else {
            throw new RuntimeException("refType 不正确，请告知管理员");
        }
        msg = msg + "</a>，目前被赞次数为：" + ref.getInt("likeCount");
        MessageService.me.sendSystemMessage(myId, userId, msg);
    }
	
}
