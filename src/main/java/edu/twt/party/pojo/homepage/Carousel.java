package edu.twt.party.pojo.homepage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 轮播图
 *
 * @author xsr
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carousel {
    private int carouselId;
    private String link;  //对应跳转链接
    private Integer fileId;  //对应的资源库文件的在资源库中的id
    private String title;
    private int position;  //轮播图的位置(1~n)
    private String picturePath;  //图片路径
    private String createdTime;
    private String updatedTime;
    private boolean isDeleted;
}
