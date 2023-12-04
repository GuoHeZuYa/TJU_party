package edu.twt.party.dao.studygroup;

import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.pojo.studygroup.StudyGroup;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StudyGroupMapper {
    @Select("select * from study_groups where party_branch_id = #{branchId} and is_delete = 0")
    List<StudyGroup> getStudyGroupsByBranchId(int branchId);
    @Select("select * from study_groups where id = #{studyGroupId} and is_delete = 0")
    StudyGroup getStudyGroupById(int studyGroupId);
    @Update("update study_groups set name = #{name} where id = #{studyGroupId}")
    Boolean updateStudyGroupById(String name,int studyGroupId);
    @Update("update study_groups set is_delete = 1 where id = #{studyGroupId}")
    Boolean deleteStudyGroup(int studyGroupId);
    @Insert("insert into study_groups (party_branch_id, status, name) values (#{partyBranchId},1,#{name});")
    Integer newStudyGroup(int partBranchId,String name);
    @Select("select * from (select * from twt_student_info where partybranch_id = (select party_branch_id from study_groups where study_groups.id = #{studyGroupId})) as tsi where id in (select user_id from study_group_member where study_group_id = #{studyGroupId} and is_delete = 0)")
    List<TwtStudentInfo> getMemberListBySGId(int studyGroupId);
    @Select("select * from (select * from twt_student_info where partybranch_id = #{partyBranchId}) as tsi where id not in(select user_id from study_group_member where study_group_id in (select id from study_groups where party_branch_id = #{partyBranchId} and is_delete = 0) and is_delete = 0);\n")
    List<TwtStudentInfo> getMemberListUnAllocByBranchId(int partyBranchId);
    @Update("<script>update study_group_member set is_delete = 1 where user_id in (" +
            "<foreach collection = 'userIds' item = 'item' separator=','>" +
            "#{item}" +
            "</foreach>)</script>")
    Boolean deleteStudyGroupMemberBatch(List<Integer> userIds);
    @Insert("<script>insert into study_group_member (user_id, study_group_id) values " +
            "<foreach collection = 'userIds' item = 'item' separator=','>" +
            "(#{item}, #{studyGroupId})" +
            "</foreach></script>")
    Boolean createStudyGroupMemberBatch(List<Integer> userIds,int studyGroupId);
    @Select("<script>select count(*) from twt_student_info where partybranch_id = #{partyBranchId} and id in (" +
            "<foreach collection = 'userIds' item = 'item' separator=','>" +
            "#{item}" +
            "</foreach>)</script>")
    Integer isMemberInPartyBranchByCount(List<Integer>userIds,int partyBranchId);
    @Update("update study_group_member set is_delete = 1 where study_group_id = #{studyGroupId}")
    Boolean deleteStudyGroupMemberBySGID(int studyGroupId);
}
