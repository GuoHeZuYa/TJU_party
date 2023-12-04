package edu.twt.party.controller.homepage;

import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.service.homepage.NoticeService;
import edu.twt.party.pojo.homepage.Notice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Tag(name = "近期通知管理")
public class NoticeController {

    @Resource
    NoticeService noticeService;

    @GetMapping("/api/notice")
    @Operation(summary = "获取近期通知列表")
    public ResponseHelper<List<Notice>> getNoticeList() {
        return new ResponseHelper<>(noticeService.getNotice());
    }

    @PostMapping("/api/notice")
    @Operation(summary = "在列表头部添加通知")
    public ResponseHelper<Boolean> addNoticeToHead(@RequestParam("text") @Parameter(description = "通知内容") String text,
                                                   @RequestParam("title") @Parameter(description = "通知标题") String title,
                                                   @RequestParam("path") @Parameter(description = "附件路径（可以为空）") String path) {
        return new ResponseHelper<>(noticeService.addNoticeToHead(text, title, path));
    }

    @PostMapping("/api/notice/delete")
    @Operation(summary = "删除通知")
    public ResponseHelper<Boolean> deleteNotice(@RequestParam("libraryFileId") @Parameter(description = "通知对应的资源库id") Integer libraryFileId) {
        return new ResponseHelper<>(noticeService.deleteNotice(libraryFileId));
    }

    @PostMapping("/api/notice/top")
    @Operation(summary = "将通知置顶")
    public ResponseHelper<Boolean> makeTop(@RequestParam("libraryFileId") @Parameter(description = "通知对应的资源库id") Integer libraryFileId) {
        return new ResponseHelper<>(noticeService.makeTop(libraryFileId));
    }

    @PostMapping("/api/notice.alter")
    @Operation(summary = "修改通知内容")
    public ResponseHelper<Boolean> alterNotice(@RequestParam("libraryFileId") @Parameter(description = "要修改的通知对应的资源库id") Integer libraryFileId,
                                               @RequestParam("title") @Parameter(description = "修改后的通知标题") String title,
                                               @RequestParam("text") @Parameter(description = "修改后的通知内容") String text,
                                               @RequestParam("path") @Parameter(description = "修改后的通知附件路径") String path) {
        return new ResponseHelper<>(noticeService.alterNotice(libraryFileId, text, title, path));
    }
}
