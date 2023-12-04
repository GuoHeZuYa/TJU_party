package edu.twt.party.dao.probationary;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberStudent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProbationaryPartyMemberStudentMapper extends BaseMapper<ProbationaryPartyMemberStudent> {
    @Select("select term from probationary_party_member_student where sno = #{sno} and deleted = 0")
    List<Integer> getTermsBySno(String sno);
    @Select("select sno form probationary_party_member_student where term = #{term} and deleted = 0")
    List<String> getSnosByTerm(Integer term);

}
