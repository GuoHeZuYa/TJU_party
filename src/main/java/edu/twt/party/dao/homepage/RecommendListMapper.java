package edu.twt.party.dao.homepage;

import edu.twt.party.pojo.homepage.RecommendedFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendListMapper {
    @Insert("insert into `recommended_file` (`file_id`, `position`) values(#{fileId}, #{position})")
    int addRecommendedFile(@Param("fileId") int fileId, @Param("position") int position);

    @Update("update `recommended_file` set position = #{newPosition} where recommended_file_id = #{recommendedFileId}")
    int alterRecommendedFilePosition(@Param("newPosition") int newPosition, @Param("recommendedFileId") int recommendedFileId);

    @Update("update `recommended_file` set  file_id = #{newId} where recommended_file_id = #{recommendFileId}")
    int alterRecommendFile(@Param("newId") int newId, @Param("recommendFileId") int recommendFileId);

    @Update("update `recommended_file` set is_deleted = 1 where recommended_file_id = #{recommendFileId}")
    int deleteRecommendFile(@Param("recommendFileId") int recommendFileId);

    @Select("select * from `recommended_file` where is_deleted = 0 order by position")
    List<RecommendedFile> getRecommendList();

    @Select("select * from `recommended_file` where recommended_file_id = #{recommendedFileId} and is_deleted = 0")
    RecommendedFile getRecommendedFileById(@Param("recommendedFileId") int recommendedFileId);

    @Select("select position from `recommended_file` where recommended_file_id = #{recommendedFileId} and is_deleted = 0")
    int getPosition(@Param("recommendedFileId") int recommendedFileId);
}
