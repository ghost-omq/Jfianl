package omq.my.friend;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import omq.common.model.Account;

public class FriendService {

	public static final FriendService me = new FriendService();
	final Account accountDao = new Account().dao();
	final int pageSize = 15;
	
	public Page<Account> getFollowPage(int accountId, int pageNum) {
        String select = "select t.*, f2.friendId as isMyFans";
		StringBuilder from = new StringBuilder();
		from.append("from (");
		from.append("    select f.friendId, a.id, a.nickName, a.avatar from friend f inner join account a ");
		from.append("    on f.friendId = a.id where f.accountId = ? order by f.createAt desc");
		from.append(") as t left join friend f2 on t.friendId = f2.accountId and f2.friendId = ?");
		return accountDao.paginate(pageNum, pageSize, select, from.toString(), accountId, accountId);
	}
	
	public Page<Account> getFansPage(int accountId, int pageNum) {
		String select = "select t.*, f2.accountId as isMyFriend";
        StringBuilder from = new StringBuilder();
        from.append(" from ( ");
		from.append("    select f.accountId, a.id, a.nickName, a.avatar from friend f inner join account a ");
		from.append("    on f.accountId = a.id where f.friendId = ? order by f.createAt desc");
		from.append(") as t left join friend f2 on t.accountId = f2.friendId and f2.accountId = ?");
		return accountDao.paginate(pageNum, pageSize, select, from.toString(), accountId, accountId);
	}
	
	public int getFriendRelation(int accountId, int friendId) {
		if (accountId == friendId) {
			return -1;                  // accountId 与 friendId 相同
		}

		List<Record> list = Db.find(
				"select accountId, friendId from friend where accountId= ? and friendId= ? union all " +
				"select accountId, friendId from friend where accountId= ? and friendId= ?",
				accountId, friendId, friendId, accountId);
		if (list.size() == 0) {
			return 0;                   // 两个账号无任何关系
		}
		if (list.size() == 1) {
			if (list.get(0).getInt("accountId") == accountId) {
				return 1;               // accountId 关注了 friendId
			} else {
				return 2;               // friendId 关注了 accountId
			}
		}
		if (list.size() == 2) {
			return 3;                   // accountId 与 friendId 互相关注
		}
		throw new RuntimeException("不可能存在的第五种关系，正常情况下该异常永远不可能抛出");
	}
	
	public int[] getFollowAndFansCount(int accountId) {
        String sql = "select count(*) from friend f1 where accountId = ? union all " +
                     "select count(*) from friend f2 where friendId = ? ";
        List<Long> list = Db.query(sql, accountId, accountId);
        return new int[]{list.get(0).intValue(), list.get(1).intValue()};
    }
}
