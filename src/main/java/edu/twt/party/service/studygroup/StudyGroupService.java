package edu.twt.party.service.studygroup;

import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.pojo.studygroup.StudyGroup;

import java.util.List;

public interface StudyGroupService {
    List<StudyGroup> getStudyGroupByBranchId(int branchId);
    StudyGroup getStudyGroupById(int id);
    Boolean updateStudyGroupById(int id,String name);
    Boolean deleteStudyGroupById(int id);
    List<TwtStudentInfo> getStudyGroupMember(int studyGroupId);
    List<TwtStudentInfo> getStudyGroupMemberUnAlloc(int branchId);
    Integer createStudyGroup(String name,int branchId);
    Boolean moveStudyGroupMember(List<Integer> userIds,int studyGroupId);
}
