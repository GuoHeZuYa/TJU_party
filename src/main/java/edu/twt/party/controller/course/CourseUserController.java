package edu.twt.party.controller.course;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import edu.twt.party.annotation.JwtToken;
import edu.twt.party.constant.RoleType;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.Role;
import edu.twt.party.pojo.course.*;
import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.service.course.CourseServise;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Tag(name = "20课管理用户端")
public class CourseUserController {
    @Resource
    CourseServise courseServise;

    @GetMapping("/api/userCourse")
    @Operation(summary = "获取课程列表")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<List<CourseUser>> getCourse(HttpServletRequest request) {
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        return new ResponseHelper<>(courseServise.getUserCourse(user.getId(), -1));
    }
    @GetMapping("/api/userCourse/exercise/{courseId}")
    @JwtToken(roles = RoleType.COMMON_USER)
    @Operation(summary = "获取课程习题")
    public ResponseHelper<List<CourseExerciseUnit>> getCourseExercise(@PathVariable("courseId") @Parameter(description = "课程id") int courseId,
                                                                      HttpServletRequest request) {
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        return new ResponseHelper<>(courseServise.getUserCourseExercise(user.getId(),courseId));
    }
    @GetMapping("/api/userCourse/passage")
    @Operation(summary = "获取课程文章列表")
    public ResponseHelper<List<CoursePassage>> getCoursePassageList(@RequestParam("courseId") @Parameter(description = "课程id") int courseId) {
        return new ResponseHelper<>(courseServise.getCoursePassageIds(courseId));
    }
    @GetMapping("/api/userCourse/passage/{id}")
    @Operation(summary = "获取课程文章")
    public ResponseHelper<CoursePassage> getCoursePassage(@PathVariable("id") @Parameter(description = "文章id") int id) {
        return new ResponseHelper<>(courseServise.getCoursePassage(id));
    }
    @PostMapping("/api/userCourse/exercise/{courseId}")
    @Operation(summary = "提交练习")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<CourseExerciseResult> submitExercise(@PathVariable Integer courseId,
                                                               @RequestBody JSONArray answers,
                                                               HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        return new ResponseHelper<>(courseServise.submitUserCourseExercise(user.getId(),courseId, answers.toList(Integer.class)));

    }
    @PostMapping("/api/userCourse/exercise/{courseId}/save")
    @Operation(summary = "提交练习")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<Boolean> saveExercise(@PathVariable Integer courseId,
                                                               @RequestBody JSONArray answers,
                                                               HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        return new ResponseHelper<>(courseServise.saveUserCourseExercise(user.getId(),courseId, answers.toList(Integer.class)));

    }

}
