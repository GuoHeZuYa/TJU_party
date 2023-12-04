package edu.twt.party.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.twt.party.constant.RoleType;
import edu.twt.party.dao.TwtPartybranchMapper;
import edu.twt.party.exception.PermissionException;
import edu.twt.party.exception.TokenErrorException;
import edu.twt.party.helper.ResponseCode;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public class JwtUtils {

    /**
     * 一小时
     */
    private static final long HOUR = 60 * 60 * 1000;
    /**
     * 30天
     */
    private static final Long EXPIRE_TIME = HOUR * 24 * 30;

    /**
     * secret
     */
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";

    private static final String SECRET = "fuck_crazy_thursday";
    private static final String ISS = "TwT_Studio";

    /**
     * header字段
     */
    public static final String HEADER = "token";

    /**
     * token清空
     */
    public static final String UNDEFINED = "undefined";

    /**
     * 本地线程存储token
     */
    private static ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    public static void setToken(String token){
        tokenThreadLocal.set(token);
    }

    public static String getToken(){
        return tokenThreadLocal.get();
    }

    public static  void tokenClear(){
        tokenThreadLocal.remove();
    }



    /**
     * 不涉及学院、栏目的jwt token
     * @param userNumber 学号
     * @param role 权限
     * @return token字符串
     */
    public static String genJwtToken(Integer uid,String userNumber, Integer role){
        return genJwtToken(uid,userNumber,role,0,"");
    }


    /**
     * 学院管理员jwt token
     * @param userNumber 学号
     * @param role 权限
     * @return token字符串
     */
    public static String genJwtToken(Integer uid,String userNumber, Integer role,Integer collegeId){
        return genJwtToken(uid,userNumber,role,collegeId,"");
    }

    /**
     * 生成jwt token
     * @param userNumber 学号
     * @param role 权限
     * @return token字符串
     */
    public static String genJwtToken(Integer uid,String userNumber, Integer role, Integer collegeId,String columns){
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    // 将 user id 保存到 token 里面
                    .withAudience(uid.toString(),userNumber, String.valueOf(role),collegeId.toString(),columns)
                    .withIssuer(ISS)
                    // 30天后token过期
                    .withExpiresAt(date)
                    // token 的密钥
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检验token的合规性
     * @param token
     */
    public static void checkToken(String token) throws TokenErrorException{
        if (token.equals(UNDEFINED)) {
            throw new TokenErrorException(ResponseCode.PLEASE_LOGIN);
        }
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
    }

    public static Integer getUid(String token) throws TokenErrorException {
            return Integer.parseInt(JWT.decode(token).getAudience().get(0));
    }

    /**
     * 从token中获取学号
     * @param token
     * @return 学号
     */
    public static String AnalysisUserNumber(String token) {
        try {
            return JWT.decode(token).getAudience().get(1);
        } catch (JWTDecodeException e) {
            throw new TokenErrorException(ResponseCode.TOKEN_ERROR);
        }
    }

    public static  String getUserNumber(){
        return JwtUtils.AnalysisUserNumber(JwtUtils.getToken());
    }

    /**
     * 从token中获取用户权限
     * @param token
     * @return 用户权限数字
     */
    public static Integer getUserType(String token) {
        try {
            String roleStr = JWT.decode(token).getAudience().get(2);
            return Integer.parseInt(roleStr);
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 从本地token中获取用户权限
     * @return 用户权限数字
     */
    public static Integer getUserType() {
        return JwtUtils.getUserType(JwtUtils.getToken());
    }

    /**
     * 从token中获取学院id
     * @param token
     * @return
     */
    public static Integer getCollegeId(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            List<String> audience = jwt.getAudience();
            String collegeIdStr = JWT.decode(token).getAudience().get(3);
            return Integer.parseInt(collegeIdStr);
        } catch (JWTDecodeException e) {
            throw new TokenErrorException(ResponseCode.TOKEN_ERROR);
        }
    }

    /**
     * 从本地token中获取学院id
     * @return
     */
    public static Integer getCollegeId(){
        return JwtUtils.getCollegeId(JwtUtils.getToken());
    }

    /**
     * 从token中获取栏目id
     * @param token
     * @return
     */
    public static String getColumnId(String token){
        try {
            String columnIdStr = JWT.decode(token).getAudience().get(4);
            return columnIdStr;
        } catch (JWTDecodeException e) {
            throw new TokenErrorException(ResponseCode.TOKEN_ERROR);
        }
    }

    @Resource
    TwtPartybranchMapper twtPartybranchMapper;



    /**
     * 校验是否有操作目标学院的权限
     * @param targetCollegeId
     * @return
     */
    public static Boolean checkCollege(Integer targetCollegeId) throws PermissionException {

        Integer managerType = JwtUtils.getUserType();

        if(managerType.equals(RoleType.ROOT.getTypeNum())) return Boolean.TRUE;

        if(managerType.equals(RoleType.COLLEGE_ADMIN.getTypeNum())){

            Integer collegeId = JwtUtils.getCollegeId();
            if(targetCollegeId.equals(collegeId))return Boolean.TRUE;
        }

        throw new PermissionException(ResponseCode.NO_PERMISSION);
    }

}
