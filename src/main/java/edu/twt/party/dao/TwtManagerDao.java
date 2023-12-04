package edu.twt.party.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.twt.party.pojo.Role;
import org.apache.ibatis.annotations.Param;
import edu.twt.party.pojo.manager.TwtManager;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * (TwtManager)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-17 14:39:07
 */
@Repository
public interface TwtManagerDao extends BaseMapper<TwtManager> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<TwtManager> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<TwtManager> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<TwtManager> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<TwtManager> entities);

    @Select("select manager_type from twt_manager where manager_name = #{manager_name}")
    int getRole(String manager_name);

    @Select("select * from twt_manager where manager_name = #{manager_name}")
    TwtManager selectByName(String manager_name);



}

