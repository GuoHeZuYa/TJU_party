package edu.twt.party.dao.library;

import edu.twt.party.pojo.library.Column;
import edu.twt.party.pojo.library.ColumnInfo;
import edu.twt.party.pojo.library.LibraryFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xsr
 * 栏目及文章Mapper
 */
@Repository
public interface ColumnMapper {

    @Insert("insert into `column` (`name`, `position`) values(#{name}, #{position})")
    int addColumn(@Param("name") String name, @Param("position") int position);

    @Insert("insert into `library_file`(`column_id`, `address`, `title`) values(#{columnId}, #{address}. #{title}")
    int addFile(@Param("columnId") int columnId, @Param("address") String address, @Param("title") String title);

    @Update("update `column` set position = #{newPosition} where column_id = #{columnId}")
    int alterPosition(@Param("newPosition") int position, @Param("columnId") int columnId);

    @Update("update `column` set name = #{newName} where column_id = #{columnId}")
    int alterColumnName(@Param("newName") String newName, @Param("columnId") int columnId);

    @Update("update `column` set is_deleted = 1 where column_id = #{columnId}")
    int deleteColumn(@Param("columnId") int columnId);

    @Update("update `library_file` set is_deleted = 1 where library_file_id = #{fileId}")
    int deleteFile(@Param("fileId") int fileId);

    @Select("select * from `column` where is_deleted = 0 order by position")
    List<Column> getAllColumns();

    //获取栏目的同时查询栏目中文件的数目
    @Select("select column.column_id, name, position, column.created_time, column.updated_time, (\n" +
            "    select count(*)\n" +
            "    from `library_file`\n" +
            "    where library_file.is_deleted = 0 and column.column_id = library_file.column_id\n" +
            "    ) as file_num\n" +
            "from `column`\n" +
            "where column.is_deleted = 0\n" +
            "group by column.column_id, column.position\n" +
            "order by column.position")
    List<ColumnInfo> getAllColumnsWithNumber();

    @Select("select * from `library_file` where is_deleted = 0 and column_id = #{columnId}")
    List<LibraryFile> getPassagesOfColumn(@Param("columnId") int columnId);

    @Select("select * from `column` where column_id = #{columnId} and is_deleted = 0")
    Column getColumnByColumnId(@Param("columnId") int columnId);

    @Select("select position from `column` where column_id = #{columnId}")
    int getColumnPosition(@Param("columnId") int columnId);

    @Select(("select max(position) as max_position from `column` where is_deleted = 0"))
    int getMaxColumnPosition();

}
