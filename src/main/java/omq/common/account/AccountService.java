package omq.common.account;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;

import omq.common.model.Account;

public class AccountService {
	
	public static final AccountService me = new AccountService();
	private final Account dao = new Account().dao();
	private final String allAccountsCacheName = "allAccounts";
	
	public void joinNickNameAndAvatar(List<? extends Model> modelList) {
		join("accountId", modelList, "nickName", "avatar");
	}
	
	public void join(String joinOnField, List<? extends Model> modelList, String... joinAttrs) {
		if (modelList != null) {
			for (Model m : modelList) {
				join(joinOnField, m, joinAttrs);
			}
		}
	}
	
	public void join(String joinOnField, Model model, String... joinAttrs) {
		Integer accountId = model.getInt(joinOnField);
		if (accountId == null) {
			throw new RuntimeException("Model中的 \"" + joinOnField  + "\" 属性不能为 null");
		}

        Account account = getById(accountId);

		if (account != null) {
			for (String attr : joinAttrs) {
				model.put(attr, account.get(attr));
			}
		} else {
			throw new RuntimeException("未找到 account 或者 account 状态不合法，account 的 id 值为：" + accountId + " 可能是数据库数据不一致");
		}
	}
	
	public Account getById(int accountId) {
        // 优先从缓存中取，未命中缓存则从数据库取
        Account account = CacheKit.get(allAccountsCacheName, accountId);
        if (account == null) {
            // 考虑到可能需要 join 状态不合法的用户，先放开 status 的判断
            // account = dao.findFirst("select * from account where id=? and status=? limit 1", accountId, Account.STATUS_OK);
            account = dao.findFirst("select * from account where id=? limit 1", accountId);
            if (account != null) {
                account.removeSensitiveInfo();
                CacheKit.put(allAccountsCacheName, accountId, account);
            }
        }
        return account;
    }

}
