package omq.my.like;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class LikeMessageLogService {
	
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
}
