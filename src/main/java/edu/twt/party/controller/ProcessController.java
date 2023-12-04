package edu.twt.party.controller;

import com.alibaba.fastjson.JSONObject;
import edu.twt.party.annotation.JwtToken;
import edu.twt.party.constant.RoleType;
import edu.twt.party.helper.ResponseCode;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.userProcess.MainProcessFilterResult;
import edu.twt.party.pojo.userProcess.ProcessNode;
import edu.twt.party.pojo.userProcess.UserProcess;
import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.service.process.ProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Tag(name = "入党流程")
public class ProcessController {
    @Autowired
    ProcessService processService;
    @GetMapping("/api/process/tree")
    public ResponseHelper<List<ProcessNode>> getProcessTree(){
        return new ResponseHelper(processService.getProcessTree());
    }
    @PostMapping("/api/process/{userId}/update")
    @Operation(summary = "修改单个用户入党进度")
    @JwtToken(roles = RoleType.SECRETARY)
    public ResponseHelper<Boolean> updateUserProcess(
            @PathVariable(name = "userId") Integer userId,
            @RequestParam(name = "processId") Integer processId,
            @RequestParam(name = "status") Integer status
    ){
        return new ResponseHelper<>(processService.updateUserProcess(userId,processId,status));
    }
    @PostMapping("/api/process/updateBatch")
    @JwtToken(roles = RoleType.SECRETARY)
    @Operation(summary = "修改多个用户入党进度",description = "userIds传json")
    public ResponseHelper<Boolean> updateProcessBatch(
            @RequestBody JSONObject body,
            HttpServletRequest request
    ){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int branchId = user.getPartybranchId();
        List<Integer> idList = body.getJSONArray("ids").toJavaList(Integer.class);
        List<Integer> processIdList = body.getJSONArray("processIds").toJavaList(Integer.class);
        List<Integer> statusList = body.getJSONArray("status").toJavaList(Integer.class);
        if(processService.isMemberInPartyBranch(idList,user.getPartybranchId())){
            return new ResponseHelper<>(processService.updateUsersProcess(idList,processIdList,statusList));
        }else {
            return new ResponseHelper<>(ResponseCode.FAILED,false);
        }
    }
    @GetMapping("/api/process/{userId}")
    @Operation(summary = "获取某个用户的入党进度")
    @JwtToken(roles = RoleType.SECRETARY)
    public ResponseHelper<List<UserProcess>> getUserProcesses(@PathVariable(name = "userId")Integer userId){
        return new ResponseHelper<>(processService.getUserProcessesById(userId));
    }
    @GetMapping("/api/process/filter/{mainProcess}")
    @JwtToken(roles = RoleType.SECRETARY)
    public ResponseHelper<List<MainProcessFilterResult>> filterMainProcess(@PathVariable(name = "mainProcess") int mainProcess,HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int branchId = user.getPartybranchId();
        return new ResponseHelper<>(processService.filterMainProcess(branchId,mainProcess));
    }

    @GetMapping("/api/process/userList/{collegeId}/{grade}/{type}")
    @JwtToken(roles = RoleType.SECRETARY)
    public ResponseHelper<List<Integer>> getUserIdsByCollegeIdAndGrade(@PathVariable(name = "collegeId")Integer collegeId,
                                                                       @PathVariable(name = "grade")Integer grade,
                                                                       @PathVariable(name = "type")Integer type){
        return new ResponseHelper<>(processService.getUsersIdByCollegeIdAndGrade(collegeId, grade,type));
    }

    @GetMapping("/api/process/my")
    @Operation(summary = "获取某个用户的入党进度")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<List<UserProcess>> getUserProcessesByToken(HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int userId = user.getId();
        return new ResponseHelper<>(processService.getUserProcessesById(userId));
    }
}
