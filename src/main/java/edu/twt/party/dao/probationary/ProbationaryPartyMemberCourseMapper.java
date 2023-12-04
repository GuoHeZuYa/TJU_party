package edu.twt.party.dao.probationary;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.twt.party.pojo.probationary.CourseVO;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProbationaryPartyMemberCourseMapper extends BaseMapper<ProbationaryPartyMemberCourse> {
    @Select("select id,name,term,start_time,location,status,teacher,type,end_time from probationary_party_member_course " +
            "where term = #{term} and type = #{type} and deleted = 0")
    List<CourseVO> getCourseByTermAndType(Integer term, Boolean type);
    @Select("select id,name,term,start_time,location,status,teacher,type,end_time from probationary_party_member_course " +
            "where term = #{term} and deleted = 0")
    List<CourseVO> getCourseByTerm(Integer term, Boolean type);
}
