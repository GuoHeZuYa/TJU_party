package edu.twt.party.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.twt.party.pojo.student.StudentVo;
import edu.twt.party.pojo.user.NameSno;
import org.apache.ibatis.annotations.Param;
import edu.twt.party.pojo.student.BUserinfo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * (BUserinfo)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-11 16:26:10
 */
@Repository
public interface BUserinfoMapper extends BaseMapper<BUserinfo> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<BUserinfo> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<BUserinfo> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<BUserinfo> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<BUserinfo> entities);


    @Update("update b_userinfo set partybranchid = #{partyBranchId} where usernumb = #{sno}")
    int updatePartybranchid(String sno,int partyBranchId);

    @Select("SELECT * FROM b_userinfo WHERE username = #{name} AND b_userinfo.usernumb = #{sno}")
    BUserinfo checkNameSno(String name,String sno);

    @Select("SELECT username FROM b_userinfo WHERE b_userinfo.usernumb = #{sno}")
    String getUserName(String sno);




}

