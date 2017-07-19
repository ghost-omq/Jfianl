package omq.my.friend;

import com.jfinal.plugin.activerecord.Page;

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
}
