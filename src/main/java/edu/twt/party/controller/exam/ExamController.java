package edu.twt.party.controller.exam;



import com.alibaba.fastjson2.JSONArray;
import edu.twt.party.annotation.JwtToken;
import edu.twt.party.constant.RoleType;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.exam.*;
import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.service.exam.ExamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@Tag(name = "线下考试")
public class ExamController {
    @Autowired
    ExamService examService;

    //@LoginToken
    @GetMapping("/api/exam/myexams")
    @JwtToken(roles = RoleType.COMMON_USER,onlyNeedId = true)
    @Operation(summary = "获取用户已报名的考试及成绩",description = "包括没成绩的")
    public ResponseHelper<List<ExamResult>> getUserExams(HttpServletRequest request){
        //int userId = ((User) request.getAttribute("iampUser")).getUid();
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int userId = user.getId();
        return new ResponseHelper<>(examService.getUserExams(userId));
    }
    //@LoginToken
    @GetMapping("/api/exam/{examId}/signup")
    @Operation(summary = "报名考试")
    @JwtToken(roles = RoleType.COMMON_USER,onlyNeedId = true)
    public ResponseHelper<Boolean> signup(@PathVariable("examId") int examId,HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int userId = user.getId();
        return new ResponseHelper<>(examService.signupExam(userId,examId));
    }

    @GetMapping("/api/exam/userList")
    @Operation(summary = "用户考试聚合")
    @JwtToken(roles = RoleType.COMMON_USER,onlyNeedId = true)
    public ResponseHelper<HashMap<String, ExamUserList>> getUserList(HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int userId = user.getId();
        return new ResponseHelper<>(examService.getUserList(userId));
    }



    @GetMapping("/api/exam/all")
    @Operation(summary = "考试列表")
    public ResponseHelper<List<Exam>> getAllExam(){
        return new ResponseHelper<>(examService.getExamList());
    }

    @GetMapping("/api/exam/{examId}/detail")
    @Operation(summary = "考试信息")
    public ResponseHelper<Exam> getExam(@PathVariable int examId){
        return new ResponseHelper<>(examService.getExamById(examId));
    }
    //@LoginToken
    @GetMapping("/api/exam/{examId}/result")
    @Operation(summary = "考试成绩")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<ExamResult> getExamResult(@PathVariable("examId") int examId,HttpServletRequest request){

        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int userId = user.getId();
        return new ResponseHelper<>(examService.getDetailResult(examId,userId));
    }
    //@LoginToken
    @GetMapping("/api/exam/{examId}/cancel")
    @Operation(summary = "取消报名")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<Boolean> cancelExam(@PathVariable("examId") int examId,HttpServletRequest request){
        //TODO: 取消报名限制
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int userId = user.getId();
        return new ResponseHelper<>(examService.cancelExam(examId,userId));
    }
    @PostMapping("/api/exam/new")
    @Operation(summary = "管理员添加考试")
    @JwtToken(roles = RoleType.ROOT)
    public ResponseHelper<Boolean> addExam(@RequestBody Exam exam){
        return new ResponseHelper<>(examService.addExam(exam.getStartTime(),exam.getEndTime(), exam.getTimes(), exam.getName(),exam.getUserType(),exam.getContent()));
    }

    @PostMapping("/api/exam/{id}/update")
    @Operation(summary = "管理员修改考试信息")
    @JwtToken(roles = RoleType.ROOT)
    public ResponseHelper<Boolean> updateExam(@PathVariable int id, @RequestBody Exam exam){
        return new ResponseHelper<>(examService.updateExam(id,exam.getStartTime(),exam.getEndTime(), exam.getTimes(), exam.getName(),exam.getUserType(),exam.getContent()));
    }
    @GetMapping("/api/exam/{id}/delete")
    @Operation(summary = "管理员删除考试")
    @JwtToken(roles = RoleType.ROOT)
    public ResponseHelper<Boolean> deleteExam(@PathVariable int id){
        return new ResponseHelper<>(examService.deleteExam(id));
    }

    @GetMapping("/api/exam/{id}/userList")
    @Operation(summary = "管理员获取考试人员名单")
    @JwtToken(roles = RoleType.ROOT)
    public ResponseHelper<List<ExamSignup>> getExamUserById(@PathVariable("id")Integer id){
        return new ResponseHelper<>(examService.getExamUserById(id));
    }
    @GetMapping("/api/exam/{id}/results/{academicId}")
    @Operation(summary = "管理员获取考试结果")
    @JwtToken(roles = RoleType.ROOT)
    public ResponseHelper<List<ExamResultVo>> getExamResults(@PathVariable("id")Integer examId,@PathVariable Integer academicId,@Nullable @RequestParam Integer pass){
        if(pass!=null){
            return new ResponseHelper<>(examService.getExamResultsById(examId,academicId,pass==1));
        }else {
            return new ResponseHelper<>(examService.getExamResultsById(examId,academicId));
        }
    }

