package edu.twt.party.service.message.impl;

import edu.twt.party.dao.message.MessageMapper;
import edu.twt.party.exception.APIException;
import edu.twt.party.pojo.message.Message;
import edu.twt.party.pojo.message.MessageUnit;
import edu.twt.party.pojo.message.StuMessage;
import edu.twt.party.service.message.StuMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 站内信
 *
 * @author xsr
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class StuMessageServiceImpl implements StuMessageService {

    @Resource
    MessageMapper messageMapper;

    /**
     * 普通用户发送信息
     *
     * @param receiverId  发送对象
     * @param uid         user id
     * @param messageUnit 信件内容等
     * @return boolean
     */
    @Override
    public Boolean sendStuMessage(int receiverId, int uid, MessageUnit messageUnit) {
        try {
            Message message = messageUnit.convertToMessage();
            messageMapper.addMessage(message);
            if(receiverId==1){//院级管理
                Integer academyId = messageMapper.getAcademyIdByUid(uid);
                if(academyId!=null){
                    return messageMapper.addStuMessage(uid, academyId*1000+120, 0, 0, message.getMessageId()) > 0;
                }else {
                    return false;
                }
            }else if(receiverId==2){//校级管理
                return messageMapper.addStuMessage(uid, 127, 0, 0, message.getMessageId()) > 0;
            }else if(receiverId==0){//支书
                return false;//TODO
            }else {
                return false;
            }

        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("发送普通用户站内信错误");
        }
    }

    /**
     * 获取普通用户已发送的站内信
     *
     * @param uid user id
     * @return List
     */
    @Override
    public List<StuMessage> getSentStuMessage(int uid) {
        try {
            return messageMapper.getSentStuMessage(uid);
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("获取普通用户站内信错误");
        }
    }

    /**
     * 获取管理员收到的所有站内信
     *
     * @param uid user id
     * @return List
     */
    @Override
    public List<StuMessage> getStuMessage(int uid,String sortBy,Boolean desc,Integer status,Integer startNum) throws APIException {
        try {
            int role = messageMapper.getRoleById(uid);

            if(sortBy!=null && (sortBy.equals("send_time")||sortBy.equals("status"))){
                return messageMapper.getReceivedStuMessage(role,sortBy,desc,status,startNum);
            }else {
                return messageMapper.getReceivedStuMessage(role,null,false,status,startNum);
            }
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("获取管理员收到的站内信错误");
        }
    }

    @Override
    public Integer getStuMessageCount(int uid, Integer status) {
        int role = messageMapper.getRoleById(uid);
        return messageMapper.getReceivedStuMsgCount(role,status);
    }

    /**
     * 将管理员收到的信件设置为已读
     *
     * @param messageInfoId 信件id
     * @return boolean
     */
    @Override
    public Boolean setStuMessageReaded(int messageInfoId) throws APIException {
        try {
            int a = messageMapper.setStuMessageReaded(messageInfoId);
            return a == 1;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("发送错误");
        }
    }
}
