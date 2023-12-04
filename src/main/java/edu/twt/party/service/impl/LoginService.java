package edu.twt.party.service.impl;

import edu.twt.party.exception.LoginException;
import edu.twt.party.pojo.user.AcntPass;
import edu.twt.party.utils.HttpUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class LoginService {
    @Resource
    RestTemplate restTemplate;

    final String LOGIN_URL = "https://api.twt.edu.cn/api/auth/common";

    protected void login(AcntPass acntPass) throws LoginException {
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
