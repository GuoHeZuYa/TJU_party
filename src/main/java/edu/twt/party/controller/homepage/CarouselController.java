package edu.twt.party.controller.homepage;


import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.homepage.Carousel;
import edu.twt.party.service.homepage.CarouselService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Tag(name = "轮播图管理")
public class CarouselController {

    @Resource
    CarouselService carouselService;

    @GetMapping("/api/carousel")
    @Operation(summary = "获取轮播图")
    public ResponseHelper<List<Carousel>> getCarousel() {
        return new ResponseHelper<>(carouselService.getCarousel());
    }

    @PostMapping("/api/carousel")
    @Operation(summary = "在列表头部添加轮播图(图片路径)")
    public ResponseHelper<Boolean> addCarousel(@RequestParam("fileId") @Parameter(description = "轮播图对应的资源库中id") Integer fileId,
                                                                            @RequestParam("link") @Parameter(description = "轮播图对应的跳转链接") String link,
                                                                            @RequestParam("title") @Parameter(description = "轮播图名称") String title,
                                                                            @RequestParam("picturePath") @Parameter(description = "对应图片路径") String picturePath) {
        return new ResponseHelper<>(carouselService.addCarousel(fileId, link, title, picturePath));
    }

//    @PostMapping("/api/carousel/picture")
//    @Operation(summary = "在最后添加轮播图(上传图片)")
//    public ResponseHelper<Boolean> addCarousel(@RequestParam("fileId") @Parameter(description = "轮播图对应的文件在资源库中的id") Integer fileId,
//                                               @RequestParam("title") @Parameter(description = "轮播图名称") String title,
//                                               @RequestParam("pictureFile") @Parameter(description = "对应图片文件") MultipartFile multipartFile) {
//        return new ResponseHelper<>(carouselService.addCarousel(fileId, title, multipartFile));
//    }

    @PostMapping("/api/carousel/alter")
    @Operation(summary = "修改轮播图")
    public ResponseHelper<Boolean> alterCarousel(@RequestParam("fileId") @Parameter(description = "修改后对应文件的资源库id") Integer fileId,
                                                                             @RequestParam("carouselId") @Parameter(description = "需修改的轮播图的id") Integer carouselId,
                                                                              @RequestParam("newLink") @Parameter(description = "修改后的跳转链接") String newLink,
                                                                              @RequestParam("newTitle") @Parameter(description = "修改后的题目") String newTitle,
                                                                              @RequestParam("newPicPath") @Parameter(description = "修改后的图片路径") String newPicPath) {
        return new ResponseHelper<>(carouselService.alterCarousel(fileId, carouselId, newLink, newTitle, newPicPath));
    }

    @PostMapping("/api/carousel/top")
    @Operation(summary = "置顶轮播图")
    public ResponseHelper<Boolean> makeTop(@RequestParam("carouselId") @Parameter(description = "需置顶的轮播图id") Integer carouselId) {
        return new ResponseHelper<>(carouselService.makeTop(carouselId));
    }

    @PostMapping("/api/carousel/delete")
    @Operation(summary = "删除轮播图")
    public ResponseHelper<Boolean> deleteCarousel(@RequestParam("carouselId") @Parameter(description = "需删除的轮播图id") Integer carouselId) {
        return new ResponseHelper<>(carouselService.deleteCarousel(carouselId));
    }
}
