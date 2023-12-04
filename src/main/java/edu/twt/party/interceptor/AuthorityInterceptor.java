package edu.twt.party.interceptor;

import edu.twt.party.annotation.JwtToken;
import edu.twt.party.constant.RoleType;
import edu.twt.party.dao.TwtManagerDao;
import edu.twt.party.dao.TwtStudentInfoMapper;
import edu.twt.party.exception.PermissionException;
import edu.twt.party.exception.TokenErrorException;
import edu.twt.party.helper.ResponseCode;
import edu.twt.party.pojo.manager.TwtManager;
import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.service.BaseService;
import edu.twt.party.utils.JwtUtils;
import edu.twt.party.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AuthorityInterceptor implements HandlerInterceptor {


    @Resource
    private TwtStudentInfoMapper twtStudentInfoMapper;

    @Resource
    private TwtManagerDao twtManagerDao;


    private final Logger logger = LoggerFactory.getLogger(BaseService.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws TokenErrorException{
        if(!(o instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod)o;
        Method method = handlerMethod.getMethod();


        if(!method.isAnnotationPresent(JwtToken.class)) {
            return true;
        }

        String token = request.getHeader("token");
        if(MyUtils.strIsNull(token)){
            throw new TokenErrorException(ResponseCode.TOKEN_EMPTY);
        }
        Integer uid = JwtUtils.getUid(token);
        //除了普通用户还有管理员登录
        Integer roleType = JwtUtils.getUserType(token);

        JwtToken jwtToken = method.getAnnotation(JwtToken.class);

        Object user;
        if(jwtToken.onlyNeedId()){
            if(Objects.equals(roleType,0)){
                user = new TwtStudentInfo();
                ((TwtStudentInfo)user).setId(uid);
            }else {
                user = new TwtManager();
                ((TwtManager)user).setManagerId(uid);
            }
        }else{
            if(Objects.equals(roleType, 0)){
                user = twtStudentInfoMapper.selectById(uid);
            }else{
                user = twtManagerDao.selectById(uid);
            }
        }

        try {
            JwtUtils.checkToken(token);
        }catch (TokenErrorException e){
            logger.info("token wrong",e);
            throw new TokenErrorException(ResponseCode.VALIDATE_FAILED);
        }
        request.setAttribute("user",user);
        List<Integer> allowRoles = Arrays
                .stream(jwtToken.roles())
                .map(RoleType::getTypeNum)
                .collect(Collectors.toList());

        //对于栏目管理员
        if(roleType.equals(RoleType.COLUMN_ADMIN)){
            String columnId = JwtUtils.getColumnId(token);
        }

        //对于院级管理员
        if(roleType.equals(RoleType.COLLEGE_ADMIN)){
            Integer collegeId = JwtUtils.getCollegeId(token);
        }


        //有权限或者是高管
        if(allowRoles.contains(roleType)||Objects.equals(roleType,127)){
            JwtUtils.setToken(token);
            return true;
        }else{
            throw new PermissionException(ResponseCode.NO_PERMISSION);
        }
    }
}
