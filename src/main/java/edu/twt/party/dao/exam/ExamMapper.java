package edu.twt.party.dao.exam;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.twt.party.pojo.exam.*;
import edu.twt.party.pojo.excelParse.applicant.ApplicantExcelImport;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component("ExamMapper")
public interface ExamMapper extends BaseMapper<Exam> {
    //考试管理
    @Select("select * from exam_list where deleted = 0")
    List<Exam> getExams();
    @Select("select * from exam_list where id = #{id} and deleted = 0")
    Exam getExamById(int id);
    @Insert("insert into exam_list (name,times, start_time, end_time, user_type, content) values (#{name}, #{times}, #{start}, #{end}, #{userType}, #{content})")
    boolean addExam(Date start, Date end,int times, String name, int userType, String content);
    @Update("update exam_list set deleted = 1, delete_at = NOW() where id = #{id}")
    Boolean deleteExamById(int id);
    @Update("update exam_list set start_time = #{start}, end_time = #{end}, name = #{name}, user_type = #{userType},content = #{content},times = #{times} where id = #{id}")
    boolean updateExamById(int id,Date start, Date end,int times, String name, int userType, String content);
    @Update("update exam_list set status = #{status} where id = #{id}")
    boolean updateExamStatusById(int id,int status);
    //考生名单
    @Select("select exam_signup.*,b_userinfo.username as name,twt_student_info.academy_id,twt_student_info.sno,b_college.collegename as academy_name\n" +
            "from exam_signup, twt_student_info,b_college,b_userinfo\n" +
            "where exam_signup.exam_id = #{id} and exam_signup.user_id = twt_student_info.id and b_userinfo.usernumb = twt_student_info.sno and b_college.id = twt_student_info.academy_id and exam_signup.is_cancel = 0 and deleted = 0")
    List<ExamSignup> getExamUserById(int id);
    //考试报名
    @Insert("insert into exam_signup (exam_id, user_id) values(#{examId}, #{userId})")
    boolean signupExam(int userId,int examId);
    @Select("select count(*) > 0 from exam_signup where is_cancel = 0 and user_id = #{userId} and exam_id = #{examId} and deleted = 0")
    boolean checkSignup(int userId, int examId);
    @Update("update exam_signup set is_cancel = 1 where exam_id = #{examId} and user_id = #{userId}")
    boolean cancelExam(int examId, int userId);
    @Select("select es.id as id, es.exam_id as exam_id, es.user_id as user_id, ifnull(score,0) as score,es.create_at as create_at, ifnull(status,0) as status,detail_score,comments from " +
            "(select * from exam_signup where user_id = #{userId} and is_cancel = 0 and deleted = 0) es left join (select * from exam_result where user_id = #{userId}) er on er.user_id = es.user_id and es.exam_id = er.exam_id")
    List<ExamResult> getUserExams(int userId);
    //考试成绩
    @Select("select * from exam_result where exam_id = #{examId} and user_id = #{userId}")
    ExamResult getExamResultById(int examId, int userId);
    //考试成绩
    @Select("select exam_result.*, twt_student_info.sno, b_userinfo.username as name,b_userinfo.majorname as major from exam_result,twt_student_info,b_userinfo where exam_id = #{examId} and exam_result.user_id = twt_student_info.id and twt_student_info.sno = b_userinfo.usernumb")
    List<ExamResultVo> getExamResultsById(int examId);

    @Select("select exam_result.*, twt_student_info.sno, b_userinfo.username as name,b_userinfo.majorname as major from exam_result,twt_student_info,b_userinfo where exam_id = #{examId} and exam_result.user_id = twt_student_info.id and twt_student_info.sno = b_userinfo.usernumb and status = 1")
    List<ExamResultVo> getExamResultsByIdPass(int examId);

