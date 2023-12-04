package edu.twt.party.dao.file;

import edu.twt.party.pojo.file.File;
import edu.twt.party.pojo.file.FileType;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileUploadMapper {

    @Insert("insert into file (save_name, uploader_id, type) values (#{saveName}, #{uploaderId}, #{fileType});")
    boolean saveFile(String saveName, int uploaderId, FileType fileType);

    @Select("select * from file where id = #{id} and is_deleted = 0;")
    File getFileById(@Param("id") int id);

    @Select("select * from file where is_deleted = 0")
    List<File> getAllFile();

    @Update("update file set is_deleted = 1 where id = #{id};")
    boolean deleteFile(@Param("id") int id);
}
