package edu.twt.party.dao;

import edu.twt.party.pojo.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMapper {
    @Select("select * from role where rid = #{id} limit 1")
    Role findById(@Param("id") int rid);
}
