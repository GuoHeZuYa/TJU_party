package edu.twt.party.service.exam.impl;

import com.alibaba.fastjson2.JSONArray;
import edu.twt.party.dao.exam.ExamMapper;
import edu.twt.party.dao.exam.ExcelMapper;
import edu.twt.party.exception.APIException;
import edu.twt.party.pojo.exam.Exam;
import edu.twt.party.pojo.excelParse.applicant.ApplicantExcelImport;
import edu.twt.party.service.exam.ExcelService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(rollbackFor = Throwable.class)
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    ExcelMapper excelMapper;
    @Autowired
    ExamMapper examMapper;
    private int getStatus(int absent,int cheat,int pass){
        //0未通过/1通过/2缺考/3作弊/4缺考还作弊
        if(absent > 0 && cheat > 0){
            return 4;
        }else if(cheat > 0) {
            return 3;
        } else if (absent > 0) {
            return 2;
        }else if(pass>0){
            return 1;
        }else {
            return 0;
        }
    }

//    public HashMap<String,Object> parseApplicant(InputStream is,String fileName){
//        try {
//            List<String> error = new ArrayList<>();
//            HashMap<String,Object> hashMap = new HashMap<>();
//
//            Workbook workbook;
//            if(fileName.endsWith("xlsx")){
//                workbook = new XSSFWorkbook(is);
//            }else {
//                workbook = new HSSFWorkbook(is);
//            }
//            Sheet sheet = workbook.getSheet("Sheet1");
//            List<ApplicantExcelImport> list = new ArrayList<>();
//            int line = 0;
//            for (Row row : sheet) {
//                if(line==3){
//                    String[] columnName = {"座位号","学号","姓名","学院","笔试","论文","是否违纪","违纪原因","是否缺考","惩罚措施","是否通过","备注"};
//                    boolean ret = true;
//                    for (int i = 0; i < 12; i++) {
//                        ret = ret && columnName[i].equals(row.getCell(i).getStringCellValue());
//                    }
//                    if(!ret){
//                        error.add("请使用正确的模板");
//
//                        hashMap.put("importId",null);
//                        hashMap.put("list",null);
//                        hashMap.put("error",error);
//                        return hashMap;
//                    }
//                }else if(line > 3){
//                    if(row.getCell(4).getNumericCellValue()>100 ||
//                            row.getCell(4).getNumericCellValue()<0||
//                            row.getCell(5).getNumericCellValue()>100||
//                            row.getCell(5).getNumericCellValue()<0){
//                        error.add("第"+(line-3)+"行，成绩错误");
//                    }
//
//                    list.add(new ApplicantExcelImport(
//                            null,String.valueOf((long) row.getCell(1).getNumericCellValue()),
//                            (int)row.getCell(10).getNumericCellValue(),
//                            "[" + (int)row.getCell(4).getNumericCellValue() + ", "+ (int)row.getCell(5).getNumericCellValue() +"]",
//                            getStatus((int)row.getCell(8).getNumericCellValue(),(int)row.getCell(6).getNumericCellValue(),(int)row.getCell(10).getNumericCellValue()),
//                            row.getCell(9).getCellType()== CellType.NUMERIC?(int)row.getCell(9).getNumericCellValue():0,
//                            row.getCell(7).getStringCellValue()+row.getCell(11).getStringCellValue(),
//                            null,row.getCell(2).getStringCellValue()));
//                }
//                line++;
//            }
//            Integer importId = excelMapper.getNewImportId();
//            if(importId==null)importId=0;
//            excelMapper.insertApplicantBatch(list,importId);
//            List<ApplicantExcelImport> checkList = excelMapper.getApplicantsWithUserId(importId);
//            line = 1;
//            for (ApplicantExcelImport ei : checkList) {
//                if (ei.getUserId() == null) {
//                    error.add("第" + line + "行，根据学号查无此人");
//                }else if (ei.getName()==null){
//                    error.add("第" + line + "行，姓名学号不匹配");
//                }
//                line++;
//            }
//            hashMap.put("importId",importId);
//            hashMap.put("list",list);
//            hashMap.put("error",error);
//            return hashMap;
//
//        }catch (IOException e){
//            throw new APIException("excel read fail");
//        }
//    }

