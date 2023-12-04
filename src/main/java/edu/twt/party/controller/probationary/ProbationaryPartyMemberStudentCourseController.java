package edu.twt.party.controller.probationary;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.twt.party.annotation.JwtToken;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.probationary.AdjustDTO;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberStudentCourse;
import edu.twt.party.pojo.probationary.ScoreVO;
import edu.twt.party.pojo.probationary.StudentCourseInfo;
import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.service.probationary.ProbationaryPartyMemberStudentCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Tag(name = "预备党员培训")
@RestController
@RequestMapping("/api/probationary/sc")
@CrossOrigin
public class ProbationaryPartyMemberStudentCourseController {
    @Autowired
    ProbationaryPartyMemberStudentCourseService studentCourseService;
    @GetMapping("/count/{term}/{cid}")
    @Operation(summary = "根据某一期培训的课程cid查看报名人数")
    public ResponseHelper<Long> countStudentNumberByCid(@PathVariable Integer cid) {
        QueryWrapper<ProbationaryPartyMemberStudentCourse> wrapper = new QueryWrapper<>();
        wrapper .eq("cid", cid)
                .eq("deleted", false);
        return new ResponseHelper<>(studentCourseService.count(wrapper));
    }
    @JwtToken
    @GetMapping("/info/{term}")
    @Operation(summary = "获取登录学生的某一期培训的课程选择情况",description = "如未报名则返回null")
    public ResponseHelper<List<StudentCourseInfo> > getStudentCourseInfoByTerm(@PathVariable Integer term, HttpServletRequest request) {
        TwtStudentInfo user = (TwtStudentInfo) (request.getAttribute("user"));
        return new ResponseHelper(studentCourseService.getStudentCourseInfoByTerm(user.getSno(),term));
    }

    @GetMapping("/student/{term}/{cid}")
    @Operation(summary = "获取某一期培训某课程的全部成员信息",description = "可以用来录入成绩的时候获取学生学号sno，成绩那一栏如果是" +
            "选修课则为P或者NP，如果是必修课则为数字,如果未出分则为-")
    public ResponseHelper<List<ScoreVO>> getStudentListByTermAndCid(@PathVariable Integer term, @PathVariable Integer cid) {
        return new ResponseHelper<>(studentCourseService.getStudentListByTermAndCid(term, cid));
    }
    @PostMapping("/grade")
    @Operation(summary = "成绩录入", description = "这个接口也可用来成绩微调")
    public ResponseHelper<Boolean> updateScore(@RequestBody ScoreVO score) {
        return new ResponseHelper<>(studentCourseService.updateScore(score));
    }
    @PostMapping("/adjust")
    @Operation(summary = "调课")
    public ResponseHelper<Boolean> adjustCourse(@RequestBody AdjustDTO adjustDTO) {
        return new ResponseHelper<>(studentCourseService.adjustCourse(adjustDTO));
    }
}
