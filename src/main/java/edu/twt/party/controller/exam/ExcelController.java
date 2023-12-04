package edu.twt.party.controller.exam;

import edu.twt.party.exception.APIException;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.excelParse.applicant.ApplicantExcelImport;
import edu.twt.party.service.exam.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class ExcelController {
    @Autowired
    ExcelService excelService;
    @PostMapping("/api/exam/excel/applicant")
    public ResponseHelper<HashMap<String,Object>> parseApplicant(MultipartFile file){
        try {
            return new ResponseHelper<>(excelService.parseExcel(file.getInputStream(), file.getOriginalFilename(),0));
        }catch (IOException e){
            throw new APIException(e.toString());
        }
    }
    @PostMapping("/api/exam/excel/activist")
    public ResponseHelper<HashMap<String,Object>> parseActivist(MultipartFile file){
        try {
            return new ResponseHelper<>(excelService.parseExcel(file.getInputStream(), file.getOriginalFilename(),1));
        }catch (IOException e){
            throw new APIException(e.toString());
        }
    }
    @PostMapping("/api/exam/excel/probationary")
    public ResponseHelper<HashMap<String,Object>> parseProbationary(MultipartFile file){
        try {
            return new ResponseHelper<>(excelService.parseExcel(file.getInputStream(), file.getOriginalFilename(),2));
        }catch (IOException e){
            throw new APIException(e.toString());
        }
    }
    @GetMapping("/api/exam/excel/{id}/commit/{examId}")
    public ResponseHelper<HashMap<String,Object>> commitApplicant(@PathVariable(name = "id")Integer id,@PathVariable(name = "examId")Integer examId){
        HashMap<String,Object> hashMap = new HashMap<String,Object>();
        ArrayList<String> error = excelService.checkBeforeCommit(id);
        Boolean success = false;
        if(error.size()==0){
            success = excelService.commitExcel(id,examId,null);
        }
        hashMap.put("result",success);
        hashMap.put("error",error);
        return new ResponseHelper<>(hashMap);
    }
    @GetMapping("/api/exam/excel/{id}/commit/{examId}/{collegeId}")
    public ResponseHelper<HashMap> commitActivist(@PathVariable(name = "id")Integer id,@PathVariable(name = "examId")Integer examId,@PathVariable(name = "collegeId")int collegeId){
        HashMap<String,Object> hashMap = new HashMap<String,Object>();
        ArrayList<String> error = excelService.checkBeforeCommit(id);
        Boolean success = false;
        if(error.size()==0){
            success = excelService.commitExcel(id,examId,collegeId);
        }
        hashMap.put("result",success);
        hashMap.put("error",error);
        return new ResponseHelper<>(hashMap);
    }
    @PostMapping("/api/exam/excel/update")
    public ResponseHelper<Boolean> updateExcelImport(@RequestBody ApplicantExcelImport aei){
        return new ResponseHelper<>(excelService.updateExcelImport(aei));
    }
}
