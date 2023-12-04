package edu.twt.party.service.process.impl;

import edu.twt.party.dao.ProcessMapper;
import edu.twt.party.pojo.userProcess.MainProcessFilterResult;
import edu.twt.party.pojo.userProcess.ProcessNode;
import edu.twt.party.pojo.userProcess.UserProcess;
import edu.twt.party.service.process.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    ProcessMapper processMapper;

    public Boolean isMemberInPartyBranch(List<Integer> userIds,int partyBranchId){
        return processMapper.isMemberInPartyBranchByCount(userIds,partyBranchId).equals(userIds.size());
    }
    @Override
    public Boolean updateUserProcess(Integer userId, Integer processId, Integer status){
        boolean ret = isValid(userId)&&isValid(processId)&&isValid(status);
        if(ret){
            processMapper.deleteAllProcessById(userId,processId);
            processMapper.addNewProcess(userId,processId,status);
        }else {
            LOGGER.info("updateUserProcess with wrong param");
        }
        return ret;
    }

    @Override
    public List<UserProcess> getUserProcessesById(Integer userId) {
        return processMapper.getUserProcessById(userId);
    }

    @Override
    public List<ProcessNode> getProcessTree() {
        return processMapper.getProcessTree();
    }

    @Override
    public List<Integer> getUsersIdByCollegeIdAndGrade(Integer collegeId, Integer grade, Integer type) {
        return processMapper.getUserIdsByCollegeIdAndGrade(collegeId,grade,type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Boolean updateUsersProcess(List<Integer> userIds, List<Integer> processIds, List<Integer> status) {
        if(processIds.size()!=status.size())return false;
        boolean ret = true;
        for (int i = 0; i < processIds.size(); i++) {
            processMapper.deleteOldUserProcessBatch(userIds,processIds.get(i));
            processMapper.updateUserProcessBatch(userIds, processIds.get(i),status.get(i));
        }
        return true;
    }
    public boolean isValid(Integer param){
        return param != null&&param >= 0;
    }

    @Override
    public List<MainProcessFilterResult> filterMainProcess(int partyBranchId, int mainProcess) {
        int[] mainProcessMap = {5,10,15,20};//TODO:确定主状态完成对应的节点ID
        return processMapper.filterMainProcess(partyBranchId,mainProcessMap[mainProcess]);
    }
}
