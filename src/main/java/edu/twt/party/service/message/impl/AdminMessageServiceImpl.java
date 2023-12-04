package edu.twt.party.service.message.impl;

import edu.twt.party.dao.IdMapper;
import edu.twt.party.dao.message.MessageMapper;
import edu.twt.party.exception.APIException;
import edu.twt.party.pojo.message.*;
import edu.twt.party.service.message.AdminMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员站内信
 *
 * @author xsr
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class AdminMessageServiceImpl implements AdminMessageService {

    @Resource
    MessageMapper messageMapper;

    @Resource
    IdMapper idMapper;

    /**
     * 管理员发送信息（非回复）
     *
     * @param sno         发送对象的学号
     * @param uid         user id
     * @param messageUnit 信件内容等
     * @return boolean
     */
    @Override
    public Boolean sendAdminMessage(String sno, int uid, MessageUnit messageUnit) {
        try {
            int receiverId = idMapper.getUserIdBySno(sno);
            Message message = messageUnit.convertToMessage();
            messageMapper.addMessage(message);
            int a = messageMapper.addAdminMessage(uid, receiverId, 0, message.getMessageId(), -1);
            return a == 1;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("发送管理员信件错误");
        }
    }

    /**
     * 管理员批量发送信息
     *
     * @param receiver    接收信息的对象，0为发送给所有用户，正数为要发送的学院代码
     * @param uid         user id
     * @param messageUnit 信件内容
     * @return Boolean
     */
    @Override
    public int sendAdminMessage(int receiver, int uid, MessageUnit messageUnit) {
        try {
            Message message = messageUnit.convertToMessage();
            messageMapper.addMessage(message);
            int messageId = message.getMessageId();
            List<Integer> receiverIdList = new ArrayList<>();
            if (receiver == 0) {
                receiverIdList.add(0);
            } else if (receiver > 0) {
                receiverIdList.add(-1000-receiver);
            } else if (receiver == -1) {
                receiverIdList.add(-1);
            }else if (receiver == -2) {
                receiverIdList.add(-2);
            }else if (receiver == -3) {
                receiverIdList.add(-3);
            }else {
                throw new APIException("receiver格式错误");
            }
            if (receiverIdList == null) {
                return 0;
            } else {
                //List<AdminMessageInfoBasic> entities = new ArrayList<>();
                int num = 0;
                for (Integer i : receiverIdList) {
                    //entities.add(new AdminMessageInfoBasic(messageId, uid, i, -1));
                    num += messageMapper.addAdminMessage(uid, i, 0, messageId, -1);
                }
                return num;
            }
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("管理员批量发送站内信错误");
        }
    }

    /**
     * 查看管理员已发送的信件
     *
     * @param uid user id
     * @return List
     */
    @Override
    public List<AdminMessage> getSentAdminMessage(int uid, String sortBy, Boolean desc,Integer status,Integer startNum) {
        try {
            if(sortBy!=null&&(sortBy.equals("send_time")||sortBy.equals("status"))){
                return messageMapper.getSentAdminMessage(uid,sortBy,desc,status,startNum);
            }else {
                return messageMapper.getSentAdminMessage(uid,null,false,status,startNum);
            }
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("获取管理员站内信错误");
        }
    }

    @Override
    public Integer getSentAdminMsgCount(int uid, Integer status) {
        return messageMapper.getSentAdminMsgCount(uid,status);
    }

    /**
     * 管理员发送信息(回复)
     *
     * @param replyMessageInfoId 所回复的信件的id
     * @param messageUnit        信件内容等
     * @param uid                user id
     * @return boolean
     */
    @Override
    public Boolean sengAdminReply(int replyMessageInfoId, int uid, MessageUnit messageUnit) {
        try {
            int receiverId = messageMapper.getSenderIdByMessageInfoId(replyMessageInfoId);
            Message message = messageUnit.convertToMessage();
            messageMapper.addMessage(message);
            int a = messageMapper.addAdminMessage(uid, receiverId, 0, message.getMessageId(), replyMessageInfoId);
            int b = messageMapper.setStuMessageHandler(uid, replyMessageInfoId);
            return a == 1 && b == 1;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("发送错误");
        }
    }

    /**
     * 获取普通用户收到的所有（管理员发送的）站内信
     *
     * @param uid user id
     * @return List
     */
    @Override
    public List<AdminMessage> getAdminMessage(int uid) {
        try {
            ArrayList<Integer> typeList = new ArrayList<>();
            Integer academyId = messageMapper.getAcademyIdByUid(uid);
            Integer userType = messageMapper.getUserType(uid);
            ArrayList<Integer> importIds = messageMapper.getImportMessageIdByUid(uid);
            for (Integer id :
                    importIds) {
                typeList.add(-2000-id);
            }
            if(userType!=null && userType >0){
                if((userType&0b100) == 0b100)typeList.add(-1);//支书
                if((userType&0b010) == 0b010)typeList.add(-2);//组织
                if((userType&0b001) == 0b001)typeList.add(-3);//宣传
            }
            if(academyId!=null){
                typeList.add(-1000-academyId);//学院
            }
            typeList.add(uid);//自己
            typeList.add(0);//全体

            return messageMapper.getReceivedAdminMessageByType(typeList);
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("获取普通用户收到的站内信错误");
        }
    }

    /**
     * 将普通用户收到的信件设置为已读
     *
     * @param messageInfoId 信件id
     * @return boolean
     */
    @Override
    public Boolean setAdminMessageReaded(int messageInfoId) {
        try {
            int a = messageMapper.setAdminMessageReaded(messageInfoId);
            return a == 1;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("设置信件已读错误");
        }
    }
}
