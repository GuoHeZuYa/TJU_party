package edu.twt.party.pojo.homepage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推荐列表的返回类
 *
 * @author xsr
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendListUnit {
    private int recommendedFileId;
    private Integer fileId;  //对应的文件在资源库中的id
    private String title;  //文件标题
    private int position;  //文件在列表中的位置，按照从1到n排序
}
