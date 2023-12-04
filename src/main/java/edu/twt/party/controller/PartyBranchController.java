package edu.twt.party.controller;

import edu.twt.party.annotation.JwtToken;
import edu.twt.party.constant.RoleType;
import edu.twt.party.dao.TwtPartybranchMapper;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.partyBranch.BatchUpdateBranchReq;
import edu.twt.party.pojo.partyBranch.PartyBranchCreateReq;
import edu.twt.party.pojo.partyBranch.PartyBranchGroupVo;
import edu.twt.party.pojo.partyBranch.TwtPartyBranchVo;
import edu.twt.party.pojo.student.BUserinfo;
import edu.twt.party.pojo.user.NameSno;
import edu.twt.party.service.impl.TwtPartyBranchService;
import edu.twt.party.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: ParyBranchController
 * @Description:
 * @Author: 过河卒
 * @Date: 2022/9/10 14:41
 * @Version: 1.0
 */
@RequestMapping("/api/partyBranch")
@RestController
@Tag(name = "党支部接口")
public class PartyBranchController {

    @Resource
    TwtPartyBranchService twtPartyBranchService;

    @Resource
    TwtPartybranchMapper twtPartybranchMapper;

    /**
     *
     * 两种方案，一种是b_userinfo里面不存储党支部，每次用的时候去student_info表里面拿
     * 另外一种方案是每次有变动的时候在b_userinfo里面也做修改，保证两边的一致性
     * 党支部里面响应的人数也需要修改
     * 触发器可以考虑
     * 但不知道是否会较大程度影响效率
     *
     * 目前使用方案2,使用触发器来同步
     * @param targetBranchId
     * @param sno
     * @return
     *
     * todo:权限确认
     */
    @PostMapping("/updateInfoById")
    @Operation(summary = "管理员更新某个人的所属党支部")
    @JwtToken(roles = {RoleType.ROOT,RoleType.COLLEGE_ADMIN})
    public ResponseHelper<Boolean> updatePartyBranchById(
            @RequestHeader String token,
            @RequestParam String sno,
            @RequestParam
            @Parameter(description = "当为0的时候，即将此人从党支部中删去") Integer targetBranchId){

        Integer collegeId = twtPartybranchMapper.selectVoById(targetBranchId).getPartybranchAcademy();
        JwtUtils.checkCollege(collegeId);

        return twtPartyBranchService.updateUserPartyBranch(sno,targetBranchId);
    }

    /**
     *
     * @param
     * @return
     */
    @PostMapping("/create")
    @Operation(summary = "管理员新建党支部")
    @JwtToken(roles = {RoleType.ROOT,RoleType.COLLEGE_ADMIN})
    public ResponseHelper<Integer> createPartyBranch(@RequestHeader String token,@RequestBody PartyBranchCreateReq req){
        JwtUtils.checkCollege(req.getPartybranchAcademy());
        return twtPartyBranchService.createPartyBranch(req);
    }

    @GetMapping
    @Operation(summary = "管理员获取党支部信息")
    @JwtToken(roles = {RoleType.ROOT,RoleType.COLLEGE_ADMIN})
    public ResponseHelper<TwtPartyBranchVo> getPartyBranchInfo(@RequestParam Integer branchId){
        Integer collegeId = twtPartybranchMapper.selectVoById(branchId).getPartybranchAcademy();
        JwtUtils.checkCollege(collegeId);
        return twtPartyBranchService.getTwtPartyBranchInfo(branchId);
    }

