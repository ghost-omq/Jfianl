package omq.my.message;

import java.sql.SQLException;
import java.util.Date;

import com.jfinal.kit.LogKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

import omq.common.account.AccountService;
import omq.common.model.Message;
import omq.my.newsfeed.ReferMeKit;
import omq.my.newsfeed.RemindService;

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
    
    private Ret send(final int sender, final int receiver, final int type, final String content) {
        if (sender == receiver) {
            return Ret.fail("msg", "不能给自己发送私信");
        }
        if (type < Message.TYPE_NORMAL || type > Message.TYPE_SYSTEM) {
            throw new IllegalArgumentException("type 值不正确");
        }
        final Ret ret = Ret.create();
        final Message m1 = new Message();
        boolean isOk = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                m1.setUser(receiver);
                m1.setFriend(sender);
                m1.setSender(sender);
                m1.setReceiver(receiver);
                m1.setType(type);
                m1.setContent(content);
                m1.setCreateAt(new Date());
                ReferMeKit.buildAtMeLink(m1);   // 转换 @提到我

                // 系统消息，只保留收信人的信息，不创建发件人的信息
                if (type == Message.TYPE_SYSTEM) {
                	ret.set("message", m1);
                    return m1.save();
                }

                // 如果是"非系统消息" 同时为发信人保存一条信息，例如：点赞功能发送的是系统消息，但发信人是点赞的人
                Message m2 = new Message();
                m2.setUser(sender);
                m2.setFriend(receiver);
                m2.setSender(sender);
                m2.setReceiver(receiver);
                m2.setType(type);
                m2.setContent(content);
                m2.setCreateAt(new Date());
                ReferMeKit.buildAtMeLink(m2);   // 转换 @提到我
                ret.set("message", m2);
                return m1.save() && m2.save();
            }
        });
        if (isOk) {
        	return ret.setOk();
        } else {
            String msg = "消息发送失败，请告知管理员";
            LogKit.error(msg);
            return Ret.fail("msg", msg);
        }
    }

}