    @Select("select exam_result.*, twt_student_info.sno, b_userinfo.username as name,b_userinfo.majorname as major from exam_result,twt_student_info,b_userinfo where exam_id = #{examId} and exam_result.user_id = twt_student_info.id and twt_student_info.sno = b_userinfo.usernumb and status != 1")
    List<ExamResultVo> getExamResultsByIdNotPass(int examId);
    @Select("select exam_result.*, twt_student_info.sno, b_userinfo.username as name,b_userinfo.majorname as major from exam_result,twt_student_info,b_userinfo where exam_id = #{examId} and exam_result.user_id = twt_student_info.id and twt_student_info.sno = b_userinfo.usernumb and  b_userinfo.collegeid = #{academicId}")
    List<ExamResultVo> getExamResultsByIdAndAcademic(int examId,int academicId);
    @Select("select exam_result.*, twt_student_info.sno, b_userinfo.username as name,b_userinfo.majorname as major from exam_result,twt_student_info,b_userinfo where exam_id = #{examId} and exam_result.user_id = twt_student_info.id and twt_student_info.sno = b_userinfo.usernumb and  b_userinfo.collegeid = #{academicId} and status = 1")
    List<ExamResultVo> getExamResultsByIdAndAcademicPass(int examId,int academicId);
    @Select("select exam_result.*, twt_student_info.sno, b_userinfo.username as name,b_userinfo.majorname as major from exam_result,twt_student_info,b_userinfo where exam_id = #{examId} and exam_result.user_id = twt_student_info.id and twt_student_info.sno = b_userinfo.usernumb and  b_userinfo.collegeid = #{academicId} and status != 1")
    List<ExamResultVo> getExamResultsByIdAndAcademicNotPass(int examId,int academicId);
    @Insert("<script>" +
            "insert into exam_result (user_id, exam_id, score, status, detail_score, comments) values " +
            "<foreach collection = 'examResultList' item='item' separator=','>" +
            "(#{item.userId}, #{item.examId}, #{item.score}, #{item.status}, #{item.detailScore}, #{item.comments})" +
            "</foreach></script>")
    boolean addExamResultById(List<ExamResult> examResultList);
    @Insert("<script>" +
            "insert into exam_result (id, exam_id, user_id, score, detail_score, status, create_at, comments) VALUES " +
            "<foreach collection = 'examResultList' item = 'item' separator=','>" +
            "(default,#{item.examId},(select id from twt_student_info where sno = #{item.sno}),#{item.score},#{item.detailScore},#{item.status},default, #{item.comments})" +
            "</foreach></script>")
    boolean addExamResultBySno(List<ExamResult> examResultList);
    //积极分子培训
    @Select("select cat.id as id, b_college.id as college_id,b_college.collegename as collegeName,ifnull(cat.exam_id,0) as examId,ifnull(cat.status,0) as status,cat.is_deleted,cat.delete_at,cat.create_at from b_college\n" +
            "    left join (select * from college_activist_train where exam_id = #{examId} and is_deleted = 0) as cat\n" +
            "        on cat.college_id = b_college.id\n" +
            "where state = 'ok';")
    List<CollegeActivistTrain> getCollegesActivistStatus(Integer examId);
    @Select("select * from college_activist_train where exam_id = #{examId} and college_id = #{collegeId} and is_deleted = 0")
    CollegeActivistTrain getCollegeActivistStatus(Integer examId,Integer collegeId);
    @Insert("insert into college_activist_train (exam_id, college_id, status) values (#{examId}, #{collegeId}, #{status})")
    boolean updateCollegeActivistStatus(Integer examId,Integer collegeId,Integer status);
    @Update("update college_activist_train set is_deleted = 1 where exam_id = #{examId} and college_id = #{collegeId} and is_deleted = 0")
    boolean deleteCollegeActivistStatus(Integer examId,Integer collegeId);
    @Select("select b_college.id,b_college.collegename, count(if(status=1,1,null)) as pass_num,count(if(status=2,1,null)) as absent_num,count(if(status>=3,1,null)) as cheat_num, count(*) as all_num from (select * from exam_result where exam_id = #{examId}) as exam_result\n" +
            "    left join twt_student_info on exam_result.user_id = twt_student_info.id\n" +
            "    left join b_college on academy_id = b_college.id\n" +
            "    group by b_college.collegename,b_college.id;\n")
    List<ExamResultAnalyseEntity> getExamResultAnalyse(int examId);
    //当前是否处于考试惩罚
    @Select("select exam_punish.id, exam_punish.user_id, exam_punish.exam_id, exam_punish.punish_type, exam_punish.punish_times+exam_list.times as punish_times from exam_punish, exam_list where user_id = #{userId} and exam_punish.exam_id = exam_list.id and exam_list.user_type = #{examType};")
    List<ExamPunish> getUserExamPunishes(int userId,int examType);
    //新增惩罚
    @Insert("insert into exam_punish (user_id, exam_id, punish_type, punish_times) values (#{userId},#{examId},#{punishType},#{punishTimes});")
    boolean newUserExamPunish(int userId, int examId,int punishType,int punishTimes);
    //删除惩罚
    @Update("update exam_punish set is_delete = 1,delete_at = NOW() where id = #{punishId}")
    boolean deleteUserExamPunish(int punishId);
    @Select("select es.* from (select exam_list.* from exam_signup,exam_list where user_id = #{userId} and is_cancel = 0 and exam_signup.exam_id = exam_list.id) es left join " +
            "(select * from exam_result where user_id = #{userId}) er on er.exam_id = es.id where er.id is null")
    List<Exam> getExamsSignedUpButNoResult(int userId);
}
