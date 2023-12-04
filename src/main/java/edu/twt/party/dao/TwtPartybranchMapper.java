package edu.twt.party.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.twt.party.pojo.partyBranch.TwtPartyBranchVo;
import edu.twt.party.pojo.partyBranch.TwtPartybranch;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (TwtPartybranch)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-10 14:43:28
 */
@Repository
public interface TwtPartybranchMapper extends BaseMapper<TwtPartybranch> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<TwtPartybranch> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<TwtPartybranch> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<TwtPartybranch> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<TwtPartybranch> entities);




    List<TwtPartyBranchVo> selectBranchList(Integer partybranch_academy,String partybranch_schoolyear,
            String partybranch_type);

    @Update("update twt_partybranch set partybranch_isdeleted = 1 where partybranch_id = #{id} and " +
            "partybranch_isdeleted = 0")
    Integer delete(Integer id);



    TwtPartyBranchVo selectVoById(Integer branchId);


    int batchUpdateUserBranch(List<String >snoList,Integer targetBranchId);


    int kickOut(List<Integer> uidList,Integer branchId);


    @Update("UPDATE twt_partybranch SET partybranch_secretary = #{uid} " +
            "WHERE twt_partybranch.partybranch_id = #{targetBranchId} ")
    int updatePartybranchSecretary(Integer targetBranchId,Integer uid);

    @Update("UPDATE  twt_partybranch SET  partybranch_organizer = #{uid} " +
            "WHERE twt_partybranch.partybranch_id = #{targetBranchId}")
    int updatePartybranchOrganizerInt(Integer targetBranchId,Integer uid);

    @Update("UPDATE twt_partybranch SET  partybranch_propagator = #{uid} " +
            "WHERE partybranch_id = #{targetBranchId}")
    int updatePartyBranchPropogator(Integer targetBranchId,Integer uid);









}