//    public HashMap<String, Object> parseActivist(InputStream is, String fileName) {
//        try {
//            List<String> error = new ArrayList<>();
//            HashMap<String,Object> hashMap = new HashMap<>();
//
//            Workbook workbook;
//            if(fileName.endsWith("xlsx")){
//                workbook = new XSSFWorkbook(is);
//            }else {
//                workbook = new HSSFWorkbook(is);
//            }
//            Sheet sheet = workbook.getSheet("Sheet1");
//            List<ApplicantExcelImport> list = new ArrayList<>();
//            int line = 0;
//            for (Row row : sheet) {
//                if(line==3){
//                    String[] columnName = {"期数","学号","姓名","学院","专业","论文","实践","笔试","是否通过"};
//                    boolean ret = true;
//                    for (int i = 0; i < 8; i++) {
//                        ret = ret && columnName[i].equals(row.getCell(i).getStringCellValue());
//                    }
//                    if(!ret){
//                        error.add("请使用正确的模板");
//
//                        hashMap.put("importId",null);
//                        hashMap.put("list",null);
//                        hashMap.put("error",error);
//                        return hashMap;
//                    }
//                }else if(line > 3){
//                    if(row.getCell(5).getNumericCellValue()>100 || row.getCell(5).getNumericCellValue()<0|| row.getCell(6).getNumericCellValue()>100|| row.getCell(6).getNumericCellValue()<0|| row.getCell(7).getNumericCellValue()>100 || row.getCell(7).getNumericCellValue()<0){
//                        error.add("第"+(line-3)+"行，成绩错误");
//                    }
//
//                    list.add(new ApplicantExcelImport(
//                            null, String.valueOf((long) row.getCell(1).getNumericCellValue()),
//                            (int)row.getCell(8).getNumericCellValue(),
//                            "[" + (int)row.getCell(5).getNumericCellValue() + ", "+ (int)row.getCell(6).getNumericCellValue() + ", "+ (int)row.getCell(7).getNumericCellValue() +"]",
//                            row.getCell(8).getNumericCellValue()>0?1:0
//                            ,0,
//                            null,
//                            null,row.getCell(2).getStringCellValue()));
//                }
//                line++;
//            }
//            Integer importId = excelMapper.getNewImportId();
//            if(importId==null)importId=0;
//            excelMapper.insertApplicantBatch(list,importId);
//            List<ApplicantExcelImport> checkList = excelMapper.getApplicantsWithUserId(importId);
//            line = 1;
//            for (ApplicantExcelImport ei : checkList) {
//                if (ei.getUserId() == null) {
//                    error.add("第" + line + "行，根据学号查无此人");
//                }else if (ei.getName()==null){
//                    error.add("第" + line + "行，姓名学号不匹配");
//                }
//                line++;
//            }
//            hashMap.put("importId",importId);
//            hashMap.put("list",list);
//            hashMap.put("error",error);
//            return hashMap;
//
//        }catch (IOException e){
//            throw new APIException("excel read fail");
//        }
//    }

    @Override
    public HashMap<String, Object> parseExcel(InputStream is, String fileName,int type) {
        try {
            List<String> error = new ArrayList<>();
            HashMap<String,Object> hashMap = new HashMap<>();

            Workbook workbook;
            if(fileName.endsWith("xlsx")){
                workbook = new XSSFWorkbook(is);
            }else {
                workbook = new HSSFWorkbook(is);
            }
            Sheet sheet = workbook.getSheet("Sheet1");
            if(sheet==null){
                error.add("请使用正确的模板");
                hashMap.put("importId",null);
                hashMap.put("list",null);
                hashMap.put("error",error);
                return hashMap;
            }
            List<ApplicantExcelImport> list = new ArrayList<>();
            int line = 0;
            for (Row row : sheet) {
                if (line == 3) {
                    String[][] columnName = {
                            {"座位号","学号","姓名","学院","笔试","论文","是否违纪","违纪原因","是否缺考","惩罚措施","是否通过","备注"},
                            {"期数", "学号", "姓名", "学院", "专业", "论文", "实践", "笔试","是否通过"},
                            {"期数", "学号", "姓名", "学院", "专业", "论文", "实践", "是否通过"}};
                    int[] length = {12,9,8};
                    boolean ret = true;
                    for (int i = 0; i < length[type]; i++) {
                        try {
                            ret = ret && columnName[type][i].equals(row.getCell(i).getStringCellValue());
                        } catch (NullPointerException e) {
                            error.add("请使用正确的模板");
                            hashMap.put("importId", null);
                            hashMap.put("list", null);
                            hashMap.put("error", error);
                            return hashMap;
                        }
                    }
                    if (!ret) {
                        error.add("请使用正确的模板");
                        hashMap.put("importId", null);
                        hashMap.put("list", null);
                        hashMap.put("error", error);
                        return hashMap;
                    }
                } else if (line > 3) {
                    try {
                        if (type == 0 && (row.getCell(4).getNumericCellValue()>100 || row.getCell(4).getNumericCellValue()<0|| row.getCell(5).getNumericCellValue()>100|| row.getCell(5).getNumericCellValue()<0)){
                            error.add("第"+(line-3)+"行，成绩错误");
                        }
                        if (type == 1 && (row.getCell(5).getNumericCellValue()>100 || row.getCell(5).getNumericCellValue()<0|| row.getCell(6).getNumericCellValue()>100|| row.getCell(6).getNumericCellValue()<0|| row.getCell(7).getNumericCellValue()>100 || row.getCell(7).getNumericCellValue()<0)){
                            error.add("第"+(line-3)+"行，成绩错误");
                        }
                        if (type == 2 && (row.getCell(5).getNumericCellValue() > 100 || row.getCell(5).getNumericCellValue() < 0 ||row.getCell(6).getNumericCellValue() > 100 || row.getCell(6).getNumericCellValue() < 0)) {
                            error.add("第" + (line - 3) + "行，成绩错误");
                        }
                    } catch (NullPointerException e) {
                        error.add("第" + (line - 3) + "行，成绩错误");
                    }
                    try {
                        if (type==0){
                            list.add(new ApplicantExcelImport(
                                    null,String.valueOf((long) row.getCell(1).getNumericCellValue()),
                                    (int)row.getCell(10).getNumericCellValue(),
                                    "[" + (int)row.getCell(4).getNumericCellValue() + ", "+ (int)row.getCell(5).getNumericCellValue() +"]",
                                    getStatus((int)row.getCell(8).getNumericCellValue(),(int)row.getCell(6).getNumericCellValue(),(int)row.getCell(10).getNumericCellValue()),
                                    row.getCell(9).getCellType()== CellType.NUMERIC?(int)row.getCell(9).getNumericCellValue():0,
                                    (row.getCell(7)!=null?row.getCell(7).getStringCellValue():"")+(row.getCell(11)!=null?row.getCell(11).getStringCellValue():""),
                                    null,row.getCell(2).getStringCellValue()));

                        }else if(type==1){
                            list.add(new ApplicantExcelImport(
                                    null, String.valueOf((long) row.getCell(1).getNumericCellValue()),
                                    (int)row.getCell(8).getNumericCellValue(),
                                    "[" + (int)row.getCell(5).getNumericCellValue() + ", "+ (int)row.getCell(6).getNumericCellValue() + ", "+ (int)row.getCell(7).getNumericCellValue() +"]",
                                    row.getCell(8).getNumericCellValue()>0?1:0
                                    ,0,
                                    null,
                                    null,row.getCell(2).getStringCellValue()));

                        }else if(type==2){
                            list.add(new ApplicantExcelImport(
                                    null, String.valueOf((long) row.getCell(1).getNumericCellValue()),
                                    (int) row.getCell(7).getNumericCellValue(),
                                    "[" + (int) row.getCell(5).getNumericCellValue() + ", " + (int) row.getCell(6).getNumericCellValue() + "]",
                                    row.getCell(7).getNumericCellValue() > 0 ? 1 : 0
                                    , 0,
                                    null,
                                    null, row.getCell(2).getStringCellValue()));
                        }

                    } catch (NullPointerException e) {
                        error.add("第" + (line - 3) + "行，未知错误");
                    }
                }
                line++;
            }
            if(list.size()==0){
                error.add("解析结果为空");
                hashMap.put("importId", null);
                hashMap.put("list", null);
                hashMap.put("error", error);
                return hashMap;
            }
            Integer importId = excelMapper.getNewImportId();
            if(importId==null)importId=0;
            excelMapper.insertApplicantBatch(list,importId);
            List<ApplicantExcelImport> checkList = excelMapper.getApplicantsWithUserId(importId);
            line = 1;
            for (ApplicantExcelImport ei : checkList) {
                if (ei.getUserId() == null) {
                    error.add("第" + line + "行，根据学号查无此人");
                }else if (ei.getName()==null){
                    error.add("第" + line + "行，姓名学号不匹配");
                }
                line++;
            }
            int i = 0;
            for (ApplicantExcelImport ei :
                    list) {
                ei.setId(checkList.get(i).getId());
                i++;
            }
            hashMap.put("importId",importId);
            hashMap.put("list",list);
            hashMap.put("error",error);
            return hashMap;

        }catch (IOException e){
            throw new APIException("excel read fail");
        }
    }

    @Override
    public Boolean commitExcel(Integer id, Integer examId,Integer collegeId) {
        Exam exam = examMapper.getExamById(examId);
        if(exam!=null){
            if (exam.getUserType()==0){//入党申请人
                examMapper.updateExamStatusById(examId,3);
            }else if(exam.getUserType()==1){//积极份子
                examMapper.deleteCollegeActivistStatus(examId,collegeId);
                examMapper.updateCollegeActivistStatus(examId,collegeId,3);
            }else if(exam.getUserType()==2){
                examMapper.updateExamStatusById(examId,3);
            }else {
                return false;
            }
            return excelMapper.commitApplicant(id,examId);
        }else {
            return false;
        }
    }
    public boolean updateExcelImport(ApplicantExcelImport aei){
        return excelMapper.updateExcelImport(aei);
    }

    @Override
    public ArrayList<String> checkBeforeCommit(int importId) {
        ArrayList error = new ArrayList<>();
        List<ApplicantExcelImport> checkList = excelMapper.getApplicantsWithUserId(importId);
        int line = 1;
        for (ApplicantExcelImport ei : checkList) {
            if (ei.getUserId() == null) {
                error.add("第" + line + "行，根据学号查无此人");
            }else if (ei.getName()==null){
                error.add("第" + line + "行，姓名学号不匹配");
            }

            JSONArray jsonArray = JSONArray.parseArray(ei.getDetailScore());
            if(jsonArray != null){
                Integer[] scores = jsonArray.toArray(Integer.class);
                if(scores.length>0){
                    for (Integer score :
                            scores) {
                        if(score>100||score<0){
                            error.add("第" + line + "行，成绩错误");
                            break;
                        }
                    }
                }else {
                    error.add("第" + line + "行，成绩错误");
                }
            }else {
                error.add("第" + line + "行，成绩错误");
            }

            line++;
        }

        return error;
    }
}
