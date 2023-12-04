package edu.twt.party.controller;

import co.elastic.clients.elasticsearch.core.search.Hit;
import edu.twt.party.annotation.JwtToken;
import edu.twt.party.constant.RoleType;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.service.OperationRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName: OperationRecordController
 * @Description:
 * @Author: 过河卒
 * @Date: 2023/3/4 20:49
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/operaionRecord")
@Tag(name = "操作记录")
@Slf4j
public class OperationRecordController {


    @Resource
    OperationRecordService operationRecordService;

    @GetMapping("/num")
    @Operation(summary = "查询某个管理员的操作记录,查询条数上限默认为20")
    @JwtToken(roles = RoleType.ROOT)

    public ResponseHelper<Object> queryOperationByManagerNum(

            @RequestHeader String token,
            String managerNum,
            @RequestParam(required = false) Integer limit){
        try {
            List<edu.twt.party.pojo.Operation> list = operationRecordService.exactSearch("operator",managerNum);

            list = list.subList(0, Math.min(limit==null?20:limit,list.size()));
            return new ResponseHelper<>(list.toString());
        }catch (Exception e){
            log.error("queryOperationByManagerNum error ",e);
            return ResponseHelper.error();
        }

    }
}
