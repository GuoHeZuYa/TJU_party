package edu.twt.party.pojo.homepage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xsr
 * 推荐列表（首页管理）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendedFile {
    private int recommendedFileId;
    private int fileId;  //资源库中文件的id
    private int position;  //文件在列表中的位置，按照从1到n排序
    private String createdTime;
    private String updatedTime;
    private boolean isDeleted;
}