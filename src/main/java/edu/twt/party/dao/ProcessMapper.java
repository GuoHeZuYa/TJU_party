package edu.twt.party.dao;

import edu.twt.party.pojo.userProcess.MainProcessFilterResult;
import edu.twt.party.pojo.userProcess.ProcessNode;
import edu.twt.party.pojo.userProcess.UserProcess;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessMapper {
    @Update("update user_process set is_deleted = 1, delete_at = NOW() where user_id = #{userId} and process_id = #{processId} and is_deleted = 0")
    boolean deleteAllProcessById(int userId, int processId);
    @Insert("insert into user_process (user_id, process_id, status, create_at, delete_at, is_deleted) VALUES (#{userId}, #{processId}, #{status}, DEFAULT, DEFAULT, DEFAULT)")
    boolean addNewProcess(int userId,int processId,int status);
    @Select("select * from user_process where user_id = #{userId} and is_deleted = 0")
    List<UserProcess> getUserProcessById(int userId);
    @Select("select * from process_tree where is_deleted = 0")
    List<ProcessNode> getProcessTree();
    @Select("select twt_student_info.id from twt_student_info, b_userinfo where b_userinfo.collegeid = #{collegeId} and b_userinfo.grade = #{grade} and b_userinfo.type = #{type} and twt_student_info.sno = b_userinfo.usernumb")
    List<Integer> getUserIdsByCollegeIdAndGrade(Integer collegeId, Integer grade, Integer type);
    @Insert("<script>" +
            "insert into user_process (user_id, process_id, status, create_at, delete_at, is_deleted) VALUES " +
            "<foreach collection = 'userIds' item = 'item' separator=','>" +
            "(#{item}, #{processId}, #{status}, DEFAULT, DEFAULT, DEFAULT)" +
            "</foreach></script>")

    boolean updateUserProcessBatch(List<Integer>userIds, int processId, int status);
    //TODO:IN，数据多了可能会慢？
    @Update("<script>" +
            "update user_process set is_deleted = 1, delete_at = NOW() where process_id = #{processId} and is_deleted = 0 and user_id in (" +
            "<foreach collection = 'userIds' item = 'item' separator=','>" +
            "#{item}" +
            "</foreach>)</script>")
    boolean deleteOldUserProcessBatch(List<Integer>userIds, int processId);
    @Select("select up.user_id, twt_student_info.sno,b_userinfo.username as userName,0 as main_process from (select * from user_process where process_id = #{landmarkProcessId} and status = 3) up left join twt_student_info on up.user_id = twt_student_info.id left join b_userinfo on b_userinfo.usernumb = twt_student_info.sno where partybranch_id = #{partyBranchId}")
    List<MainProcessFilterResult> filterMainProcess(int partyBranchId,int landmarkProcessId);
    @Select("<script>select count(*) from twt_student_info where partybranch_id = #{partyBranchId} and id in (" +
            "<foreach collection = 'userIds' item = 'item' separator=','>" +
            "#{item}" +
            "</foreach>)</script>")
    Integer isMemberInPartyBranchByCount(List<Integer>userIds,int partyBranchId);
    @Select("select ifnull(status,0) from user_process where is_deleted = 0 and user_id = #{userId} and process_id = #{nodeId} order by id desc limit 1")
    Integer getUserProcessStatus(int userId,int nodeId);

}
