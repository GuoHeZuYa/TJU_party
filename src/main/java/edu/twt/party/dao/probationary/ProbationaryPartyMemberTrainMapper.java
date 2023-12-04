package edu.twt.party.dao.probationary;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.twt.party.dao.exam.ExamMapper;
import edu.twt.party.pojo.exam.Exam;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberTrain;
import edu.twt.party.service.probationary.impl.ProbationaryPartyMemberTrainServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProbationaryPartyMemberTrainMapper extends BaseMapper<ProbationaryPartyMemberTrain>{


    //    @Update("update exam_list set status = status | #{canSelect} where times = #{term}")
//    Boolean setSelectByTerm(Integer term,Boolean canSelect);
    @Select("select * from exam_list where user_type = #{user_type} order by times desc limit 1")
    ProbationaryPartyMemberTrain getLatestTrain(Integer user_type);
    @Update("update exam_list set status = #{status} where times = #{term} and user_type = #{user_type}")
    Boolean updateStatusByTerm(Integer status, Integer term, Integer user_type);
    @Select("select status from exam_list where times = #{term} and user_type = #{user_type}")
    Integer getStatusByTerm(Integer term, Integer user_type);

    @Select("select * from exam_list where times = #{term} and user_type = #{user_type}")
    ProbationaryPartyMemberTrain getExamByTerm(Integer term, Integer user_type);
    @Select("select * from exam_list where user_type = #{user_type}")
    List<ProbationaryPartyMemberTrain> getExamList(Integer user_type);

//    @Update("update exam_list set set status = #{isOpen} where term = #{term}")
//    Boolean updateOpenStateByTerm(Integer term,Boolean isOpen);
//    @Select("select is_open from probationary_party_member_train where term = #{term}")
//    Boolean getOpenStateByTerm(Integer term);
//    @Select("select can_select from probationary_party_member_train where term = #{term}")
//    Boolean getSelectStateByTerm(Integer term);
}
