package edu.twt.party.service.probationary;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.twt.party.pojo.probationary.CourseVO;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberCourse;
import edu.twt.party.pojo.probationary.CourseDTO;

import java.util.List;

public interface ProbationaryPartyMemberCourseService extends IService<ProbationaryPartyMemberCourse> {
    Integer addCourse(CourseDTO courseDTO);

    List<CourseVO> getCourseByTermAndType(Integer term, Boolean type);
}
