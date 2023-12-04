package edu.twt.party.service.process.impl;

import edu.twt.party.dao.ProcessMapper;
import edu.twt.party.pojo.userProcess.UserProcessNode;
import edu.twt.party.pojo.userProcess.UserProcessType;
import edu.twt.party.service.process.UserStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStateServiceImpl implements UserStateService {
    @Autowired
    ProcessMapper processMapper;
    @Override
    public Integer getUserProcessStatus(int userId, UserProcessNode userProcessNode){
        Integer result = processMapper.getUserProcessStatus(userId,userProcessNode.getId());
        if(result!=null){
            return result;
        }else {
            return 0;
        }
    }

    @Override
    public UserProcessType getUserType(int userId) {
        if(processMapper.getUserProcessStatus(userId,UserProcessNode.PROBATIONARY_COMMUNICATION.getId())==1){
            return UserProcessType.PARTY_MEMBER;
        } else if(processMapper.getUserProcessStatus(userId,UserProcessNode.DEVELOP_RECORD.getId())==1){
            return UserProcessType.PROBATIONARY;
        } else if(processMapper.getUserProcessStatus(userId,UserProcessNode.ACTIVIST_EXAMINATION.getId())==1){
            return UserProcessType.DEVELOP;
        } else if(processMapper.getUserProcessStatus(userId,UserProcessNode.APPLICANT_EXAMINATION.getId())==1){
            return UserProcessType.ACTIVIST;
        } else if(processMapper.getUserProcessStatus(userId,UserProcessNode.APPLICANT_APPLICATION.getId())==1){
            return UserProcessType.APPLICANT;
        } else {
            return UserProcessType.NONE;
        }
    }
}
