package edu.twt.party.service.exam;

import edu.twt.party.pojo.excelParse.applicant.ApplicantExcelImport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public interface ExcelService {

    HashMap<String,Object> parseExcel(InputStream is,String fileName,int type);
    Boolean commitExcel(Integer id, Integer examId,Integer collegeId);
    boolean updateExcelImport(ApplicantExcelImport aei);
    ArrayList<String> checkBeforeCommit(int importId);
}
