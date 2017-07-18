package omq.feedback;

import com.jfinal.core.Controller;

import omq.common.interceptor.BaseSeoInterceptor;
import omq.common.model.Feedback;

public class FeedbackSeo extends BaseSeoInterceptor {

	@Override
	public void indexSeo(Controller c) {
		setSeoTitle(c, "JFinal 极速开发反馈");
		setSeoKeywords(c, "JFinal 反馈, JFinal Weixin 反馈, JFinal demo 反馈, JFinal 微信反馈, JFinal 案列反馈, JFinal 插件反馈, JFinal 教程反馈");
		setSeoDescr(c, "JFinal 极速开发反馈集合, JFinal 学习资源反馈, JFinal 教程反馈, JFinal 案例反馈, JFinal 实战反馈");
	}

	@Override
	public void detailSeo(Controller c) {
		Feedback feedback = c.getAttr("feedback");
		setSeoTitle(c, feedback.getTitle());
		setSeoKeywords(c, feedback.getTitle());
		setSeoDescr(c, feedback.getTitle());
	}

	@Override
	public void othersSeo(Controller c, String method) {

	}
}
