package edu.twt.party.service.message;

import edu.twt.party.pojo.message.MessageUnit;
import edu.twt.party.pojo.message.StuMessage;

import java.util.List;

public interface StuMessageService {
    /**
     * 普通用户发送信息
     *
     * @param receiverId 发送对象
     * @param uid        user id
     * @param message    信件内容等
     * @return boolean
     */
    Boolean sendStuMessage(int receiverId, int uid, MessageUnit message);
    /**
     * 获取普通用户已发送的站内信
     *
     * @param uid user id
     * @return List
     */
    List<StuMessage> getSentStuMessage(int uid);

    /**
     * 获取收到的所有站内信
     *
     * @param uid user id
     * @param sortBy 排序
     * @param desc 是否倒序
     * @return List
     */
    List<StuMessage> getStuMessage(int uid,String sortBy,Boolean desc,Integer status,Integer startNum);
    Integer getStuMessageCount(int uid,Integer status);

    /**
     * 将管理员收到的信件设置为已读
     *
     * @param messageInfoId 信件id
     * @return boolean
     */
    Boolean setStuMessageReaded(int messageInfoId);
}
