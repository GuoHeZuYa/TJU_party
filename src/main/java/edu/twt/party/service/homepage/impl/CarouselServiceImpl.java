package edu.twt.party.service.homepage.impl;

import edu.twt.party.dao.homepage.CarouselMapper;
import edu.twt.party.exception.APIException;
import edu.twt.party.pojo.homepage.Carousel;
import edu.twt.party.service.homepage.CarouselService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xsr
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class CarouselServiceImpl implements CarouselService {

    @Resource
    CarouselMapper carouselMapper;

//    @Resource
//    FileUploadService fileUploadService;

    /**
     * 在列表头部添加轮播图
     *
     * @param fileId 轮播图对应的文件在资源库中的id
     * @param link      轮播图对应的跳转链接
     * @param title       轮播图对应标题
     * @param picturePath 轮播图的图片路径
     * @return Boolean
     */
    @Override
    public Boolean addCarousel(Integer fileId, String link, String title, String picturePath) throws APIException {
        try {
            List<Carousel> presentList = carouselMapper.getCarousel();
            for(Carousel tmp : presentList) {
                carouselMapper.alterCarouselPosition(tmp.getPosition() + 1, tmp.getCarouselId());
            }
            carouselMapper.addCarousel(fileId, link, title, picturePath, 1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("添加轮播图错误");
        }
    }

//    /**
//     * 添加轮播图（使用上传图片）
//     *
//     * @param fileId        轮播图对应文件在资源库中的id
//     * @param title         轮播图对应标题
//     * @param multipartFile 上传的轮播图图片
//     * @return Boolean
//     */
//    @Override
//    public Boolean addCarousel(Integer fileId, String title, MultipartFile multipartFile) {
//        try {
//            //String userId = JwtUtils.getUid(token);
//            String picturePath = fileUploadService.saveFile(multipartFile, 1);
//            List<Carousel> presentList = carouselMapper.getCarousel();
//            carouselMapper.addCarousel(fileId, title, picturePath, presentList.size() + 1);
//            return true;
//        } catch (APIException e) {
//            throw e;
//        }  catch (Exception e) {
//            throw new APIException("添加轮播图错误");
//        }
//    }

    /**
     * 获取所有轮播图相关信息
     *
     * @return 轮播图List
     */
    @Override
    public List<Carousel> getCarousel() throws APIException {
        try {
            return carouselMapper.getCarousel();
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("获取轮播图错误");
        }
    }

    /**
     * 将特定的轮播图置顶
     *
     * @param carouselId 需置顶的轮播图id
     * @return Boolean
     */
    @Override
    public Boolean makeTop(Integer carouselId) throws APIException {
        try {
            List<Carousel> presentList = carouselMapper.getCarousel();
            int presentPosition = carouselMapper.getPositionById(carouselId);
            if(presentPosition == 0) {
                throw new APIException("轮播图不存在");
            }
            carouselMapper.alterCarouselPosition(1,carouselId);
            //调整受影响的轮播图的位置
            for(Carousel tmp : presentList) {
                if(tmp.getPosition() < presentPosition) {
                    carouselMapper.alterCarouselPosition(tmp.getPosition() + 1, tmp.getCarouselId());
                }
            }
            return true;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("置顶轮播图错误");
        }
    }

    /**
     * 修改轮播图
     *
     * @param fileId 修改后文件对应的资源库中的id
     * @param carouselId 需修改的轮播图id
     * @param newLink    修改后的跳转链接
     * @param newTitle   修改后的题目
     * @param newPicPath 修改后的图片路径
     * @return Boolean
     */
    @Override
    public Boolean alterCarousel(Integer fileId, Integer carouselId, String newLink, String newTitle, String newPicPath) {
        try {
            carouselMapper.alterCarousel(fileId, carouselId, newLink, newTitle, newPicPath);
            return true;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("修改轮播图错误");
        }
    }

    /**
     * 删除轮播图
     *
     * @param carouselId 轮播图id
     * @return Integer
     */
    @Override
    public Boolean deleteCarousel(Integer carouselId) throws APIException {
        try {
            List<Carousel> presentList = carouselMapper.getCarousel();
            int presentPosition = carouselMapper.getPositionById(carouselId);
            if(presentPosition == 0) {
                throw new APIException("轮播图不存在");
            }
            //调整删除后位置受影响的轮播图的位置
            for(Carousel tmp : presentList) {
                if(tmp.getPosition() > presentPosition) {
                    carouselMapper.alterCarouselPosition(tmp.getPosition() - 1, tmp.getCarouselId());
                }
            }
            carouselMapper.deleteCarousel(carouselId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("删除轮播图错误");
        }
    }
}
