package edu.twt.party.service.process;

import edu.twt.party.pojo.userProcess.UserProcessNode;
import edu.twt.party.pojo.userProcess.UserProcessType;

public interface UserStateService {
    //获取用户某个入党流程节点的状态，0为未完成，1为通过，2为驳回
    Integer getUserProcessStatus(int userId, UserProcessNode userProcessNode);
    //获取用户类型
    UserProcessType getUserType(int userId);
}
