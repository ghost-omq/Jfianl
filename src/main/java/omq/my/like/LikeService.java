package omq.my.like;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import omq.common.account.AccountService;

public class LikeService {
	
	public static final LikeService me = new LikeService();
	final String REF_TYPE_PROJECT = "project";
    final String REF_TYPE_SHARE = "share";
    final String REF_TYPE_FEEDBACK = "feedback";
	
	private final Set<String> permissionTables = new HashSet<String>(){{
        add(REF_TYPE_PROJECT);
        add(REF_TYPE_SHARE);
        add(REF_TYPE_FEEDBACK);
    }};
	
	private void check(String refType) {
        if ( !permissionTables.contains(refType) ) {
            throw new IllegalArgumentException("refType 不正确");
        }
    }
	 
	private Ret like(int myId, String refType, int refId) {
        if (isLike(myId, refType, refId)) {
            return delete(myId, refType, refId);
        } else {
            return save(myId, refType, refId);
        }
    }
	
	public Ret like(int myId, String refType, int refId, Boolean isAdd){
		check(refType);
        if (isAdd != null) {
            if (isAdd) {
                return save(myId, refType, refId);
            } else {
                return delete(myId, refType, refId);
            }
        } else {
            return like(myId, refType, refId);
        }
	}
	
	private Integer getUserIdOfRef(String refType, int refId) {
        return Db.queryInt("select accountId from " + refType + " where id=? limit 1", refId);
    }
	
	private Ret save(final int myId, final String refType, final int refId) {
        final Integer userId = getUserIdOfRef(refType, refId);
        if (userId == null) {
            return Ret.fail("msg", "未找到资源，可能已经被删除");
        }
        if (myId == userId) {
            return Ret.fail("msg", "不能给自己点赞");
        }
        if (isLike(myId, refType, refId)) {
            return Ret.fail("msg", "已经点赞，请刷新页面");
        }
        boolean isOk = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                int n = Db.update("insert into " + refType + "_like(accountId, refId, createAt) value(?, ?, now())", myId, refId);
                if (n > 0) {
                    n = Db.update("update " + refType + " set likeCount=likeCount+1 where id=? limit 1", refId);
                    if (n > 0) {
                        AccountService.me.addLikeCount(userId);
                    }
                }

                return n > 0;
            }
        });
        if (isOk) {
            LikeMessageLogService.me.sendSystemMessage(myId, userId, refType, refId);
        }
        return isOk ? Ret.ok() : Ret.fail("msg", "点赞失败");
    }

    private Ret delete(final int myId, final String refType, final int refId) {
        boolean isOk = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                int n = Db.update("delete from " + refType + "_like where accountId=? and refId=? limit 1", myId, refId);
                if (n > 0) {
                    n = Db.update("update " + refType + " set likeCount=likeCount-1 where id=? and likeCount>0 limit 1", refId);
                    Integer userId = getUserIdOfRef(refType, refId);
                    if (userId != null) {
                        AccountService.me.minusLikeCount(userId);
                    }
                }
                return n > 0;
            }
        });
        return isOk ? Ret.ok() : Ret.fail("msg", "取消点赞失败");
    }
    
    public boolean isLike(int accountId, String refType, int refId) {
        String sql = "select accountId from " + refType + "_like where accountId=? and refId=? limit 1";
        return Db.queryInt(sql, accountId, refId) != null;
    }

}
