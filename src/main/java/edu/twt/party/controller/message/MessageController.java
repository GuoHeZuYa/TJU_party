package edu.twt.party.controller.message;

import edu.twt.party.exception.APIException;
import edu.twt.party.helper.Page;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.message.*;
import edu.twt.party.service.message.AdminMessageService;
import edu.twt.party.service.message.MessageExcelService;
import edu.twt.party.service.message.StuMessageService;
import edu.twt.party.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Tag(name = "站内信")
@RestController
public class MessageController {

    @Resource
    AdminMessageService adminMessageService;

    @Resource
    StuMessageService stuMessageService;

    @Resource
    MessageExcelService excelService;

    @PostMapping("/api/message/stu")
    @Operation(summary = "普通用户发送站内信")
    public ResponseHelper<Boolean> sendStuMessage(@RequestParam("receiverId") @Parameter(description = "发送对象，管理员表中的管理员类型,1院管，2超管，0支书，支书暂未实现") int receiverId,
                                                  @RequestHeader @Parameter(description = "token") String token,
                                                  @RequestBody MessageUnit messageUnit) {
        Integer uid = JwtUtils.getUid(token);
        return new ResponseHelper<>(stuMessageService.sendStuMessage(receiverId, uid, messageUnit));
    }

    @GetMapping("/api/message/stu/sent")
    @Operation(summary = "查看普通用户已发送的站内信")
    public ResponseHelper<List<StuMessage>> getSentStuMessage(@RequestHeader @Parameter(description = "token") String token,
                                                              @Nullable @RequestParam Integer page) {
        Integer uid = JwtUtils.getUid(token);
        int p = page!=null?page:0;
        int pageCount = 0;
        List<StuMessage> messages =stuMessageService.getSentStuMessage(uid);
        if(messages.size()>0){
            pageCount = (messages.size()-1)/15+1;
        }
        return new ResponseHelper<>(new Page(pageCount, messages.size()),messages.subList(p*15,p*15+15));
    }

    @GetMapping("/api/message/stu")
    @Operation(summary = "查看普通用户收到的站内信")
    public ResponseHelper<List<AdminMessage>> getAdminMessage(@RequestHeader @Parameter(description = "token") String token,
                                                              @Nullable @RequestParam Integer page) {
        Integer uid = JwtUtils.getUid(token);
        int p = page!=null?page:0;
        int pageCount = 0;
        List<AdminMessage> messages = adminMessageService.getAdminMessage(uid);
        if(messages.size()>0){
            pageCount = (messages.size()-1)/15+1;
        }
        return new ResponseHelper<>(new Page(pageCount, messages.size()),messages.subList(p*15,p*15+15));
    }

    @PostMapping("/api/message/stu/read")
    @Operation(summary = "将普通用户收到的站内信设置为已读")
    public ResponseHelper<Boolean> setAdminMessageReaded(@RequestParam("messageInfoId") @Parameter(description = "站内信id") int messageInfoId) {
        return new ResponseHelper<>(adminMessageService.setAdminMessageReaded(messageInfoId));
    }

    @PostMapping("/api/message/admin")
    @Operation(summary = "管理员发送站内信（非回复，单独发送）")
    public ResponseHelper<Boolean> sendAdminMessage(@RequestParam("studentNumber") @Parameter(description = "发送对象的学号") String sno,
                                                    @RequestHeader @Parameter(description = "token") String token,
                                                    @RequestBody MessageUnit messageUnit) {
        Integer uid = JwtUtils.getUid(token);
        return new ResponseHelper<>(adminMessageService.sendAdminMessage(sno, uid, messageUnit));
    }

    @PostMapping("/api/message/admin/batch")
    @Operation(summary = "管理员发送站内信（批量发送）")
    public ResponseHelper<Integer> sendAdminMessage(@RequestParam("receiver") @Parameter(description = "接收信息的对象，0为发送给所有用户，正数为要发送的学院代码,-1支书,-2组织,-3宣传") int receiver,
                                                    @RequestHeader @Parameter(description = "token") String token,
                                                    @RequestBody MessageUnit messageUnit) {
        Integer uid = JwtUtils.getUid(token);
        return new ResponseHelper<>(adminMessageService.sendAdminMessage(receiver, uid, messageUnit));
    }

