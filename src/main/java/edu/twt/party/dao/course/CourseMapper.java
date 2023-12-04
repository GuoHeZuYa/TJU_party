package edu.twt.party.dao.course;

import edu.twt.party.pojo.course.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseMapper {
    @Insert("insert into `course` (`course_name`, `course_detail`,  `position`) " +
            "values(#{courseName}, #{courseDetail},  #{position})")
    int addCourse(@Param("courseName") String courseName, @Param("courseDetail") String courseDetail,
                  @Param("position") int position);

    @Insert("insert into `course_exercise` (`course_id`, `exercise_type`, `question`, `value`, `exercise_answer`, " +
            "`option_a`, `option_b`, `option_c`, `option_d`, `option_e`) " +
            "values(#{courseId}, #{exerciseType}, #{question}, #{value}, #{exerciseAnswer}, #{optionA}, #{optionB}, #{optionC}, #{optionD}, #{optionE})")
    int addCourseExercise(@Param("courseId") int courseId, @Param("exerciseType") int exerciseType, @Param("question") String question, @Param("value") int value,
                          @Param("exerciseAnswer") int exerciseAnswer, @Param("optionA") String optionA, @Param("optionB") String optionB,
                          @Param("optionC") String optionC, @Param("optionD") String optionD, @Param("optionE") String optionE);

    @Insert("insert into `course_passage` (`course_id`, `text`, `title`) " +
            "values(#{courseId}, #{text}, #{title})")
    int addCoursePassage(@Param("courseId") int courseId, @Param("text") String text, @Param("title") String title);

    @Update("update `course` set is_deleted = 1 where course_id = #{courseId}")
    int deleteCourse(@Param("courseId") int courseId);

    @Update("update `course` set course_name = #{courseName}, course_detail = #{courseDetail} " +
            "where course_id = #{courseId}")
    int alterCourseInfo(@Param("courseId") int courseId,
                        @Param("courseName") String courseName, @Param("courseDetail") String courseDetail);

    @Update("update `course` set position = #{position} where course_id = #{courseId}")
    int alterCoursePosition(@Param("position") int position, @Param("courseId") int courseId);

    @Update("update `course_exercise` set is_deleted = 1 where exercise_id = #{exerciseId}")
    int deleteCourseExercise(@Param("exerciseId") int exerciseId);

    @Update("update `course_exercise` set is_deleted = 1 where course_id = #{courseId}")
    int deleteCourseExerciseByCourseId(@Param("courseId") int courseId);

    @Update("update `course_exercise` set exercise_type = #{exerciseType}, value = #{value}, exercise_answer = #{exerciseAnswer}, " +
            "option_a = #{optionA}, option_b = #{optionB}, option_c = #{optionC}, option_d = #{optionD}, option_e = #{optionE} " +
            "where exercise_id = #{exerciseId}")
    int alterCourseExercise(@Param("exerciseType") int exerciseType, @Param("value") int value, @Param("exerciseAnswer") int exerciseAnswer,
                            @Param("optionA") String optionA, @Param("optionB") String optionB, @Param("optionC") String optionC,
                            @Param("optionD") String optionD, @Param("optionE") String optionE, @Param("exerciseId") int exerciseId);

    @Update("update `course_passage` set is_deleted = 1 where passage_id = #{passageId}")
    int deleteCoursePassage(@Param("passageId") int passageId);

    @Update("update `course_passage` set is_deleted = 1 where course_id = #{courseId}")
    int deleteCoursePassageByCourseId(@Param("courseId") int courseId);

    @Update("update `course_passage` set title = #{newTitle} where passage_id = #{passageId}")
    int alterCoursePassageTitle(@Param("passageId") int passageId, @Param("newTitle") String newTitle);

    @Update("update `course_passage` set text = #{newText} where passage_id = #{passageId}")
    int alterCoursePassageText(@Param("passageId") int passageId, @Param("newText") String newText);

    @Select("select * from `course` where is_deleted = 0 order by position")
    List<Course> getCourse();

    @Select("select * from `course` where is_deleted = 0 and course_id = #{courseId}")
    Course getCourseById(@Param("courseId") int courseId);

    @Select("select `exercise_id`, `course_id`, `exercise_type`, `question`, `value`,`exercise_answer`, `option_a`, " +
            "`option_b`, `option_c`, `option_d`, `option_e` from `course_exercise` where course_id = #{courseId} and is_deleted = 0")
    List<CourseExercise> getCourseExercise(@Param("courseId") int courseId);

    @Select("select * from `course_passage` where course_id = #{courseId} and is_deleted = 0")
    List<CoursePassage> getCoursePassage(@Param("courseId") int courseId);

    @Select("select * from `course_passage` where passage_id = #{id} and is_deleted = 0")
    CoursePassage getSinglePassage(int id);
    @Select("select passage_id, title from `course_passage` where course_id = #{courseId} and is_deleted = 0")
    List<CoursePassage> getCoursePassageIds(@Param("courseId") int courseId);
    @Insert("insert into course_paper (user_id, course_id, answer,paper_content) values (#{userId},#{courseId},#{answer},#{paperContent})")
    Boolean addPaper(int userId,int courseId,String answer,String paperContent);
    @Select("select * from course_paper where user_id = #{userId} and course_id = #{courseId} and status != 3 and status != 1 order by id desc limit 1")
    CoursePaper getPaperAns(int userId, int courseId);
    @Update("update course_paper set status = #{status} where user_id = #{userId} and course_id = #{courseId} and status != 1 and status != 3")
    Boolean updatePaperStatus(int userId, int courseId,int status);
    @Select("select * from course_paper where user_id = #{userId} and course_id = #{courseId} and status = 2 order by id desc limit 1")
    CoursePaper getPaperAnsSaved(int userId, int courseId);
    @Update("update course_paper set tmp_saved_ans = #{savedAns}, status = 2 where user_id = #{userId} and course_id = #{courseId} and status != 1 and status != 3")
    Boolean savePaperAns(int userId,int courseId,String savedAns);
    @Insert("insert into course_result (user_id, course_id, paper_id, score, is_pass, times, status) values (#{userId},#{courseId},#{paperId},#{score},#{isPass},(select max(times) from exam_list where user_type = 0 and deleted = 0),#{status})")
    Boolean addExerciseResult(int userId,int courseId,int paperId,int score,boolean isPass,int status);
    @Select("select course.course_id as course_id, course_name as course_name, course_detail as course_detail, position as position, ifnull(is_pass,0) as is_pass, max(times) as last_pass_time from course " +
            "left join (select * from course_result where is_deleted = 0 and status = 1 and is_pass = 1 and user_id= #{userId} and times >= #{startTimes}) result on result.course_id = course.course_id where course.is_deleted = 0 group by course_id")
    List<CourseUser> getUserCourse(int userId,int startTimes);
//    @Select("select course.course_id as course_id, course_name as course_name, course_detail as course_detail, position as position, ifnull(is_pass,0) as is_pass, times as last_pass_time from course " +
//            "left join (select * from course_result where is_deleted = 0 and status = 1 and is_pass = 1 and user_id= #{userId} and times >= (select max(times)-1 from exam_list where user_type = 0 and deleted = 0) order by times) result on result.course_id = course.course_id where course.is_deleted = 0")
//    List<CourseUser> getUserCourse(int userId);
    @Select("select max(times) from exam_list where user_type = 0 and deleted = 0")
    Integer getLastApplicantTimes();
    @Update("update course set position = position + 1 where position >= #{to} and position <= #{from} and is_deleted = 0")
    Boolean posMoveBefore(int from, int to);
    @Update("update course set position = position - 1 where position >= #{from} and position <= #{to} and is_deleted = 0")
    Boolean posMoveAfter(int from, int to);

}