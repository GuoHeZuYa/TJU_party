package edu.twt.party.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.twt.party.exception.LoginException;
import edu.twt.party.pojo.user.AcntPass;
import edu.twt.party.service.BaseService;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 *
 * @author Guohezu
 *
 */
@Configuration
public class HttpUtils {




    public static final String DOMAIN = "weipeiyang.twt.edu.cn";
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String TICKET = "YmFuYW5hLjM3YjU5MDA2M2Q1OTM3MTY0MDVhMmM1YTM4MmIxMTMwYjI4YmY4YTc=";

    public static final String SUCCESS = "0";

    public static final String TOKEN_HEADER = "token";

    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    //todo: @Value为什么读取不到
    // @Value("${constant.domain}")
    // static String domain;

    /**
     *
     * @return 构造向个人中心发送的请求头
     */
    public static HttpHeaders buildHeadersToOpen(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("ticket",TICKET);
        headers.set("domain",DOMAIN);
        headers.set("Content-type",CONTENT_TYPE);
        return headers;
    }

    public static void loginJudgeFromOpen(ResponseEntity<String> res) throws LoginException{
        String respStr = res.getBody();
        JSONObject response = JSON.parseObject(respStr);
        assert response != null;
        String resCode =  response.get("error_code").toString();
        if(!SUCCESS.equals(resCode)){
            throw new LoginException("fail to login");
        }
    }

    @Resource
    RestTemplate restTemplate;

    final String LOGIN_URL = "https://api.twt.edu.cn/api/auth/common";
    public void login(AcntPass acntPass){
        String account = acntPass.getAccount();
        String password = acntPass.getPass();
        // 向个人中心发送的请求头
        HttpHeaders headers = HttpUtils.buildHeadersToOpen();
        //提交参数设置
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("account", account);
        map.add("password", password);

        // 组装请求体
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        // 发送post请求，并打印结果，以String类型接收响应结果JSON字符串
        ResponseEntity<String> res = restTemplate.postForEntity(LOGIN_URL, request, String.class);

        HttpUtils.loginJudgeFromOpen(res);
    }

}
