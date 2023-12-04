package edu.twt.party.service.message;

import edu.twt.party.pojo.message.AdminMessage;
import edu.twt.party.pojo.message.MessageUnit;

import java.util.List;

public interface AdminMessageService {
    /**
     * 管理员发送信息(非回复，单独发送)
     *
     * @param sno     发送对象的学号
     * @param uid     user id
     * @param messageUnit 信件内容等
     * @return boolean
     */
    Boolean sendAdminMessage(String sno, int uid, MessageUnit messageUnit);

    /**
     * 管理员批量发送信息
     *
     * @param receiver    接收信息的对象，0为发送给所有用户，正数为要发送的学院代码
     * @param uid         user id
     * @param messageUnit 信件内容
     * @return Boolean
     */
    int sendAdminMessage(int receiver, int uid, MessageUnit messageUnit);

    /**
     * 查看管理员已发送的信件
     *
     * @param uid user id
     * @return List
     */
    List<AdminMessage> getSentAdminMessage(int uid,String sortBy,Boolean desc,Integer status,Integer startNum);
    Integer getSentAdminMsgCount(int uid,Integer status);
    /**
     * 管理员发送信息(回复)
     *
     * @param replyMessageInfoId 回复的信件的id
     * @param uid                user id
     * @param messageUnit        信件内容等
     * @return boolean
     */
    Boolean sengAdminReply(int replyMessageInfoId, int uid, MessageUnit messageUnit);

    /**
     * 获取收到的所有（管理员发送的）站内信
     *
     * @param uid user id
     * @return List
     */
    List<AdminMessage> getAdminMessage(int uid);

    /**
     * 将普通用户收到的信件设置为已读
     *
     * @param messageInfoId 信件id
     * @return boolean
     */
    Boolean setAdminMessageReaded(int messageInfoId);
}
