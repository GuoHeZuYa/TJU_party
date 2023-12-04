package edu.twt.party.dao.probationary;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberStudentCourse;
import edu.twt.party.pojo.probationary.StudentCourseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProbationaryPartyMemberStudentCourseMapper extends BaseMapper<ProbationaryPartyMemberStudentCourse> {
    @Select("select sc.cid, sc.term, c.name, c.type, sc.inherited, sc.score from probationary_party_member_student_course sc, probationary_party_member_course c" +
            "where sc.sno=#{sno} and sc.cid = c.id and sc.term = #{term} and sc.deleted = 0 and c.deleted = 0")
    List<StudentCourseVO> getScoresBySnoAndTerm(String sno, Integer term);
}
