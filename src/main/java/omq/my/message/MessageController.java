package omq.my.message;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.plugin.activerecord.Page;

import omq.common.controller.BaseController;
import omq.common.model.Message;
import omq.my.like.LikeInterceptor;

@Before({LikeInterceptor.class})
public class MessageController extends BaseController{
	
	MessageService srv = MessageService.me;

	 @ActionKey("/my/message")
		public void message() {
	        Page<Message> messagePage = srv.paginate(getParaToInt("p", 1), getLoginAccountId());
	        setAttr("messagePage", messagePage);
	        render("index.html");
		}
}
