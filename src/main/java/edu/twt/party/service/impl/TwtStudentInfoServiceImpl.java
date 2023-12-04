package edu.twt.party.service.impl;

import edu.twt.party.constant.RoleType;
import edu.twt.party.dao.*;
import edu.twt.party.exception.LoginException;
import edu.twt.party.helper.ResponseCode;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.classPojo.ClassVo;
import edu.twt.party.pojo.partyBranch.TwtPartybranch;
import edu.twt.party.pojo.student.UserInfoBasic;
import edu.twt.party.pojo.student.StudentGroupByClass;
import edu.twt.party.pojo.student.StudentVo;
import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.pojo.user.AcntPass;
import edu.twt.party.service.TwtStudentInfoService;
import edu.twt.party.utils.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Throwable.class)
public class TwtStudentInfoServiceImpl implements TwtStudentInfoService {

    @Resource
    RestTemplate restTemplate;

    @Resource
    TwtStudentInfoMapper twtStudentInfoMapper;

    @Resource
    BUserinfoMapper bUserinfoMapper;

    @Resource
    RoleMapper roleMapper;

    @Resource
    BClassMapper bClassMapper;

    @Resource
    LoginService loginService;

    @Resource
    TwtPartybranchMapper twtPartybranchMapper;




    final String CHECKOUT_TOKEN_URL = "https://api.twt.edu.cn/api/user/single";
    final String LOGIN_URL = "https://api.twt.edu.cn/api/auth/common";

    /**
     *从小程序登录用账密方式登录
     * @return token
     * @throws Exception
     * @Description: 重载两种登录方式
     */
    @Override
    public String login(AcntPass acntPass) throws Exception {
        String account = acntPass.getAccount();
        String password = acntPass.getPass();

            try {
                loginService.login(acntPass);

                StudentVo info = twtStudentInfoMapper.selectInfoBySno(account);
                Integer uid = info.getUid();

                TwtPartybranch twtPartybranch = twtPartybranchMapper.selectVoById(info.getPartyBranchId());
                RoleType roleType = RoleType.COMMON_USER;

                //判断是否是支书
                if (twtPartybranch!=null&&twtPartybranch.getPartybranchSecretary()!=null&&twtPartybranch.getPartybranchSecretary().getSno().equals(account)){
                    roleType = RoleType.SECRETARY;
                }

                //创建TOKEN
                return JwtUtils.genJwtToken(uid,account, roleType.getTypeNum());

            }catch (LoginException e){
                LOGGER.info("fail to login, account = {}",account);
                throw new LoginException("fail to login");
            }catch (Exception e){
                LOGGER.error("some thing error when login, account = {}",account,e);
                throw new Exception(e);
            }
    }

    @Override
    public TwtStudentInfo getMyInfo(String token) {
        try {
            Integer  uid = JwtUtils.getUid(token);
            TwtStudentInfo info = twtStudentInfoMapper.selectById(uid);
            return info;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public ResponseHelper<StudentVo> getInfoBySno(String sno){
        try {
            return new ResponseHelper<>(twtStudentInfoMapper.selectInfoBySno(sno));
        }catch (Exception e){
            LOGGER.error("fail to get info",e);
            return new ResponseHelper<>(ResponseCode.ERROR,null);
        }
    }

    @Override
    public ResponseHelper<List<StudentVo>> getInfoList(Integer collegeId, Integer grade) {
        try {
            return new ResponseHelper<>(twtStudentInfoMapper.selectStudentByCollegeAndGrade(collegeId, grade)) ;
        }catch (Exception e){
            LOGGER.error("fail to get infoList",e);
            return new ResponseHelper<>(ResponseCode.ERROR,null);
        }
    }

    @Override
    public ResponseHelper<List<ClassVo>> listClass(Integer collegeId,Integer grade) {
        try {
            return new ResponseHelper<>(bClassMapper.listClass(collegeId, grade));
        }catch (Exception e){
            return new ResponseHelper<>(ResponseCode.TARGET_NOT_EXIST,null);
        }
    }

    @Override
    public ResponseHelper<List<StudentGroupByClass>> listStudentByClass(@RequestBody List<Integer>classIdList) {
        try {
            List<StudentVo> studentVos = twtStudentInfoMapper.selectStudentByClassIdList(classIdList);
            Map<ClassVo,List<StudentVo>> map =
                    studentVos.stream().collect(Collectors.groupingBy(StudentVo::getClassVo));
            List<StudentGroupByClass> ret = map.entrySet().stream().
                    map(e->new StudentGroupByClass(
                            e.getKey(),
                            e.getValue()))
                    .collect(Collectors.toList());
            return new ResponseHelper<>(ret);
        }catch (Exception e){
            LOGGER.error("fail to get target",e );
            return new ResponseHelper<>(ResponseCode.TARGET_NOT_EXIST,null);
        }
    }

    @Override
    public ResponseHelper<List<UserInfoBasic>> getUnameList(List<String> sno) {
        try {
            return new ResponseHelper<>(twtStudentInfoMapper.selectBasicInfoBySnoList(sno));
        }catch (Exception e){
            LOGGER.error("fail",e);
            return new ResponseHelper<>(ResponseCode.ERROR,null);
        }
    }

}
