package omq.common.account;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
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
        Account account = CacheKit.get(allAccountsCacheName, accountId);
        if (account == null) {
            account = dao.findFirst("select * from account where id=? limit 1", accountId);
            if (account != null) {
                account.removeSensitiveInfo();
                CacheKit.put(allAccountsCacheName, accountId, account);
            }
        }
        return account;
    }
	
	private void updateLikeCount(int accountId, boolean isAdd) {
        String sql = isAdd ?
                "update account set likeCount=likeCount+1 where id=? limit 1" :
                "update account set likeCount=likeCount-1 where id=? and likeCount > 0 limit 1";
        int n = Db.update(sql, accountId);
        if (n > 0) {
            Account account = CacheKit.get(allAccountsCacheName, accountId);
            if (account != null) {
                account.setLikeCount(account.getLikeCount() + (isAdd ? 1 : -1));
            }
        }
    }
	
	public Account getUsefulById(int accountId) {
        Account account = getById(accountId);
        return account.isStatusOk() ? account : null;
	}
	
	public void addLikeCount(int accountId) {
        updateLikeCount(accountId, true);
    }

    public void minusLikeCount(int accountId) {
        updateLikeCount(accountId, false);
    }

}
