package edu.twt.party.pojo.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xsr
 * 资源库文件
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryFile {
    private Integer libraryFileId;
    private Integer columnId;
    private String text;  // 文章内容
    private String path;  //文件路径
    private String title;
    private String createdTime;
    private String updatedTime;
    private Integer isDeleted;
}
