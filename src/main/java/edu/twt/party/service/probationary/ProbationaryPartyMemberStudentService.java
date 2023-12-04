package edu.twt.party.service.probationary;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberStudent;
import edu.twt.party.pojo.probationary.StudentVO;

import java.util.List;

public interface ProbationaryPartyMemberStudentService extends IService<ProbationaryPartyMemberStudent> {
    StudentVO getStudentBySnoAndTerm(String sno, Integer term);

    Boolean lateSelectCourse(String sno, Integer cid);

    Boolean signUp(String sno, Integer term);

    Boolean lateSignUp(String sno, Integer term);

    Integer countStudentNumberByTerm(Integer term);

    List<StudentVO> getStudentListBySno(String sno);

    List<StudentVO> getStudentListByTerm(Integer term);

    Boolean cancelSignUp(String sno, Integer term);

    Boolean selectCourse(String sno, Integer cid);

    Boolean cancelCourse(String sno, Integer cid);

    Boolean whetherSignedUp(String sno);

}
