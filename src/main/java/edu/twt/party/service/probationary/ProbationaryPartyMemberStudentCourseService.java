package edu.twt.party.service.probationary;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.twt.party.pojo.probationary.AdjustDTO;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberStudentCourse;
import edu.twt.party.pojo.probationary.ScoreVO;
import edu.twt.party.pojo.probationary.StudentCourseInfo;

import java.util.List;

public interface ProbationaryPartyMemberStudentCourseService extends IService<ProbationaryPartyMemberStudentCourse> {
    List<ScoreVO> getStudentListByTermAndCid(Integer term, Integer cid);

    Boolean updateScore(ScoreVO score);

    Boolean adjustCourse(AdjustDTO adjustDTO);

    List<StudentCourseInfo> getStudentCourseInfoByTerm(String sno, Integer term);
}
