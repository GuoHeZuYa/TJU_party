package edu.twt.party.service.course;

import edu.twt.party.pojo.course.*;

import java.util.List;

/**
 * @author xsr
 * "20课"
 */
public interface CourseServise {
    /**
     * 在最后添加新的课程
     *
     * @param courseName 课程名称
     * @param courseDetail 课程描述
     * @return Boolean
     */
    Boolean addCourse(String courseName, String courseDetail);

    /**
     * 在 position 后的位置添加课程
     *
     * @param courseName 课程名称
     * @param courseDetail 课程描述
     * @param position 要添加在后面的位置
     * @return Boolean
     */
    Boolean addCourseAfter(String courseName, String courseDetail, int position);

    /**
     * 修改课程信息（不包含课程文本信息）
     *
     * @param courseId 课程id
     * @param newCourseName 修改后的课程名称
     * @param newCourseDetail 修改后的课程描述
     * @return Boolean
     */
    Boolean alterCourseInfo(int courseId, String newCourseName, String newCourseDetail);

    /**
     * 删除课程
     *
     * @param courseId 课程id
     * @return Boolean
     */
    Boolean deleteCourse(int courseId);

    /**
     * 获取课程列表
     *
     * @return list
     */
    List<Course> getCourse();

    /**
     * 为课程添加文章
     *
     * @param courseId 课程id
     * @param title 课程标题
     * @param text 文章正文
     * @return  Boolean
     */
    Boolean addCoursePassage(int courseId, String title, String text);

    /**
     * 修改课程文章标题
     *
     * @param passageId 课程文章 id
     * @param newTitle 新的标题
     * @return Boolean
     */
    Boolean alterCoursePassageTitle(int passageId, String newTitle);

    /**
     * 修改课程文章正文
     *
     * @param passageId 课程文章 id
     * @param newText 正文
     * @return Boolean
     */
    Boolean alterCoursePassageText(int passageId, String newText);

    /**
     * 删除课程的文章
     *
     * @param passageIdList 要删除的id列表
     * @return Boolean
     */
    Boolean deleteCoursePassage(List<Integer> passageIdList);

    /**
     * 获取某一课程的所有文章
     *
     * @param courseId 要查询的课程id
     * @return List <CoursePassage>
     */
    List<CoursePassage> getCoursePassageList(int courseId);

    /**
     * 获取某一课程的所有文章的Id和标题
     *
     * @param courseId 要查询的课程id
     * @return List <CoursePassage>
     */

    List<CoursePassage> getCoursePassageIds(int courseId);
    /**
     * 获取某一课程的所有文章的Id和标题
     *
     * @param id 要查询的文章id
     * @return CoursePassage
     */
    CoursePassage getCoursePassage(int id);

    /**
     * 添加课程习题
     *
     * @param exercise 习题的实体类
     * @return Boolean
     */
    Boolean addCourseExercise(CourseExerciseUnit exercise);

    /**
     * 修改课程习题
     *
     * @param newExercise 修改后的习题内容
     * @return Boolean
     */
    Boolean alterCourseExercise(CourseExerciseUnit newExercise);

    /**
     * 删除课程习题
     *
     * @param exerciseIdList 要删除的课程习题 id 列表
     * @return Boolean
     */
    Boolean deleteCourseExercise(List<Integer> exerciseIdList);

    /**
     * 获取课程对应的习题列表
     *
     * @param courseId 课程id
     * @return list
     */
    List<CourseExerciseUnit> getCourseExercise(int courseId);
    List<CourseExerciseUnit> getUserCourseExercise(int userId,int courseId);
    CourseExerciseResult submitUserCourseExercise(int userId, int courseId,List<Integer> answer);
    Boolean saveUserCourseExercise(int userId, int courseId,List<Integer> answer);
    List<CourseUser> getUserCourse(int userId,int startTimes);
    Boolean isPassAllCourse(int userId);

    /**
     * 上传答案
     *
     * @param userId 用户id
     * @param courseId 课程id
     * @param answer 答案
     * @return Boolean
     */
    Boolean uploadExamAnswer(int userId, int courseId, String answer);

    Boolean sortCourses(int courseId, int to);
}
