package edu.twt.party.controller.homepage;

import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.service.homepage.RecommendListService;
import edu.twt.party.pojo.homepage.RecommendListUnit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Tag(name = "推荐列表管理")
public class RecommendListController {
    @Resource
    RecommendListService recommendListService;

    @GetMapping("/api/recommendList")
    @Operation(summary = "获取推荐列表")
    public ResponseHelper<List<RecommendListUnit>> getRecommendList() {
        return new ResponseHelper<>(recommendListService.getRecommendList());
    }

    @PostMapping("/api/recommendList")
    @Operation(summary = "向推荐列表中添加文件")
    public ResponseHelper<Boolean> addRecommendedFile(@RequestParam("fileId") @Parameter(description = "文件在资源库中的id") Integer fileId,
                                                      @RequestParam("position") @Parameter(description = "文件在列表中的位置（1~n表示）") Integer position) {
        return new ResponseHelper<>(recommendListService.addRecommendedFile(fileId, position));
    }

    @PostMapping("/api/recommendList/head")
    @Operation(summary = "向推荐列表的头部添加文件")
    public ResponseHelper<Boolean> addRecommendedFileToHead(@RequestParam("fileId") @Parameter(description = "文件在资源库中的id") Integer fileId) {
        return new ResponseHelper<>(recommendListService.addRecommendFileToHead(fileId));
    }

    @PostMapping("/api/recommendList/top")
    @Operation(summary = "置顶文件")
    public ResponseHelper<Boolean> makeTop(@RequestParam("recommendedFileId") @Parameter(description = "要置顶的文件在推荐列表中的id") Integer recommendedFileId) {
        return new ResponseHelper<>(recommendListService.makeTop(recommendedFileId));
    }

    @PostMapping("/api/recommendList/order")
    @Operation(summary = "调整推荐列表中文件的顺序")
    public ResponseHelper<Boolean> alterRecommendListOrder(@RequestParam("recommendedFileId") @Parameter(description = "文件在推荐列表中的id") Integer recommendedFileId,
                                                           @RequestParam("newPosition") @Parameter(description = "新的文件位置") Integer newPosition) {
        return new ResponseHelper<>(recommendListService.alterRecommendListOrder(recommendedFileId, newPosition));
    }

    @PostMapping("/api/recommendList/alter")
    @Operation(summary = "修改推荐列表中的文件")
    public ResponseHelper<Boolean> alterRecommendFile(@RequestParam("recommendedFileId") @Parameter(description = "文件在推荐列表中的id") Integer recommendedFileId,
                                                                                        @RequestParam("newFileId") @Parameter(description = "新的文件在资源库中的id") Integer newId) {
        return new ResponseHelper<>(recommendListService.alterRecommendFile(recommendedFileId, newId));
    }

    @PostMapping("/api/recommendedList/delete")
    @Operation(summary = "删除推荐列表中的文件")
    public ResponseHelper<Boolean> deleteRecommendFile(@RequestParam("recommendedFileId") @Parameter(description = "文件在推荐列表中的id") Integer recommendedFileId) {
        return new ResponseHelper<>(recommendListService.deleteRecommendFile(recommendedFileId));
    }
}
