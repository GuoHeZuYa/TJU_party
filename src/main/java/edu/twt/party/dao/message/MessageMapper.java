package edu.twt.party.dao.message;

import edu.twt.party.pojo.message.AdminMessage;
import edu.twt.party.pojo.message.Message;
import edu.twt.party.pojo.message.MessageImport;
import edu.twt.party.pojo.message.StuMessage;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xsr
 */
@Repository
public interface MessageMapper {

    @Insert("insert into `message` (`title`, `content`, `picture_path`, `attachment_path`) " +
            "values(#{title}, #{content}, #{picturePath}, #{attachmentPath})")
    @Options(useGeneratedKeys=true, keyProperty="messageId", keyColumn="message_id")
    void addMessage(Message message);

    @Insert("insert into `stu_message_info` (`sender_id`, `receiver_id`, `handler_id`, `status`, `message_id`) " +
            "values(#{senderId}, #{receiverId}, #{handlerId}, #{status}, #{messageId})")
    int addStuMessage(@Param("senderId") int senderId, @Param("receiverId") int receiverId,
                      @Param("handlerId") int handleId, @Param("status") int status, @Param("messageId") int messageId);

    @Insert("insert into `admin_message_info` (`sender_id`, `receiver_id`, `status`, `message_id`, `reply_message_id`) " +
            "values(#{senderId}, #{receiverId}, #{status}, #{messageId}, #{replyMessageId})")
    int addAdminMessage(@Param("senderId") int senderId, @Param("receiverId") int receiverId,
                        @Param("status") int status, @Param("messageId") int messageId,
                        @Param("replyMessageId") int replyMessageId);

    @Update("update stu_message_info set handler_id = #{handlerId} where message_info_id = #{messageInfoId}")
    int setStuMessageHandler(@Param("handlerId") int handlerId, @Param("messageInfoId") int mesageInfoId);

    @Update("update stu_message_info set status = 1 where message_info_id = #{messageInfoId}")
    int setStuMessageReaded(@Param("messageInfoId") int messageInfoId);

    @Update("update admin_message_info set status = 1 where message_info_id = #{messageInfoId}")
    int setAdminMessageReaded(@Param("messageInfoId") int messageInfoId);

    @Select("select message_info_id, title, content, attachment_path, picture_path, " +
            "sender_id, receiver_id, handler_id, status, send_time " +
            "from `message` inner join `stu_message_info` " +
            "on message.message_id = stu_message_info.message_id " +
            "where sender_id = #{uid} and is_deleted = 0 " +
            "order by send_time desc")
    List<StuMessage> getSentStuMessage(@Param("uid") int uid);

    @Select("<script>" +
            "select message_info_id, title, content, attachment_path, picture_path, " +
            "sender_id, receiver_id, handler_id, status, send_time " +
            "from `message` inner join `stu_message_info` " +
            "on message.message_id = stu_message_info.message_id " +
            "where receiver_id = #{uid} and is_deleted = 0 " +
            "<if test='status != null'>" +
            "and status = #{status} " +
            "</if>" +
            "<if test='sortBy == \"send_time\" or sortBy == \"status\"'>" +
            "order by ${sortBy} " +
            "</if>" +
            "<if test='desc == true'>" +
            "desc" +
            "</if>" +
            "limit #{startNum},15" +
            "</script>")
    List<StuMessage> getReceivedStuMessage(@Param("uid") int uid,
                                           @Param("sortBy") String sortBy,
                                           @Param("desc") Boolean desc,
                                           @Param("status")Integer status,
                                           @Param("startNum")Integer startNum);
    @Select("<script>" +
            "select count(*) from message inner join stu_message_info ami on message.message_id = ami.message_id where receiver_id = #{uid} and is_deleted = 0" +
            "<if test='status!=null'>" +
            " and status = #{status}" +
            "</if>" +
            "</script>")
    Integer getReceivedStuMsgCount(@Param("uid")int uid,@Param("status")Integer status);
    @Select("<script>" +
            "select message_info_id, title, content, attachment_path, picture_path, " +
            "sender_id, receiver_id, status, reply_message_id, send_time " +
            "from `message` inner join `admin_message_info` " +
            "on message.message_id = admin_message_info.message_id " +
            "where sender_id = #{uid} and is_deleted = 0 " +
            "<if test='status != null'>" +
            "and status = #{status} " +
            "</if>" +
            "<if test='sortBy == \"send_time\" or sortBy == \"status\"'>" +
            "order by ${sortBy} " +
            "</if>" +
            "<if test='desc == true'>" +
            "desc" +
            "</if>" +
            "limit #{startNum},15" +
            "</script>")
    List<AdminMessage> getSentAdminMessage(@Param("uid") int uid,
                                           @Param("sortBy") String sortBy,
                                           @Param("desc") Boolean desc,
                                           @Param("status") Integer status,
                                           @Param("startNum")Integer startNum);
    @Select("<script>" +
            "select count(*) from message inner join admin_message_info ami on message.message_id = ami.message_id where sender_id = #{uid} and is_deleted = 0" +
            "<if test='status!=null'>" +
            " and status = #{status}" +
            "</if>" +
            "</script>")
    Integer getSentAdminMsgCount(@Param("uid") int uid,
                                 @Param("status")Integer status);


