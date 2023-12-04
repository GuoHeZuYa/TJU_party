package edu.twt.party.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.twt.party.pojo.student.UserInfoBasic;
import edu.twt.party.pojo.student.StudentVo;
import edu.twt.party.pojo.student.TwtStudentInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (TwtStudentInfo)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-08 11:30:17
 */
@Repository
public interface TwtStudentInfoMapper extends BaseMapper<TwtStudentInfo> {




    @Select("select id from twt_student_info where sno = #{sno}")
    Integer getIdBySno(@Param("sno")String sno);

    @Update("update twt_student_info set partybranch_id = #{targetBranchId} where id = #{id}")
    Integer updateBranchById(@Param("id")Integer id,@Param("targetBranchId") Integer targetBranchId);


    @Select("select main_status from twt_student_info where id = #{id}")
    Integer getMainStatus(@Param("id")String id);

    List<StudentVo> selectStudentByCollegeAndGrade(Integer collegeId,Integer grade);


    List<StudentVo> selectStudentByClassIdList(List<Integer> classIdList);

    StudentVo selectInfoBySno(String sno);


    List<UserInfoBasic> selectBasicInfoBySnoList(List<String> sno);




}

