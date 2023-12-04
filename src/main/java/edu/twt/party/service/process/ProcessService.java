package edu.twt.party.service.process;

import edu.twt.party.pojo.userProcess.MainProcessFilterResult;
import edu.twt.party.pojo.userProcess.ProcessNode;
import edu.twt.party.pojo.userProcess.UserProcess;
import edu.twt.party.service.BaseService;

import java.util.List;

public interface ProcessService extends BaseService {
    //更新单个学生的入党进度
    Boolean updateUserProcess(Integer userId,Integer processId,Integer status);
    //获取单个学生的入党进度
    List<UserProcess> getUserProcessesById(Integer userId);
    //批量更改用户状态
    Boolean updateUsersProcess(List<Integer> userIds, List<Integer> processIds, List<Integer> status);
    //获取入党流程树（暂时无用
    List<ProcessNode> getProcessTree();
    List<Integer> getUsersIdByCollegeIdAndGrade(Integer collegeId,Integer grade, Integer type);
    List<MainProcessFilterResult> filterMainProcess(int partyBranchId,int mainProcess);
    Boolean isMemberInPartyBranch(List<Integer> userIds,int partyBranchId);
}
