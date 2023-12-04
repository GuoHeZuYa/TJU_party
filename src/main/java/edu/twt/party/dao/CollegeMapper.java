package edu.twt.party.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.twt.party.pojo.College;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CollegeMapper extends BaseMapper<College> {
    @Select("select * from b_college where state = 'ok' and id between 200 and 300;")
    List<College> getColleges();

}
