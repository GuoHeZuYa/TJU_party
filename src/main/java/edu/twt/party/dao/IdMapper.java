package edu.twt.party.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface IdMapper {
    @Select("select id from twt_student_info where sno = #{sno}")
    Integer getUserIdBySno(String sno);
    @Select("select sno from twt_student_info where id = #{id}")
    String getUserSnoById(Integer id);
}