    @PostMapping("/api/exam/result/batchById")
    @Operation(summary = "管理员根据用户id直接录入成绩（废弃）")
    @JwtToken(roles = RoleType.ROOT)
    public ResponseHelper<Boolean> addResultBatchById(@RequestBody List<ExamResult> examResults){
        return new ResponseHelper<>(examService.addResultBatchById(examResults));
    }
    @PostMapping("/api/exam/result/batchBySno")
    @Operation(summary = "管理员根据学号直接录入成绩（废弃）")
    @JwtToken(roles = RoleType.ROOT)
    public ResponseHelper<Boolean> addResultBatchBySno(@RequestBody List<ExamResult> examResults){
        return new ResponseHelper<>(examService.addResultBatchBySno(examResults));
    }
    @GetMapping("/api/exam/probationary/{examId}")
    @Operation(summary = "预备党员开设状态")
//    @JwtToken(roles = RoleType.COLLEGE_ADMIN)
    public ResponseHelper<boolean[]> getProbationaryStatus(@PathVariable(name = "examId") Integer examId){
        return new ResponseHelper<>(examService.getProbationaryStatus(examId));
    }
    @PostMapping("/api/exam/probationary/{examId}")
    @Operation(summary = "预备党员培训更改状态")
    @JwtToken(roles = RoleType.COLLEGE_ADMIN)
    public ResponseHelper<Boolean> updateProbationaryStatus(@PathVariable(name = "examId")Integer examId,
                                                               @RequestBody() JSONArray body){
        Boolean[] b = body.toArray(Boolean.class);
        return new ResponseHelper<>(examService.updateProbationaryStatus(examId,b));
    }
    @GetMapping("/api/exam/activist/list/{examId}")
    @Operation(summary = "积极分子各学院开设状态")
    @JwtToken(roles = RoleType.ROOT)
    public ResponseHelper<List<CollegeActivistTrain>> getCollegeActivistStatus(@PathVariable(name = "examId") Integer examId){
        return new ResponseHelper<>(examService.getCollegesActivistStatus(examId));
    }
    @GetMapping("/api/exam/activist/{examId}/{collegeId}")
    @Operation(summary = "积极分子单个学院状态")
    @JwtToken(roles = RoleType.COLLEGE_ADMIN)
    public ResponseHelper<CollegeActivistTrain> getCollegeActivistStatus(@PathVariable(name = "examId")Integer examId,
                                                                         @PathVariable(name = "collegeId")Integer collegeId){
        return new ResponseHelper<>(examService.getCollegeActivistStatus(examId,collegeId));
    }
    @PostMapping("/api/exam/activist/{examId}/{collegeId}")
    @Operation(summary = "积极分子更改状态")
    @JwtToken(roles = RoleType.COLLEGE_ADMIN)
    public ResponseHelper<Boolean> updateCollegeActivistStatus(@PathVariable(name = "examId")Integer examId,
                                                              @PathVariable(name = "collegeId")Integer collegeId,
                                                              @RequestParam(name = "status")Integer status){
        return new ResponseHelper<>(examService.updateCollegeActivistStatus(examId,collegeId,status));
    }
    @GetMapping("/api/exam/export/analyse/{examId}")
    @Operation(summary = "成绩分析导出")
    @JwtToken(roles = RoleType.ROOT)
    public void exportExamAnalyse(@PathVariable("examId") int examId, HttpServletResponse response){
        try {
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment;filename=%E5%90%84%E5%AD%A6%E9%99%A2%E8%80%83%E8%AF%95%E6%88%90%E7%BB%A9.xls");
            response.flushBuffer();
            examService.exportExamAnalyse(examId).write(response.getOutputStream());
        }catch (IOException e){

        }
    }
    @GetMapping("/api/exam/export/attendee/{examId}")
    @Operation(summary = "考试人员名单")
    @JwtToken(roles = RoleType.ROOT)
    public void exportExamAttendee(@PathVariable("examId")int examId, HttpServletResponse response){
        try {
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment;filename=%E8%80%83%E8%AF%95%E4%BA%BA%E5%91%98%E5%90%8D%E5%8D%95.xls");
            response.flushBuffer();
            examService.exportExamAttendee(examId).write(response.getOutputStream());
        }catch (IOException e){

        }
    }
}
