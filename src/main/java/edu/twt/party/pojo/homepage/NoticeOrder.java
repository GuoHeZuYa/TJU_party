package edu.twt.party.pojo.homepage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xsr
 * 近期通知列表的显示顺序
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeOrder {
    int libraryFileId;
    int position;
    private String createdTime;
    private String updatedTime;
    private int isDeleted;
}
