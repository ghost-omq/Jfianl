/**
 * 请勿将俱乐部专享资源复制给其他人，保护知识产权即是保护我们所在的行业，进而保护我们自己的利益
 * 即便是公司的同事，也请尊重 JFinal 作者的努力与付出，不要复制给同事
 * 
 * 如果你尚未加入俱乐部，请立即删除该项目，或者现在加入俱乐部：http://jfinal.com/club
 * 
 * 俱乐部将提供 jfinal-club 项目文档与设计资源、专用 QQ 群，以及作者在俱乐部定期的分享与答疑，
 * 价值远比仅仅拥有 jfinal club 项目源代码要大得多
 * 
 * JFinal 俱乐部是五年以来首次寻求外部资源的尝试，以便于有资源创建更加
 * 高品质的产品与服务，为大家带来更大的价值，所以请大家多多支持，不要将
 * 首次的尝试扼杀在了摇篮之中
 */

package omq.feedback;

import com.jfinal.aop.Before;

import omq.common.contrller.BaseController;
import omq.common.model.Feedback;
import omq.common.pageView.PageViewInterceptor;

@Before(FeedbackSeo.class)
public class FeedbackController extends BaseController {

	static FeedbackService srv = FeedbackService.me;

	public void index() {
		setAttr("feedbackPage", srv.paginate(getParaToInt("p", 1)));
		//setAttr("hotFeedback", srv.getHotFeedback());
		render("index.html");
	}

	@Before(PageViewInterceptor.class)
	public void detail() {
		Feedback feedback = srv.findById(getParaToInt());
		setAttr("feedback", feedback);
		//setAttr("hotFeedback", srv.getHotFeedback());
		render("detail.html");

	}
}
