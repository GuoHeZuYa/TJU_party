package edu.twt.party.controller;


import edu.twt.party.constant.RoleType;
import edu.twt.party.helper.ResponseCode;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.classPojo.ClassVo;
import edu.twt.party.pojo.student.UserInfoBasic;
import edu.twt.party.pojo.student.StudentGroupByClass;
import edu.twt.party.pojo.student.StudentVo;
import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.pojo.user.AcntPass;
import edu.twt.party.service.TwtStudentInfoService;
import edu.twt.party.utils.HttpUtils;
import edu.twt.party.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "用户")
public class UserController {


    @Resource
    TwtStudentInfoService twtStudentInfoService;

    // @PostMapping("/update/progress")
    // @JwtToken(roles = Role.Student)
    // public ResponseHelper<Boolean> updateProgress(){
    //     return twtStudentInfoService.
    // }

    @PostMapping("/user/login")
    @Operation(summary = "登录",description = "普通用户登录和支书登录都是这个接口,登录成功返回的data>=0,0为普通用户,10为党支书")
    public ResponseHelper<Integer> login(@RequestBody AcntPass acntPass, HttpServletResponse response)  {
        try {
            String token = twtStudentInfoService.login(acntPass);
            response.setHeader("Access-Control-Expose-Headers","token");
            response.setHeader(HttpUtils.TOKEN_HEADER,token);
            return new ResponseHelper<>(JwtUtils.getUserType(token));
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseHelper<>(ResponseCode.LOGIN_FAILED,-1);
        }
    }


    @GetMapping("/user")
    @Operation(summary = "获取自己的信息")
    public ResponseHelper<TwtStudentInfo> getInfo(@RequestHeader String token){
        return new ResponseHelper<>(twtStudentInfoService.getMyInfo(token));
    }


    @GetMapping("/user/college/grade")
    @Operation(summary = "根据学院和年级查询学生列表，按照班级进行分组")
    public ResponseHelper<List<StudentVo>> listByCollegeAndGrade(Integer collegeId,Integer grade){
        return twtStudentInfoService.getInfoList(collegeId,grade);
    }

    @GetMapping("/class/list")
    @Operation(summary = "根据学院和年级查询班级列表")
    public ResponseHelper<List<ClassVo>> listClassVo(Integer collegeId,Integer grade){
        return twtStudentInfoService.listClass(collegeId,grade);
    }


    @PostMapping("/users/classList")
    @Operation(summary = "根据班级Id列表查询user,按班级为单位进行展示")
    public ResponseHelper<List<StudentGroupByClass>> listByClass(@RequestParam List<Integer> classIdList){
        return twtStudentInfoService.listStudentByClass(classIdList);
    }


    @GetMapping("/user/{sno}")
    @Operation(summary = "根据学号获取信息")
    public ResponseHelper<StudentVo> getInfoBySno(@PathVariable String sno){
        return twtStudentInfoService.getInfoBySno(sno);
    }


    @PostMapping("/names")
    @Operation(summary = "获取用户名列表")
    public ResponseHelper<List<UserInfoBasic>> getUnameList(@RequestParam List<String> snoList){
        return twtStudentInfoService.getUnameList(snoList);
    }


}
