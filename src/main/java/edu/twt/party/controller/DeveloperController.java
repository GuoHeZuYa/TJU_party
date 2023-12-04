package edu.twt.party.controller;

import edu.twt.party.helper.ResponseHelper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xsr
 * 测试连接情况等开发使用接口
 */
@RestController
@Tag(name = "developer")

public class DeveloperController {

    @CrossOrigin
    @GetMapping("/api/connection")
    public ResponseHelper<String> testConnection(){
        return new ResponseHelper<>("Connection is OK.");
    }
}
