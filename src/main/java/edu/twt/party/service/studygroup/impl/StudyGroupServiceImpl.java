package edu.twt.party.service.studygroup.impl;

import edu.twt.party.dao.studygroup.StudyGroupMapper;
import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.pojo.studygroup.StudyGroup;
import edu.twt.party.service.studygroup.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudyGroupServiceImpl implements StudyGroupService {
    @Autowired
    StudyGroupMapper studyGroupMapper;
    @Override
    public List<StudyGroup> getStudyGroupByBranchId(int branchId){
        return studyGroupMapper.getStudyGroupsByBranchId(branchId);
    }

    @Override
    public StudyGroup getStudyGroupById(int id) {
        return studyGroupMapper.getStudyGroupById(id);

    }

    @Override
    public Boolean updateStudyGroupById(int id, String name) {
        return studyGroupMapper.updateStudyGroupById(name,id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public Boolean deleteStudyGroupById(int id) {
        studyGroupMapper.deleteStudyGroupMemberBySGID(id);
        return studyGroupMapper.deleteStudyGroup(id);
    }

    @Override
    public List<TwtStudentInfo> getStudyGroupMember(int studyGroupId) {
        return studyGroupMapper.getMemberListBySGId(studyGroupId);
    }

    @Override
    public List<TwtStudentInfo> getStudyGroupMemberUnAlloc(int branchId) {
        return studyGroupMapper.getMemberListUnAllocByBranchId(branchId);
    }

    @Override
    public Integer createStudyGroup(String name, int branchId) {
        return studyGroupMapper.newStudyGroup(branchId,name);
    }
    private Boolean isMemberInPartyBranch(List<Integer> userIds,int partyBranchId){
        return studyGroupMapper.isMemberInPartyBranchByCount(userIds,partyBranchId).equals(userIds.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Boolean moveStudyGroupMember(List<Integer> userIds, int studyGroupId) {
        StudyGroup studyGroup = getStudyGroupById(studyGroupId);
        if(studyGroup!=null){
            int branchId = studyGroup.getPartyBranchId();
            //保证所有成员和支部对应
            if(isMemberInPartyBranch(userIds,branchId)){
                studyGroupMapper.deleteStudyGroupMemberBatch(userIds);
                return studyGroupMapper.createStudyGroupMemberBatch(userIds,studyGroupId);

            }
        }
        return false;
    }
}