    @Select("select message_info_id, title, content, attachment_path, picture_path, " +
            "sender_id, receiver_id, status, reply_message_id, send_time " +
            "from `message` inner join `admin_message_info` " +
            "on message.message_id = admin_message_info.message_id " +
            "where receiver_id = #{uid} and is_deleted = 0 " +
            "order by send_time desc")
    List<AdminMessage> getReceivedAdminMessage(@Param("uid") int uid);

    @Select("<script>" +
            "select message_info_id, title, content, attachment_path, picture_path, " +
            "sender_id, receiver_id, status, reply_message_id, send_time " +
            "from `message` inner join `admin_message_info` " +
            "on message.message_id = admin_message_info.message_id " +
            "where receiver_id in (" +
            "<foreach item='item' collection='typeId' separator=','>" +
            "#{item}" +
            "</foreach>" +
            ") and is_deleted = 0 " +
            "order by send_time desc" +
            "</script>")
    List<AdminMessage> getReceivedAdminMessageByType(ArrayList<Integer> typeId);

    @Select("select is_secretary*4+is_organizer*2+is_propagator from\n" +
            "(select count(*)>0 as is_secretary from twt_partybranch where partybranch_secretary = #{uid} and partybranch_isdeleted = false) as field1," +
            "(select count(*)>0 as is_organizer from twt_partybranch where partybranch_organizer = #{uid} and partybranch_isdeleted = false) as field2," +
            "(select count(*)>0 as is_propagator from twt_partybranch where partybranch_propagator = #{uid} and partybranch_isdeleted = false) as field3")
    Integer getUserType(int uid);
    @Select("select academy_id from twt_student_info where id = #{uid}")
    Integer getAcademyIdByUid(int uid);
    @Select("select sender_id from stu_message_info where message_info_id = #{messageInfoId}")
    int getSenderIdByMessageInfoId(@Param("messageInfoId") int messageInfoId);

    @Select("select if (manager_type=127,manager_type,manager_academy*1000+manager_type) as role from twt_manager where manager_id = #{managerId}")
    int getRoleById(@Param("managerId") int managerId);

    @Select("select id from twt_student_info where academy_id = #{academyId}")
    List<Integer> getUidByAcademyId(@Param("academyId") int academyId);

    @Select("select id from twt_student_info")
    List<Integer> getAllUid();

    @Insert("<script>" +
            "insert into message_import (import_id,name,sno) values " +
            "<foreach item='item' collection='messageImports' separator=','>" +
            "(#{importId},#{item.name},#{item.sno})" +
            "</foreach>" +
            "</script>")
    int importExcelUserList(ArrayList<MessageImport> messageImports,int importId);

    @Select("select max(import_id)+1 from message_import")
    int getNewImportId();

    @Update("update message_import,twt_student_info, b_userinfo set user_id=twt_student_info.id where message_import.import_id = #{importId} and message_import.is_deleted = false and message_import.name = b_userinfo.username and message_import.sno = b_userinfo.usernumb and message_import.sno = twt_student_info.sno")
    int updateExcelUserListByImportId(int importId);
    @Select("select * from message_import where is_deleted = 0 and import_id = #{importId}")
    ArrayList<MessageImport> getImportExcelByImportId(int importId);
    @Update("update message_import set name=#{name},sno=#{sno} where id=#{id}")
    boolean updateExcelItem(int id,String name,String sno);

    @Select("select distinct import_id from message_import where user_id = #{uid}")
    ArrayList<Integer> getImportMessageIdByUid(int uid);
}
