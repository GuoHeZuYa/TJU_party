package edu.twt.party.controller;

import edu.twt.party.annotation.JwtToken;
import edu.twt.party.constant.RoleType;
import edu.twt.party.exception.LoginException;
import edu.twt.party.exception.PermissionException;
import edu.twt.party.helper.ResponseCode;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.manager.AuthorizeReq;
import edu.twt.party.pojo.user.AcntPass;
import edu.twt.party.service.BaseService;
import edu.twt.party.service.ManagerService;
import edu.twt.party.service.OperationRecordService;
import edu.twt.party.utils.HttpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/manager")
@Tag(name = "管理员")
public class ManagerController {

    public static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);
    @Resource
    ManagerService managerService;

    @Resource
    OperationRecordService operationRecordService;


    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public ResponseHelper<Boolean> login(@RequestBody AcntPass acntPass, HttpServletResponse response){
        try {

            String token = managerService.login(acntPass);
            response.setHeader(HttpUtils.TOKEN_HEADER,token);
            response.setHeader("Access-Control-Expose-Headers","token");

            //
            operationRecordService.log(acntPass.getAccount(),"登录");
            return new ResponseHelper<>(true);
        }catch (LoginException e){
            LOGGER.warn("wrong pass for account {}",acntPass.getAccount());
            return new ResponseHelper<>(ResponseCode.LOGIN_FAILED,false);
        }catch (PermissionException e){
            LOGGER.warn("no permission for {}",acntPass.getAccount());
            return new ResponseHelper<>(ResponseCode.NO_PERMISSION,false);
        } catch (Exception e) {
            LOGGER.error("something error when login,acount={}",acntPass.getAccount());
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/authorize")
    @Operation(summary = "给账号授予管理员权限")
    @JwtToken(roles = RoleType.ROOT)
    public ResponseHelper<Boolean> authorize(@RequestHeader String token, @RequestBody AuthorizeReq authorizeReq) throws Exception {
        Boolean ret = managerService.authorize(authorizeReq,token);
        return new ResponseHelper<>(ret);
    }

    @PostMapping("/test")
    @Operation(summary = "test")
    @JwtToken(roles = {RoleType.COLLEGE_ADMIN,RoleType.COLUMN_ADMIN})
    public ResponseHelper<Boolean> permissionTest(@RequestHeader String token){
        try {
            return new ResponseHelper<>(true);
        }catch (Exception e){
            return new ResponseHelper<>(ResponseCode.ERROR, false);
        }
    }


}
