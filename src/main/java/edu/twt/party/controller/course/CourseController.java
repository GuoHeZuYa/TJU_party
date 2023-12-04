package edu.twt.party.controller.course;

import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.course.Course;
import edu.twt.party.pojo.course.CourseExerciseUnit;
import edu.twt.party.pojo.course.CoursePassage;
import edu.twt.party.service.course.CourseServise;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Tag(name = "20课管理")
public class CourseController {
    @Resource
    CourseServise courseServise;

    @PostMapping("/api/course")
    @Operation(summary = "在最后添加课程")
    public ResponseHelper<Boolean> addCourse(@RequestParam("courseName") @Parameter(description = "课程名称") String courseName,
                                             @RequestParam("courseDetail") @Parameter(description = "课程描述") String courseDetail) {
        return new ResponseHelper<>(courseServise.addCourse(courseName, courseDetail));
    }

    @PostMapping("/api/course/position")
    @Operation(summary = "在特定位置后添加课程")
    public ResponseHelper<Boolean> addCourseAfter(@RequestParam("courseName") @Parameter(description = "课程名称") String courseName,
                                                  @RequestParam("courseDetail") @Parameter(description = "课程描述") String courseDetail,
                                                  @RequestParam("position") @Parameter(description = "添加在position后面的位置") int position){
        return new ResponseHelper<>(courseServise.addCourseAfter(courseName, courseDetail, position));
    }

    @GetMapping("/api/course")
    @Operation(summary = "获取课程列表")
    public ResponseHelper<List<Course>> getCourse() {
        return new ResponseHelper<>(courseServise.getCourse());
    }

    @PostMapping("/api/course/alter")
    @Operation(summary = "修改课程信息（不包括课程文本内容）")
    public ResponseHelper<Boolean> alterCourseInfo(@RequestParam("courseId") @Parameter(description = "要修改的课程id") int courseId,
                                                   @RequestParam("newCourseName") @Parameter(description = "修改后的课程名称") String newCourseName,
                                                   @RequestParam("newCourseDetail") @Parameter(description = "修改后的课程描述") String newCourseDetail) {
        return new ResponseHelper<>(courseServise.alterCourseInfo(courseId, newCourseName, newCourseDetail));
    }

    @PostMapping("/api/course/delete")
    @Operation(summary = "删除课程以及课程的文章与习题")
    public ResponseHelper<Boolean> deleteCourse(@RequestParam("courseId") @Parameter(description = "要删除的课程id") int courseId) {
        return new ResponseHelper<>(courseServise.deleteCourse(courseId));
    }

    @PostMapping("/api/course/exercise")
    @Operation(summary = "添加课程习题")
    public ResponseHelper<Boolean> addCourseExercise(@RequestBody @Parameter(description = "习题信息") CourseExerciseUnit exercise) {
        return new ResponseHelper<>(courseServise.addCourseExercise(exercise));
    }

    @GetMapping("/api/course/exercise")
    @Operation(summary = "获取课程习题")
    public ResponseHelper<List<CourseExerciseUnit>> getCourseExercise(@RequestParam("courseId") @Parameter(description = "课程id") int courseId) {
        return new ResponseHelper<>(courseServise.getCourseExercise(courseId));
    }

    @PostMapping("/api/course/exercise/alter")
    @Operation(summary = "修改课程习题")
    public ResponseHelper<Boolean> alterCourseExercise(@RequestBody @Parameter(description = "修改后的习题信息") CourseExerciseUnit newExercise) {
        return new ResponseHelper<>(courseServise.alterCourseExercise(newExercise));
    }

    @PostMapping("/api/course/exercise/delete")
    @Operation(summary = "删除课程习题")
    public ResponseHelper<Boolean> deleteCourseExercise(@RequestBody @Parameter(description = "要删除的习题id列表") List<Integer> exerciseIdList) {
        return new ResponseHelper<>(courseServise.deleteCourseExercise(exerciseIdList));
    }

    @PostMapping("/api/course/passage")
    @Operation(summary = "添加课程文章")
    public ResponseHelper<Boolean> addCoursePassage(@RequestParam("courseId") @Parameter(description = "添加的文章所属课程id") int courseId,
                                                    @RequestParam("title") @Parameter(description = "文章标题") String title,
                                                    @RequestBody @Parameter(description = "文章正文") String text) {
        return new ResponseHelper<>(courseServise.addCoursePassage(courseId, title, text));
    }

    @PostMapping("/api/course/passage/title/alter")
    @Operation(summary = "修改课程文章标题")
    public ResponseHelper<Boolean> alterCoursePassageTiTle(@RequestParam("passageId") @Parameter(description = "要修改的课程文章id") int passageId,
                                                           @RequestParam("newTitle") @Parameter(description = "新的标题") String newTitle) {
        return new ResponseHelper<>(courseServise.alterCoursePassageTitle(passageId, newTitle));
    }

    @PostMapping("/api/course/passage/text/alter")
    @Operation(summary = "修改课程文章正文")
    public ResponseHelper<Boolean> alterCoursePassageText(@RequestParam("passageId") @Parameter(description = "要修改的课程文章id") int passageId,
                                                           @RequestParam("newText") @Parameter(description = "新的正文") String newText) {
        return new ResponseHelper<>(courseServise.alterCoursePassageText(passageId, newText));
    }

    @PostMapping("/api/course/passage/delete")
    @Operation(summary = "删除课程文章")
    public ResponseHelper<Boolean> deleteCoursePassage(@RequestBody @Parameter(description = "要删除的文章id列表") List<Integer> passageIdList) {
        return new ResponseHelper<>(courseServise.deleteCoursePassage(passageIdList));
    }

    @GetMapping("/api/course/passage")
    @Operation(summary = "获取课程文章列表")
    public ResponseHelper<List<CoursePassage>> getCoursePassage(@RequestParam("courseId") @Parameter(description = "课程id") int courseId) {
        return new ResponseHelper<>(courseServise.getCoursePassageList(courseId));
    }
    @PostMapping("/api/course/{id}/position")
    @Operation(summary = "调整课程顺序")
    public ResponseHelper<Boolean> sortCourse(@PathVariable int id,
                                              @RequestParam int position) {
        return new ResponseHelper<>(courseServise.sortCourses(id,position));
    }
}
