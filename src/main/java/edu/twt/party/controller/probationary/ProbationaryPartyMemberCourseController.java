package edu.twt.party.controller.probationary;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberCourse;
import edu.twt.party.pojo.probationary.CourseDTO;
import edu.twt.party.pojo.probationary.CourseVO;
import edu.twt.party.service.probationary.ProbationaryPartyMemberCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "预备党员培训")
@RestController
@RequestMapping("/api/probationary/course")
@CrossOrigin
public class ProbationaryPartyMemberCourseController {
    @Autowired
    private ProbationaryPartyMemberCourseService courseService;
    @GetMapping("/{id}")
    @Operation(summary = "根据id获取培训课程",description = "0必修 1选修")
    public ResponseHelper<ProbationaryPartyMemberCourse> getCourseById(@PathVariable Integer id) {
        return new ResponseHelper<>(courseService.getById(id));
    }
    @GetMapping("/term/{term}")
    @Operation(summary = "根据term获取所有培训课程",description = "先必修后选修")
    public ResponseHelper<List<ProbationaryPartyMemberCourse>> getCoursesByTerm(@PathVariable Integer term) {
        QueryWrapper<ProbationaryPartyMemberCourse> wrapper = new QueryWrapper();
        wrapper .eq("term",term)
                .orderByAsc("type");
        return new ResponseHelper<>(courseService.list(wrapper));
    }
    @GetMapping("/{term}/{type}")
    @Operation(summary = "获取某一期培训的某一类型的所有课程信息", description = "0必修 1选修")
    public ResponseHelper<List<CourseVO> > getCourseByTermAndType(@PathVariable Integer term, @PathVariable Boolean type) {
        return new ResponseHelper<>(courseService.getCourseByTermAndType(term, type));
    }
    @PostMapping
    @Operation(summary = "添加培训课程")
    public ResponseHelper<Integer> addCourse(@RequestBody CourseDTO courseDTO) {
        return new ResponseHelper<>(courseService.addCourse(courseDTO));
    }

    @GetMapping("/del/{id}")
    @Operation(summary = "根据id删除培训课程")
    public ResponseHelper<Boolean> delCourseById(@PathVariable Integer id) {
        return new ResponseHelper<>(courseService.removeById(id));
    }

    @PostMapping("/update")
    @Operation(summary = "修改培训课程",description = "deleted和updateTime变量不加")
    public ResponseHelper<Boolean> updateCourse(@RequestBody ProbationaryPartyMemberCourse course) {
        return new ResponseHelper<>(courseService.updateById(course));
    }

}
