package edu.twt.party.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.twt.party.pojo.classPojo.ClassVo;
import org.apache.ibatis.annotations.Param;
import edu.twt.party.pojo.classPojo.BClass;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * (BClass)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-29 16:43:07
 */
@Repository
public interface BClassMapper extends BaseMapper<BClass> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<BClass> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<BClass> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<BClass> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<BClass> entities);


    @Select("select id as classId, classname from b_class where collegeid = #{collegeId} and grade = #{grade}")
    List<ClassVo> listClass(Integer collegeId,Integer grade);




}

