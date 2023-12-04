package edu.twt.party.controller.probationary;

import edu.twt.party.annotation.JwtToken;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.probationary.StudentVO;
import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.service.probationary.ProbationaryPartyMemberStudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Tag(name = "预备党员培训")
@RestController
@RequestMapping("/api/probationary/student")
@CrossOrigin
public class ProbationaryPartyMemberStudentController {
    @Autowired
    private ProbationaryPartyMemberStudentService studentService;

    @GetMapping("/{sno}")
    @Operation(summary = "根据学号获取所有培训情况")
    public ResponseHelper<List<StudentVO> > getStudentListBySno(@PathVariable String sno) {
        return new ResponseHelper<>(studentService.getStudentListBySno(sno));
    }
    @JwtToken
    @GetMapping("/list")
    @Operation(summary = "获取登录学生的所有培训情况")
    public ResponseHelper<List<StudentVO> > getStudentListBySno(HttpServletRequest request) {
        TwtStudentInfo user = (TwtStudentInfo)(request.getAttribute("user"));
        return new ResponseHelper<>(studentService.getStudentListBySno(user.getSno()));
    }
    @JwtToken
    @GetMapping("/latest/whetherSignedUp")
    @Operation(summary = "获取学生是否报名最新一期培训")
    public ResponseHelper<Boolean> whetherSignedUp(HttpServletRequest request) {
        TwtStudentInfo user = (TwtStudentInfo) (request.getAttribute("user"));
        return new ResponseHelper<>(studentService.whetherSignedUp(user.getSno()));
    }
    @GetMapping("/{sno}/{term}")
    @Operation(summary = "根据学号和期数获取培训情况")
    public ResponseHelper<StudentVO> getStudentBySnoAndTerm(@PathVariable String sno, @PathVariable Integer term) {
        return new ResponseHelper<>(studentService.getStudentBySnoAndTerm(sno,term));
    }
    @GetMapping("/{term}")
    @Operation(summary = "获取某一期所有学生的培训情况")
    public ResponseHelper<List<StudentVO> > getStudentListByTerm(@PathVariable Integer term){
        return new ResponseHelper<>(studentService.getStudentListByTerm(term));

    }
    @GetMapping("/count/{term}")
    @Operation(summary = "统计某一期培训的报名人数")
    public ResponseHelper<Integer> countStudentNumberByTerm(@PathVariable Integer term) {
        return new ResponseHelper<>(studentService.countStudentNumberByTerm(term));
    }

    @JwtToken
    @PostMapping("/signUp/{term}")
    @Operation(summary = "报名某一期培训")
    public ResponseHelper<Boolean> signUp(Integer term, HttpServletRequest request) {
        TwtStudentInfo user = (TwtStudentInfo)request.getAttribute("user");
        return new ResponseHelper<>(studentService.signUp(user.getSno(),term));
    }
    @PostMapping("/signUp/{sno}/{term}")
    @Operation(summary = "管理员通过学号补报名某一期培训", description = "绕过培训能否报名状态")
    public ResponseHelper<Boolean> lateSignUp(String sno, Integer term) {
        return new ResponseHelper<>(studentService.lateSignUp(sno,term));
    }

    @JwtToken
    @PostMapping("/selectClass/{cid}")
    @Operation(summary = "根据课程cid选课")
    public ResponseHelper<Boolean> selectCourse(@PathVariable Integer cid, HttpServletRequest request) {
        TwtStudentInfo user = (TwtStudentInfo)request.getAttribute("user");
        return new ResponseHelper<>(studentService.selectCourse(user.getSno(), cid));
    }

    @JwtToken
    @PostMapping("/cancle/class/{cid}")
    @Operation(summary = "取消选课")
    public ResponseHelper<Boolean> cancleCourse(@PathVariable Integer cid, HttpServletRequest request) {
        TwtStudentInfo user = (TwtStudentInfo)request.getAttribute("user");
        return new ResponseHelper<>(studentService.cancelCourse(user.getSno(), cid));
    }

    @PostMapping("/selectClass/{sno}/{cid}")
    @Operation(summary = "管理员根据学号和课程cid补选课")
    public ResponseHelper<Boolean> lateSelectCourse(@PathVariable String sno, @PathVariable Integer cid) {
        return new ResponseHelper<>(studentService.lateSelectCourse(sno, cid));
    }

    @JwtToken
    @PostMapping("/cancle/train/{term}")
    @Operation(summary = "取消报名某一期培训", description = "只能取消一次，若取消过返回false")
    public ResponseHelper<Boolean> cancleSignUp(@PathVariable Integer term,HttpServletRequest request) {
        TwtStudentInfo user = (TwtStudentInfo)request.getAttribute("user");
        return new ResponseHelper<>(studentService.cancelSignUp(user.getSno(), term));
    }
}