    @GetMapping("/api/message/admin/sent")
    @Operation(summary = "查看管理员已发送的信件")
    public ResponseHelper<List<AdminMessage>> getSentAdminMessage(@RequestHeader @Parameter(description = "token") String token,
                                                                  @Nullable @RequestParam("sortBy") @Parameter(description = "排序方式 可选 send_time status null") String sort,
                                                                  @Nullable @RequestParam("desc") @Parameter(description = "排序方式 正序0 倒序1 null")Boolean desc,
                                                                  @Nullable @RequestParam("status") @Parameter(description = "状态筛选 0 1 null")Integer status,
                                                                  @Nullable @RequestParam("page") @Parameter(description = "页码")Integer page) {
        Integer uid = JwtUtils.getUid(token);
        Integer startNum = 0;
        if(page==null || page < 1){
            startNum = 0;
        }else {
            startNum = (page-1)*15;
        }
        Integer totalCount = adminMessageService.getSentAdminMsgCount(uid,status);
        return new ResponseHelper<>(new Page((totalCount-1)/15+1,totalCount),adminMessageService.getSentAdminMessage(uid,sort,desc,status,startNum));
    }

    @PostMapping("/api/message/admin/reply")
    @Operation(summary = "管理员发送站内信（回复）")
    public ResponseHelper<Boolean> sendAdminReply(@RequestParam("replyMessageInfoId") @Parameter(description = "所回复的信件的id") int replyMessageId,
                                                  @RequestHeader @Parameter(description = "token") String token,
                                                  @RequestBody MessageUnit messageUnit) {
        Integer uid = JwtUtils.getUid(token);
        return new ResponseHelper<>(adminMessageService.sengAdminReply(replyMessageId, uid, messageUnit));
    }

    @GetMapping("/api/message/admin")
    @Operation(summary = "查看管理员收到的站内信")
    public ResponseHelper<List<StuMessage>> getStuMessage(@RequestHeader @Parameter(description = "token") String token,
                                                          @Nullable @RequestParam("sortBy") @Parameter(description = "排序方式 可选 send_time status null(不传这个参数)") String sort,
                                                          @Nullable @RequestParam("desc") @Parameter(description = "排序方式 正序0 倒序1 null")Boolean desc,
                                                          @Nullable @RequestParam("status") @Parameter(description = "状态筛选 0 1 null")Integer status,
                                                          @Nullable @RequestParam("page")@Parameter(description = "页码")Integer page) {
        Integer uid = JwtUtils.getUid(token);

        Integer startNum = 0;
        if(page==null || page < 1){
            startNum = 0;
        }else {
            startNum = (page-1)*15;
        }
        Integer totalCount = stuMessageService.getStuMessageCount(uid,status);
        return new ResponseHelper<>(new Page((totalCount-1)/15+1,totalCount),stuMessageService.getStuMessage(uid,sort,desc,status,startNum));
    }

    @PostMapping("/api/message/importExcel")
    @Operation(summary = "管理员确认导入Excel")
    public ResponseHelper<HashMap> importExcel(@RequestBody MultipartFile file){
        try {
            return new ResponseHelper<>(excelService.parseExcel(file));
        }catch (IOException e){
            throw new APIException(e.toString());
        }
    }

    @PostMapping("/api/message/admin/excel/{importId}/commit")
    @Operation(summary = "管理员确认发送Excel导入的站内信")
    public ResponseHelper<HashMap<String,Object>> sendAdminMessageExcel(@PathVariable @Parameter(description = "上传Excel时返回的importId") Integer importId,
                                                    @RequestHeader @Parameter(description = "token") String token,
                                                    @RequestBody MessageUnit messageUnit) {
        Integer uid = JwtUtils.getUid(token);
        return new ResponseHelper<>(excelService.commitMessage(uid, importId, messageUnit));
    }
    @PostMapping("/api/message/admin/excel/{id}/modify")
    @Operation(summary = "管理员修改已经上传的Excel数据")
    public ResponseHelper<Boolean> adminModifyExcel(@PathVariable @Parameter(description = "上传Excel时返回的itemId") Integer id,
                                                    @RequestHeader @Parameter(description = "token") String token,
                                                    @RequestParam @Parameter(description = "学号")String sno,
                                                    @RequestParam @Parameter(description = "姓名")String name) {
        return new ResponseHelper<>(excelService.modifyExcelItem(id,name,sno));
    }

}
