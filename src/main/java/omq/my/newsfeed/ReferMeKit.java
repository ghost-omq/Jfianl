package omq.my.newsfeed;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;

import omq.common.account.AccountService;
import omq.common.model.Account;

public class ReferMeKit {

	static final Pattern p = Pattern.compile("@([^@\\s:,;：，；　<&]{1,})([\\s:,;：，；　<&]{0,1})");

	public static String buildAtMeLink(String content, List<Integer> referAccounts) {
		if (StrKit.isBlank(content)) {
			return content;
		}

		StringBuilder ret = new StringBuilder();
		Matcher matcher = p.matcher(content);
		int pointer = 0;
		while (matcher.find()) {
			ret.append(content.substring(pointer, matcher.start()));
			String nickName = matcher.group(1);

			Account account = AccountService.me.getByNickName(nickName, "id");
			if (account != null) {
				ret.append("<a href=\"/user/").append(account.getId())
						.append("\" target=\"_blank\" class=\"at-me\">")
						.append("@").append(nickName).append("</a>");
				ret.append(matcher.group(2));

				if ( !referAccounts.contains(account.getId()) ) {
					referAccounts.add(account.getId());
				}
			} else {
				ret.append(matcher.group());
			}

			pointer = matcher.end();
		}
		ret.append(content.substring(pointer));
		return ret.toString();
	}

	/**
	 * 将 model 中的 attrName 属性内容创建 at me 链接
	 */
	public static List<Integer> buildAtMeLink(Model model, String attrName) {
		List<Integer> referAccounts = new ArrayList<Integer>();
		String content = model.getStr(attrName);
		if (StrKit.notBlank(content)) {
			content = buildAtMeLink(content, referAccounts);
			model.set(attrName, content);
		}
		return referAccounts;
	}

	/**
	 * 将 model 中的 content 属性内容创建 at me 链接
	 */
	public static List<Integer> buildAtMeLink(Model model) {
		return buildAtMeLink(model, "content");
	}
}
