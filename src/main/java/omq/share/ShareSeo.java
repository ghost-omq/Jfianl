package omq.share;

import com.jfinal.core.Controller;

import omq.common.interceptor.BaseSeoInterceptor;
import omq.common.model.Share;

public class ShareSeo extends BaseSeoInterceptor{

	@Override
	public void indexSeo(Controller c) {
		setSeoTitle(c, "JFinal 极速开发分享");
		setSeoKeywords(c, "JFinal 分享, JFinal Weixin 分享, JFinal demo 分享, JFinal 微信分享, JFinal 案列分享, JFinal 插件分享, JFinal 教程分享");
		setSeoDescr(c, "JFinal 极速开发分享集合, JFinal 学习资源分享, JFinal 教程分享, JFinal 案例分享, JFinal 实战分享");
	}

	@Override
	public void detailSeo(Controller c) {
		Share share = c.getAttr("share");
		setSeoTitle(c, share.getTitle());
		setSeoKeywords(c, share.getTitle());
		setSeoDescr(c, share.getTitle());
	}

	@Override
	public void othersSeo(Controller c, String method) {

	}
}
