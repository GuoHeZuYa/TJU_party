package edu.twt.party.controller.library;

import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.library.Column;
import edu.twt.party.pojo.library.ColumnInfo;
import edu.twt.party.service.library.ColumnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xsr
 */
@RestController
@Tag(name = "栏目管理")
public class ColumnController {
    @Resource
    ColumnService columnService;

    @PostMapping("/api/column")
    @Operation(summary = "添加新的栏目")
    public ResponseHelper<Boolean> addNewColumn(@RequestParam("columnName") @Parameter(description = "栏目名称") String ColumnName,
                                                @RequestParam("position") @Parameter(description = "栏目位置") Integer position) {
        return new ResponseHelper<>(columnService.addNewColumn(ColumnName, position));
    }

    @PostMapping("api/column/delete")
    @Operation(summary = "删除栏目")
    public ResponseHelper<Boolean> deleteColumn(@RequestParam("columnId") @Parameter(description = "需删除的栏目id") Integer columnId){
        return new ResponseHelper<>(columnService.deleteColumn(columnId));
    }

    @PostMapping("api/column/alter")
    @Operation(summary = "修改栏目名称")
    public ResponseHelper<Boolean> alterColumnName(@RequestParam("newColumnName") @Parameter(description = "新的栏目名称") String newColumnName,
                                                   @RequestParam("columnId") @Parameter(description = "需修改的栏目id") Integer columnId) {
        return new ResponseHelper<>(columnService.alterColumnName(newColumnName, columnId));
    }

    @PostMapping("/api/column/position")
    @Operation(summary = "修改栏目位置")
    public ResponseHelper<Boolean> alterColumnPosition(@RequestParam("columnId") @Parameter(description = "需修改的栏目id") Integer columnId,
                                                       @RequestParam("newPosition") @Parameter(description = "新的栏目位置") Integer newPosition){
        return new ResponseHelper<>(columnService.alterColumnPosition(columnId, newPosition));
    }

    @GetMapping("/api/column")
    @Operation(summary = "获取所有栏目")
    public ResponseHelper<List<ColumnInfo>> getAllColumn() {
        return new ResponseHelper<>(columnService.getColumns());
    }
}
