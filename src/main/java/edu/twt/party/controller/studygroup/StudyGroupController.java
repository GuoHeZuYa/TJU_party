package edu.twt.party.controller.studygroup;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import edu.twt.party.annotation.JwtToken;
import edu.twt.party.constant.RoleType;
import edu.twt.party.helper.ResponseCode;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.pojo.studygroup.StudyGroup;
import edu.twt.party.service.studygroup.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StudyGroupController {
    @Autowired
    StudyGroupService studyGroupService;

    @GetMapping("/api/studyGroup/myBranch")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<List<StudyGroup>> getStudyGroupInMyBranch(HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int branchId = user.getPartybranchId();
        return new ResponseHelper<>(studyGroupService.getStudyGroupByBranchId(branchId));
    }
    @GetMapping("/api/studyGroup/{id}")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<StudyGroup> getStudyGroupByIdInMyBranch(@PathVariable int id,HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int branchId = user.getPartybranchId();
        StudyGroup studyGroup = studyGroupService.getStudyGroupById(id);
        if(studyGroup!=null && studyGroup.getPartyBranchId() == user.getPartybranchId()){
            return new ResponseHelper<>(studyGroup);
        }else {
            return new ResponseHelper<>(ResponseCode.FAILED,false,"无权限");
        }
    }
    @PostMapping("/api/studyGroup/{id}")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<Boolean> updateStudyGroupById(@PathVariable int id, @RequestBody JSONObject body,HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int branchId = user.getPartybranchId();
        StudyGroup studyGroup = studyGroupService.getStudyGroupById(id);
        if(studyGroup!=null && studyGroup.getPartyBranchId() == user.getPartybranchId()){
            String name = body.getStr("name","");
            if("".equals(name)){
               return new ResponseHelper<>(ResponseCode.FAILED,false,"名称不能为空");
            }else {
                return new ResponseHelper<>(studyGroupService.updateStudyGroupById(id,name));
            }
        }else {
            return new ResponseHelper<>(ResponseCode.FAILED,false,"无权限");
        }
    }

    @GetMapping("/api/studyGroup/{id}/delete")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<Boolean> deleteStudyGroupByIdInMyBranch(@PathVariable int id,HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int branchId = user.getPartybranchId();
        StudyGroup studyGroup = studyGroupService.getStudyGroupById(id);
        if(studyGroup!=null && studyGroup.getPartyBranchId() == user.getPartybranchId()){
            return new ResponseHelper<>(studyGroupService.deleteStudyGroupById(branchId));
        }else {
            return new ResponseHelper<>(ResponseCode.FAILED,false,"无权限");
        }
    }
    @GetMapping("/api/studyGroup/{id}/member")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<List<TwtStudentInfo>> getStudyGroupMember(@PathVariable int id,HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int branchId = user.getPartybranchId();
        if(id == -1){
            return new ResponseHelper<>(studyGroupService.getStudyGroupMemberUnAlloc(branchId));
        }else{
            StudyGroup studyGroup = studyGroupService.getStudyGroupById(id);
            if(studyGroup!=null && studyGroup.getPartyBranchId() == branchId){
                return new ResponseHelper<>(studyGroupService.getStudyGroupMember(id));
            }else {
                return new ResponseHelper<>(ResponseCode.FAILED,false,"无权限");
            }
        }
    }
    @PostMapping("/api/studyGroup/new")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<Integer> createStudyGroup(HttpServletRequest request, @RequestBody JSONObject body){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int branchId = user.getPartybranchId();
        String name = body.getStr("name","");
        if(name.equals("")){
            return new ResponseHelper<>(ResponseCode.FAILED,false,"名称不能为空");
        }
        return new ResponseHelper<>(studyGroupService.createStudyGroup(name,branchId));
    }
    @PostMapping("/api/studyGroup/{id}/addMember")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<Boolean> moveMember(@PathVariable int id,HttpServletRequest request,@RequestBody JSONArray body){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        List<Integer> userIds = body.toList(Integer.class);
        int branchId = user.getPartybranchId();
        StudyGroup studyGroup = studyGroupService.getStudyGroupById(id);
        if(studyGroup!=null && studyGroup.getPartyBranchId()==branchId){
            return new ResponseHelper<>(studyGroupService.moveStudyGroupMember(userIds,id));
        }else {
            return new ResponseHelper<>(ResponseCode.FAILED,false,"无权限");
        }

    }
}
