package edu.twt.party.dao.library;

import edu.twt.party.pojo.library.LibraryFile;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资源库
 *
 * @author xsr
 */
@Repository
public interface LibraryMapper {

    @Insert("insert into `library_file` (`text`, `column_id`, `path`, `title`) values(#{text}, #{columnId}, #{path}, #{title})")
    int addLibraryFile(@Param("text") String text, @Param("columnId") int columnId, @Param("path") String path, @Param("title") String title);

    @Insert("insert into `library_file` (`text`, `column_id`, `path`, `title`) values(#{text}, #{columnId}, #{path}, #{title})")
    @Options(useGeneratedKeys=true, keyProperty="libraryFileId", keyColumn="library_file_id")
    void insertLibraryFile(LibraryFile libraryFile);

    @Update("update `library_file` set is_deleted = 1 where library_file_id = #{fileId} and is_deleted = 0")
    int deleteLibraryFile(@Param("fileId") int fileId);

    @Update("update `library_file` set text = #{text}, title = #{title}, path = #{path} where library_file_id = #{libraryFileId}")
    int alterLibraryFile(@Param("libraryFileId") Integer librraryFileId, @Param("text") String text,
                         @Param("path") String path, @Param("title") String title);

    @Select("select * from `library_file` where column_id = #{columnId} and is_deleted = 0 order by library_file_id desc limit #{startNum},12")
    List<LibraryFile> getLibraryFileByColumnId(@Param("columnId") int columnId,@Param("startNum")int startNum);

    @Select("select count(*) from `library_file` where column_id = #{id} and is_deleted = 0")
    Integer getLibraryFileCount(@Param("id") int id);

    @Select("select * from library_file where library_file_id = #{libraryFileId}")
    LibraryFile getLibraryFileById(@Param("libraryFileId") int libraryFileId);

    @Select("select * from `library_file` where title regexp #{name} order by updated_time desc")
    List<LibraryFile> search(@Param("name") String name);
}
