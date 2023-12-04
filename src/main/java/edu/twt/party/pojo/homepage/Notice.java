package edu.twt.party.pojo.homepage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author xsr
 * 近期通知
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
    private Integer libraryFileId;
    private String text;  // 通知内容
    private String path;  //附件路径
    private String title;
    private Integer position;

    private Timestamp updatedTime;
}
