package omq.my.message;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import omq.common.account.AccountService;
import omq.common.model.Message;

public class MessageService {
	
	public static final MessageService me = new MessageService();
    final Message dao = new Message().dao();
    final int pageSize = 15;
    
    public Page<Message> paginate(int pageNum, int accountId) {
        String select = "select m.*, t.msgCount ";
        String from = "from (select max(id) as maxId, count(id) as msgCount from message where user=? group by friend " +
                                    ") as t inner join message m where t.maxId=m.id order by m.id desc";
        Page<Message> messagePage = dao.paginate(pageNum, pageSize, select, from, accountId);
        AccountService.me.join("friend", messagePage.getList(), "nickName", "avatar");
        return messagePage;
    }
    
    public Ret sendSystemMessage(int sender, int receiver, String content) {
        return send(sender, receiver, Message.TYPE_SYSTEM, content);
    }

}
