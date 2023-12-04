package edu.twt.party.dao.file;

import edu.twt.party.pojo.file.HTD;
import edu.twt.party.pojo.file.HTDVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HTDMapper {
    @Select("select * from hyper_text_documents where user_id = #{userId} order by type desc,id")
    List<HTD> getHasCommitByUserId(int userId);
    @Select("select * from hyper_text_documents where user_id = #{userId} and type = #{type} order by id desc limit 1")
    HTD getHTDByUserIdAndType(int userId,int type);
    @Insert("insert into hyper_text_documents (user_id, type, content) values (#{userId},#{type},#{content})")
    Boolean insertHTD(int userId, int type, String content);
    @Update("update hyper_text_documents set status = #{status} where user_id = #{userId} and type = #{type} order by id desc limit 1")
    Boolean updateHTDStatus(int userId, int type, int status);
    @Update("update hyper_text_documents set comment = #{comment} where id = #{id}")
    Boolean commentHTDById(int id,String comment);
    @Update("update hyper_text_documents set comment = #{comment} where user_id = #{userId} and type = #{type} order by id desc limit 1")
    Boolean commentHTDByUserIdAndType(int userId, int type, String comment);
    @Select("select htd.* ,sno ,username as user_name from (select * from hyper_text_documents where status = 0 and is_delete =0) htd left join twt_student_info on htd.user_id = twt_student_info.id left join b_userinfo on b_userinfo.usernumb = twt_student_info.sno where partybranch_id = #{partyBranchId}")
    List<HTDVo> getUnread(int partyBranchId);
}
