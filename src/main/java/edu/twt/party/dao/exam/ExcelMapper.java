package edu.twt.party.dao.exam;

import edu.twt.party.pojo.excelParse.applicant.ApplicantExcelImport;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ExcelMapper {
    @Insert("<script>" +
            "insert into applicant_import (sno,score,detail_score,status,punish,comments,import_id,name) values " +
            "<foreach collection ='list' item = 'item' separator=','>" +
            "(#{item.sno},#{item.score},#{item.detailScore},#{item.status},#{item.punish},#{item.comments},#{importId},#{item.name})" +
            "</foreach></script>")
    boolean insertApplicantBatch(List<ApplicantExcelImport> list, int importId);
    @Select("select ai.id, score, detail_score, status, punish, comments, import_id, b_userinfo.username as name,tsi.id as user_id " +
            "from (select * from applicant_import where import_id = #{importId}) as ai " +
            "left join twt_student_info tsi on ai.sno = tsi.sno " +
            "left join b_userinfo on (tsi.sno = b_userinfo.usernumb and ai.name = b_userinfo.username)")
    List<ApplicantExcelImport> getApplicantsWithUserId(int importId);
    @Select("select ifnull(import_id,0) + 1 from applicant_import order by import_id desc limit 1")
    Integer getNewImportId();
    @Insert("insert into exam_result(exam_id,score, detail_score,status,comments,user_id)  (select #{examId} as exam_id, score, detail_score, status, comments, tsi.id as user_id\n" +
            "from (select * from applicant_import where import_id = #{importId}) as ai\n" +
            "left join twt_student_info tsi on ai.sno = tsi.sno\n" +
            "left join b_userinfo on (tsi.sno = b_userinfo.usernumb and ai.name = b_userinfo.username))")
    Boolean commitApplicant(Integer importId,Integer examId);

    @Update("update applicant_import set sno = #{sno},score=#{score},detail_score=#{detailScore},status=#{status},punish=#{punish},comments=#{comments},name=#{name} where id = #{id}")
    Boolean updateExcelImport(ApplicantExcelImport aei);

}
