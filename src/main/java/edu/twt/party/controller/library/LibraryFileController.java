package edu.twt.party.controller.library;

import edu.twt.party.helper.Page;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.library.LibraryFile;
import edu.twt.party.service.library.LibraryFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.List;

@RestController
@Tag(name = "资源库管理")
public class LibraryFileController {
    @Resource
    LibraryFileService libraryFileService;

    @PostMapping("/api/library")
    @Operation(summary = "向资源库中添加文件")
    public ResponseHelper<Boolean> addLibraryFile(@RequestParam("text") @Parameter(description = "文章内容") String text,
                                                  @RequestParam("columnId") @Parameter(description = "要添加的文件所属的栏目id") Integer columnId,
                                                  @RequestParam("path") @Parameter(description = "文件上传后的路径(先调用文件上传接口)") String path,
                                                  @RequestParam("title") @Parameter(description = "文件标题") String title) {
        return new ResponseHelper<>(libraryFileService.addLibraryFile(text, columnId, path, title));
    }

    @GetMapping("/api/library/column")
    @Operation(summary = "获取某一栏目下的资源库文件(按照文件修改时间排序)")
    public ResponseHelper<List<LibraryFile>> getColumnFile(@RequestParam("columnId") @Parameter(description = "要查询的栏目的id") Integer columnId,
                                                           @Nullable @RequestParam("page") @Parameter(description = "页码") Integer page)
    {
        int startNum = 0;
        if(page==null||page < 1){
            startNum = 0;
        }else {
            startNum = (page-1)*12;
        }
        int totalCount = libraryFileService.getLibraryFileCount(columnId);
        //简单分个页
        return new ResponseHelper<>(new Page((totalCount-1)/12+1,totalCount),libraryFileService.getColumnFile(columnId,startNum));
    }

    @PostMapping("/api/library/delete")
    @Operation(summary = "删除资源库文件")
    public ResponseHelper<Boolean> deleteFile(@RequestParam("fileId") @Parameter(description = "需删除的文件在资源库中的id") Integer fileId) {
        return new ResponseHelper<>(libraryFileService.delete(fileId));
    }

    @GetMapping("/api/library")
    @Operation(summary = "通过资源库id获取文件")
    public  ResponseHelper<LibraryFile> getFileById(@RequestParam("fileId") @Parameter(description = "文件在资源库中的id") Integer fileId) {
        return new ResponseHelper<>(libraryFileService.getFileById(fileId));
    }

    @GetMapping("/api/library/search")
    @Operation(summary = "搜索(按照文件修改时间排序)")
    public ResponseHelper<List<LibraryFile>> search(@RequestParam("name") @Parameter(description = "搜索内容") String name) {
        return new ResponseHelper<>(libraryFileService.search(name));
    }

    @PostMapping("/api/library/alter")
    @Operation(summary = "修改资源库文件")
    public ResponseHelper<Boolean> alterLibraryFile(@RequestParam("libraryFileId") @Parameter(description = "要修改的资源库文件id") Integer libraryFileId,
                                                    @RequestParam("text") @Parameter(description = "修改后的文章内容") String text,
                                                    @RequestParam("path") @Parameter(description = "修改后的附件路径") String path,
                                                    @RequestParam("title") @Parameter(description = "修改后的文章标题") String title) {
        return new ResponseHelper<>(libraryFileService.alterLibraryFile(libraryFileId, text, path, title));
    }

}
