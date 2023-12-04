package edu.twt.party.controller;

import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.College;
import edu.twt.party.service.CollegeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(name = "学院相关")
@RestController
public class CollegeController {
    @Autowired
    CollegeService collegeService;
    @GetMapping("/api/college/all")
    @Operation(summary = "获取学院列表")
    public ResponseHelper<List<College>> getColleges(){
        return new ResponseHelper<>(collegeService.getColleges());
    }
}