    /**
     * 更改党支部三个委员的信息
     * @param index 0为修改支书，1为修改组织委员，2为修改宣传委员
     * @param newStuNum
     * @return
     */
    @PostMapping("/update3Person")
    @Operation(summary = "管理员更改党支部三个委员的信息")
    @Parameters({
            @Parameter(name = "branchId",description = "修改的分支id"),
            @Parameter(name = "index",description = "0为修改支书，1为修改组织委员，2为修改宣传委员"),
            @Parameter(name = "newStuNum",description = "更改之后的学号,传\"\"是撤销原本的委员")})
    @JwtToken(roles = {RoleType.ROOT,RoleType.COLLEGE_ADMIN})
    public ResponseHelper<Boolean> update3Person(@RequestParam Integer branchId,
                                                 @RequestParam Integer index,
                                                 @RequestParam String newStuNum){

        Integer collegeId = twtPartybranchMapper.selectVoById(branchId).getPartybranchAcademy();
        JwtUtils.checkCollege(collegeId);
        return twtPartyBranchService.updatePartyBranch3Person(branchId,index,newStuNum);
    }

    /**
     * @param branchId
     * @return
     */
    @GetMapping("/memberList")
    @Operation(summary = "获取党支部的成员列表")
    @JwtToken(roles = {RoleType.ROOT,RoleType.COLLEGE_ADMIN})
    public ResponseHelper<List<BUserinfo>> getBranchPersonList(@RequestParam Integer branchId){

        Integer collegeId = twtPartybranchMapper.selectVoById(branchId).getPartybranchAcademy();
        JwtUtils.checkCollege(collegeId);
        return twtPartyBranchService.getBranchMemberList(branchId);
    }

    /**
     * todo: 还可能需要进行分页处理
     * @param collegeId
     * @param grade
     * @param type
     * @return
     */
    @GetMapping("/branchList")
    @Operation(summary = "筛选获得党支部列表",description = "如果查询条件为空，grade为长度为0的字符串 则该条件不进行筛选")
    @JwtToken(roles = {RoleType.ROOT,RoleType.COLLEGE_ADMIN})
    public ResponseHelper<List<PartyBranchGroupVo>> getBranchByAcademy( Integer collegeId, String  grade, String type){


        return twtPartyBranchService.getBranchList(collegeId,grade,type);
    }

    @DeleteMapping
    @Operation(summary = "删除党支部",description = "如果党支部不存在会返回false")
    @JwtToken(roles = {RoleType.ROOT,RoleType.COLLEGE_ADMIN})
    public  ResponseHelper<Boolean> delete(Integer branchId){
        Integer collegeId = twtPartybranchMapper.selectVoById(branchId).getPartybranchAcademy();
        JwtUtils.checkCollege(collegeId);
        return twtPartyBranchService.delete(branchId);
    }


    @PostMapping("/batch/update/branch")
    @Operation(summary = "批量更改党支部,如果想要删除,则targetBranchId为0")
    @JwtToken(roles = {RoleType.ROOT,RoleType.COLLEGE_ADMIN})
    public ResponseHelper<Object> batchUpdateBranch(@RequestBody BatchUpdateBranchReq batchUpdateBranchReq){
        Integer collegeId = twtPartybranchMapper.selectVoById(batchUpdateBranchReq.getTargetBranchId()).getPartybranchAcademy();
        JwtUtils.checkCollege(collegeId);
        return twtPartyBranchService.batchUpdateUserPartyBranch(batchUpdateBranchReq.getNameSnoList(), batchUpdateBranchReq.getTargetBranchId());
    }


    @PostMapping("/file")
    @Operation(summary = "上传excel文件,返回姓名-学号列表",description = "excel的格式必须第一行是name和sno")
    @JwtToken(roles = {RoleType.ROOT,RoleType.COLLEGE_ADMIN})
    public ResponseHelper<List<NameSno>> uploadFile(MultipartFile file){
        return twtPartyBranchService.analysisExcel(file);
    }

    @Deprecated
    @PostMapping("/kick-out")
    @Operation(summary = "将uidList中的人从指定的党支部中删除，后续需要加上权限管理")
    @JwtToken(roles = {RoleType.ROOT,RoleType.COLLEGE_ADMIN})
    public ResponseHelper<Boolean> kickOutStudents(List<Integer> uidList,Integer branchId){
        return twtPartyBranchService.kickOut(uidList,branchId);
    }

}
