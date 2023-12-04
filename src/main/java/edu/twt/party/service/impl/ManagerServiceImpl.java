package edu.twt.party.service.impl;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import edu.twt.party.constant.RoleType;
import edu.twt.party.dao.TwtManagerDao;
import edu.twt.party.exception.PermissionException;
import edu.twt.party.helper.ResponseCode;
import edu.twt.party.pojo.manager.AuthorizeReq;
import edu.twt.party.pojo.manager.TwtManager;
import edu.twt.party.pojo.user.AcntPass;
import edu.twt.party.service.ManagerService;
import edu.twt.party.service.OperationRecordService;
import edu.twt.party.utils.JwtUtils;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Resource
    LoginService loginService;

    @Resource
    TwtManagerDao twtManagerDao;

    @Resource
    OperationRecordService operationRecordService;




    private TwtManager getRole(String account)throws PermissionException {
        LambdaQueryChainWrapper<TwtManager> queryWrapper = new LambdaQueryChainWrapper<>(twtManagerDao);

        //todo: 如果管理员不止一个角色，会error，是否设置学号唯一？
        TwtManager twtManager =
                queryWrapper.eq(TwtManager::getManagerName,account).one();


        //todo:?
        if(twtManager.getRole().equals(RoleType.COLLEGE_ADMIN)){

        }

        if (twtManager==null) {
            throw new PermissionException(ResponseCode.NO_PERMISSION);
        }

        return twtManager;

    }

    @Override
    public String login(AcntPass acntPass) {
        loginService.login(acntPass);
        String account = acntPass.getAccount();
        TwtManager manager = getRole(account);
        Integer collegeId = 0;
        String columnId = "";
        if(manager.getRole().equals(RoleType.COLLEGE_ADMIN)){
            collegeId = manager.getManagerAcademy();
        }else if (manager.getRole().equals(RoleType.COLUMN_ADMIN)){
            collegeId = manager.getManagerAcademy();
            columnId = manager.getManagerColumngrant();
        }
        String token =  JwtUtils.genJwtToken(manager.getManagerId(),account,manager.getRole().getTypeNum(),collegeId,columnId);
        return token;
    }

    @Override
    public Boolean authorize(AuthorizeReq authorizeReq,String token) {
        try {

            //todo: 检查账号是否存在


            TwtManager twtManager = new TwtManager();
            twtManager
                    .setManagerAcademy(authorizeReq.getTargetAcademy())
                    .setManagerName(authorizeReq.getTargetNum())
                    .setRole(authorizeReq.getRoleType())
                    .setManagerColumngrant(authorizeReq.getManagerColumn());
            twtManagerDao.insert(twtManager);

            String operatorNum = JwtUtils.AnalysisUserNumber(token);
            String targetNum = authorizeReq.getTargetNum();
            operationRecordService.log(operatorNum,operatorNum+" authorize "+targetNum+" to "+authorizeReq);

            return true;
        }catch (Exception e){
            LOGGER.error("fail to authorize",e);
            return false;
        }

    }
}
