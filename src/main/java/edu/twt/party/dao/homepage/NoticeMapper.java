package edu.twt.party.dao.homepage;

import edu.twt.party.pojo.homepage.NoticeOrder;
import edu.twt.party.pojo.homepage.Notice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeMapper {
    @Insert("insert into `notice_order` (`library_file_id`, `position`) values(#{libraryFileId}, #{position})")
    int addNotice(@Param("libraryFileId") Integer libraryFileId, @Param("position") Integer position);

    @Update("update `notice_order` set position = #{newPosition} where library_file_id = #{libraryFileId}")
    int alterNoticePosition(@Param("libraryFileId") Integer libraryFileId, @Param("newPosition") Integer newPosition);

    @Update("update `notice_order` set is_deleted = 1 where library_file_id = #{libraryFileId}")
    int deleteNoticePosition(@Param("libraryFileId") Integer libraryFileId);

    @Select("select * from `notice_order` where is_deleted = 0")
    List<NoticeOrder> getNoticeOrder();

    @Select("select * from `notice_order` where is_deleted = 0 and library_file_id = #{libraryFileId} and is_deleted = 0")
    NoticeOrder getNoticeOrderById(@Param("libraryFileId") Integer libraryFileId);

    @Select("select library_file.library_file_id, `text`, `path`, `title`, `position`, notice_order.updated_time from `library_file` inner join `notice_order`"
            +" ON library_file.library_file_id = notice_order.library_file_id where library_file.is_deleted = 0 and notice_order.is_deleted = 0 order by position")
    List<Notice> getNotice();
}
