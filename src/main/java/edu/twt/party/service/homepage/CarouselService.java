package edu.twt.party.service.homepage;


import edu.twt.party.pojo.homepage.Carousel;


import java.util.List;

/**
 * 轮播图
 */
public interface CarouselService {
    /**
     * 添加轮播图（使用图片路径）
     *
     * @param fileId 轮播图对应的文件在资源库中的id
     * @param link 轮播图对应的跳转链接
     * @param title 轮播图对应标题
     * @param picturePath 轮播图的图片路径
     * @return Boolean
     */
    Boolean addCarousel(Integer fileId, String link, String title, String picturePath);

//    /**
//     * 添加轮播图（使用上传图片）
//     *
//     * @param fileId 轮播图对应文件在资源库中的id
//     * @param title 轮播图对应标题
//     * @param multipartFile 上传的轮播图图片
//     * @return Boolean
//     */
//    Boolean addCarousel(Integer fileId, String title, MultipartFile multipartFile);

    /**
     * 获取所有轮播图相关信息
     *
     * @return List
     */
    List<Carousel> getCarousel();

    /**
     * 将特定的轮播图置顶
     *
     * @param carouselId 需置顶的轮播图id
     * @return Boolean
     */
    Boolean makeTop(Integer carouselId);

    /**
     * 修改轮播图
     *
     * @param fileId 修改后的文件对应的资源库id
     * @param carouselId 需修改的轮播图id
     * @param newLink 修改后的跳转链接
     * @param newTitle 修改后的题目
     * @param newPicPath 修改后的图片路径
     * @return Boolean
     */
    Boolean alterCarousel(Integer fileId, Integer carouselId,  String newLink, String newTitle, String newPicPath);

    /**
     * 删除轮播图
     *
     * @param carouselId 轮播图id
     * @return Integer
     */
    Boolean deleteCarousel(Integer carouselId);
}
