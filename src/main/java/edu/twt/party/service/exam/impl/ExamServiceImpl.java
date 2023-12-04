package edu.twt.party.service.exam.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import edu.twt.party.dao.TwtStudentInfoMapper;
import edu.twt.party.dao.exam.ExamMapper;
import edu.twt.party.exception.APIException;
import edu.twt.party.pojo.course.CourseUser;
import edu.twt.party.pojo.exam.CollegeActivistTrain;
import edu.twt.party.pojo.exam.*;
import edu.twt.party.pojo.userProcess.UserProcessNode;
import edu.twt.party.service.course.CourseServise;
import edu.twt.party.service.exam.ExamService;
import edu.twt.party.service.process.UserStateService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    ExamMapper examMapper;
    @Autowired
    TwtStudentInfoMapper twtStudentInfoMapper;
    @Autowired
    UserStateService userStateService;
    @Autowired
    CourseServise courseServise;
    //报名
    public boolean signupExam(int userId,int examId)throws APIException{
        Exam exam = examMapper.getExamById(examId);
        int userType = userStateService.getUserType(userId).getId();
        if(exam!=null && exam.getUserType() < 3){
            if(exam.getUserType()==0){
                if(userType == 0 && !examMapper.checkSignup(userId,examId) && courseServise.isPassAllCourse(userId)){
                    return examMapper.signupExam(userId,examId);
                }
            } else if (exam.getUserType()==1) {
                if(userType == 1 && examMapper.checkSignup(userId,examId)){
                    return examMapper.signupExam(userId,examId);
                }
            }else if(exam.getUserType()==2){
                if(userType == 3 && examMapper.checkSignup(userId,examId)){
                    return examMapper.signupExam(userId,examId);
                }
            }else {
                throw new APIException("Wrong exam type");
            }
        }
        return false;
    }
    //已报名的考试
    public List<ExamResult> getUserExams(int userId)throws APIException {
        return examMapper.getUserExams(userId);
    }
    //已考完的详情
    public ExamResult getDetailResult(int examId, int userId)throws APIException{
        return examMapper.getExamResultById(examId,userId);
    }
    //退考
    public boolean cancelExam(int examId,int userId)throws APIException{
        //TODO:校验是否允许取消报名
        return examMapper.cancelExam(examId,userId);
    }
    //可报名考试的列表
    public List<Exam> getExamList()throws APIException{
        //TODO:根据用户类型和考试状态判断是否可以报名
        return examMapper.getExams();
    }
    public Exam getExamById(int examId){
        return examMapper.getExamById(examId);
    }
    public boolean addExam(Date start, Date end, int times,String name, int userType, String content)throws APIException{
        if(name.equals("")){
            throw new APIException("名字不能为空");
        }
        if(start.compareTo(end)>=0){
            throw new APIException("起止时间错误");
        }
        return examMapper.addExam(start,end,times,name,userType,content);
    }
    public boolean updateExam(int id,Date start, Date end,int times, String name, int userType, String content){
        if(name.equals("")){
            throw new APIException("名字不能为空");
        }
        if(start.compareTo(end)>=0){
            throw new APIException("起止时间错误");
        }
        return examMapper.updateExamById(id,start,end ,times,name,userType,content);

    }
    public boolean deleteExam(int id){
        return examMapper.deleteExamById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public boolean addResultBatchById(List<ExamResult> examResults) {
        return examMapper.addExamResultById(examResults);
    }
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public boolean addResultBatchBySno(List<ExamResult> examResults) {
        return examMapper.addExamResultBySno(examResults);
    }


    public List<ExamSignup> getExamUserById(int id){
        return examMapper.getExamUserById(id);
    }

    @Override
    public List<CollegeActivistTrain> getCollegesActivistStatus(Integer examId) {
        return examMapper.getCollegesActivistStatus(examId);
    }

    @Override
    public List<ExamResultVo> getExamResultsById(int examId,int academicId) {
        if(academicId < 0){
            return examMapper.getExamResultsById(examId);
        }
        return examMapper.getExamResultsByIdAndAcademic(examId,academicId);
    }
    @Override
    public List<ExamResultVo> getExamResultsById(int examId,int academicId,Boolean isPass) {
        if(isPass == null){
            return this.getExamResultsById(examId,academicId);
        }else {
            if(academicId < 0){
                if(isPass){
                    return examMapper.getExamResultsByIdPass(examId);
                }else {
                    return examMapper.getExamResultsByIdNotPass(examId);
                }
            }else {
                if (isPass){
                    return examMapper.getExamResultsByIdAndAcademicPass(examId,academicId);
                }else {
                    return examMapper.getExamResultsByIdAndAcademicNotPass(examId,academicId);
                }
            }
        }
    }

    @Override
    public CollegeActivistTrain getCollegeActivistStatus(Integer examId, Integer collegeId) {
        CollegeActivistTrain collegeActivistTrain = examMapper.getCollegeActivistStatus(examId,collegeId);
        if(collegeActivistTrain == null){
            return new CollegeActivistTrain(0,collegeId,"",examId,0,false,null,null);
        }else {
            return collegeActivistTrain;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public boolean updateCollegeActivistStatus(Integer exmaId, Integer collegeId, Integer status) {
        examMapper.deleteCollegeActivistStatus(exmaId,collegeId);
        return examMapper.updateCollegeActivistStatus(exmaId,collegeId,status);
    }

    @Override
    public HSSFWorkbook exportExamAnalyse(int examId) {
        String tableHeader[] = {"学院名称","考试总人数","参加人数","缺考人数","缺考率%","作弊人数","通过人数","净通过率%","总通过率%"};
        List<ExamResultAnalyseEntity> list = examMapper.getExamResultAnalyse(examId);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet1");
        HSSFRow row =  sheet.createRow(0);
        for (int i = 0; i < tableHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(tableHeader[i]);
            cell.setCellType(CellType.STRING);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i+1);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(list.get(i).getCollegename());
            cell.setCellType(CellType.STRING);
            cell = row.createCell(1);
            cell.setCellValue(list.get(i).getAllNum());
            cell.setCellType(CellType.NUMERIC);
            cell = row.createCell(2);
            cell.setCellValue(list.get(i).getPresentNum());
            cell.setCellType(CellType.NUMERIC);
            cell = row.createCell(3);
            cell.setCellValue(list.get(i).getAbsentNum());
            cell.setCellType(CellType.NUMERIC);
            cell = row.createCell(4);
            cell.setCellValue(list.get(i).getAbsentRate()*100);
            cell.setCellType(CellType.NUMERIC);
            cell = row.createCell(5);
            cell.setCellValue(list.get(i).getCheatNum());
            cell.setCellType(CellType.NUMERIC);
            cell = row.createCell(6);
            cell.setCellValue(list.get(i).getPassNum());
            cell.setCellType(CellType.NUMERIC);
            cell = row.createCell(7);
            cell.setCellValue(list.get(i).getPassRatePresent()*100);
            cell.setCellType(CellType.NUMERIC);
            cell = row.createCell(8);
            cell.setCellValue(list.get(i).getPassRateAll()*100);
            cell.setCellType(CellType.NUMERIC);
        }
        return workbook;
    }

    @Override
    public List<ExamResultAnalyseEntity> getExamResultAnalyses(int examId) {
        return examMapper.getExamResultAnalyse(examId);
    }

    @Override
    public HSSFWorkbook exportExamAttendee(int examId) {
        String tableHeader[] = {"序号","学号","姓名","学院"};
        List<ExamSignup> list = examMapper.getExamUserById(examId);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet1");
        HSSFRow row =  sheet.createRow(0);
        for (int i = 0; i < tableHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(tableHeader[i]);
            cell.setCellType(CellType.STRING);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i+1);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell.setCellType(CellType.NUMERIC);
            cell = row.createCell(1);
            cell.setCellValue(list.get(i).getSno());
            cell.setCellType(CellType.STRING);
            cell = row.createCell(2);
            cell.setCellValue(list.get(i).getName());
            cell.setCellType(CellType.STRING);
            cell = row.createCell(3);
            cell.setCellValue(list.get(i).getAcademyName());
            cell.setCellType(CellType.STRING);
        }
        return workbook;
    }

    @Override
    public boolean[] getProbationaryStatus(int examId) {
        CollegeActivistTrain collegeActivistTrain = examMapper.getCollegeActivistStatus(examId,0);
        boolean[] result = {false,false,false};
        if (collegeActivistTrain!=null){
            result[2] = ((collegeActivistTrain.getStatus()&0x4) >> 2) == 1;
            result[1] = ((collegeActivistTrain.getStatus()&0x2) >> 1) == 1;
            result[0] = ((collegeActivistTrain.getStatus()&0x1) == 1);
        }
        return result;
    }

    @Override
    public boolean updateProbationaryStatus(int examId, Boolean[] status) {
        int result = 0;
        int i = 1;
        for (Boolean b : status) {
            if(b) result += i;
            i*=2;
        }
        examMapper.deleteCollegeActivistStatus(examId,0);
        return examMapper.updateCollegeActivistStatus(examId,0,result);
    }

    @Override
    public HashMap<String, ExamUserList> getUserList(int userId) {
        HashMap<String, ExamUserList> ret = new HashMap();
        int courseData[] = new int[]{0,0};
        int examPassTerm[] = new int[]{0,0,0,0,0};//考试通过的期数

        List<ExamResult> examResults = getUserExams(userId);
        ArrayList<ExamResult> examPassedResults = new ArrayList<>();
        ArrayList<Exam> examPassed = new ArrayList<>();
        List<Exam> allExam = getExamList();
        List<Exam> examNoResult = examMapper.getExamsSignedUpButNoResult(userId);
        
        //20课
        List<CourseUser> courseUsers = courseServise.getUserCourse(userId,-1);
        for (CourseUser item : courseUsers) {
            if (item.isPass()) {
                courseData[0]++;
            }
            courseData[1]++;
        }
        ret.put("20course",new ExamUserList(0,courseData[0]==courseData[1]?1:0,courseData));

        //入党申请人考试
        for (ExamResult item : examResults) {
            if (item.getStatus() == 1) {
                Exam exam = getExamById(item.getExamId());
                examPassed.add(exam);
                examPassedResults.add(item);
            }
        }
        
        if(userStateService.getUserProcessStatus(userId, UserProcessNode.APPLICANT_EXERCISE)==1){
            //已经完成，查下分数
            ExamResult targetExamResult = null;
            int applicantData[] = null;
            for (int i=0;i<examPassed.size();i++){
                if(examPassed.get(i).getUserType()==0){
                    targetExamResult = examPassedResults.get(i);
                }
            }
            if(targetExamResult!=null){
                JSONArray applicantDataJson = JSON.parseArray(targetExamResult.getDetailScore());
                List<Integer> applicantDataList = applicantDataJson.toList(Integer.class);
                applicantData = new int[applicantDataList.size()];
                for(int i=0;i< applicantDataList.size();i++)applicantData[i]=applicantDataList.get(i);
            }
            ret.put("applicant",new ExamUserList(1,1,applicantData));
        }else{
            int status = 0;
            int targetExamId = 0;
            for (Exam item : examNoResult) {
                if (item.getUserType() == 0) {
                    status = 3;
                    targetExamId = item.getId();
                }
            }
            if(targetExamId == 0){
                //未报名，查询是否能报名
                for (Exam item : allExam) {
                    if(item.getUserType()==0&&
                            item.getStartTime().before(new Date(System.currentTimeMillis()))&&
                            item.getEndTime().after(new Date(System.currentTimeMillis()))
                    ){
                        targetExamId = item.getId();
                        status = 2;
                    }
                }
            }
            if(targetExamId!=0){
                ret.put("applicant",new ExamUserList(1,status,new int[]{targetExamId}));
            }else {
                ret.put("applicant",new ExamUserList(1,0,new int[]{}));
            }
        }
        //积极分子
        if(userStateService.getUserProcessStatus(userId, UserProcessNode.ACTIVIST_TRAIN)==1){
            //已经完成，查下分数
            ExamResult targetExamResult = null;
            int applicantData[] = null;
            for (int i=0;i<examPassed.size();i++){
                if(examPassed.get(i).getUserType()==1){
                    targetExamResult = examPassedResults.get(i);
                }
            }
            if(targetExamResult!=null){
                JSONArray applicantDataJson = JSON.parseArray(targetExamResult.getDetailScore());
                List<Integer> applicantDataList = applicantDataJson.toList(Integer.class);
                applicantData = new int[applicantDataList.size()];
                for(int i=0;i< applicantDataList.size();i++)applicantData[i]=applicantDataList.get(i);
            }
            ret.put("activist",new ExamUserList(2,1,applicantData));
        }else{
            int status = 0;
            int targetExamId = 0;
            for (Exam item : examNoResult) {
                if (item.getUserType() == 1) {
                    status = 3;
                    targetExamId = item.getId();
                }
            }
            if(targetExamId == 0){
                //未报名，查询是否能报名
                for (Exam item : allExam) {
                    if(item.getUserType()==1&&
                            item.getStartTime().before(new Date(System.currentTimeMillis()))&&
                            item.getEndTime().after(new Date(System.currentTimeMillis()))
                    ){
                        targetExamId = item.getId();
                        status = 2;
                    }
                }
            }
            if(targetExamId!=0){
                ret.put("activist",new ExamUserList(1,status,new int[]{targetExamId}));
            }else {
                ret.put("activist",new ExamUserList(1,0,new int[]{}));
            }
        }
        //预备党员
        if(userStateService.getUserProcessStatus(userId, UserProcessNode.PROBATIONARY_EXERCISE)==1){
            //已经完成，查下分数
            ExamResult targetExamResult = null;
            int applicantData[] = null;
            for (int i=0;i<examPassed.size();i++){
                if(examPassed.get(i).getUserType()==2){
                    targetExamResult = examPassedResults.get(i);
                }
            }
            if(targetExamResult!=null){
                JSONArray applicantDataJson = JSON.parseArray(targetExamResult.getDetailScore());
                List<Integer> applicantDataList = applicantDataJson.toList(Integer.class);
                applicantData = new int[applicantDataList.size()];
                for(int i=0;i< applicantDataList.size();i++)applicantData[i]=applicantDataList.get(i);
            }
            ret.put("probationary",new ExamUserList(1,1,applicantData));
        }else{
            int status = 0;
            int targetExamId = 0;
            for (Exam item : examNoResult) {
                if (item.getUserType() == 2) {
                    status = 3;
                    targetExamId = item.getId();
                }
            }
            if(targetExamId == 0){
                //未报名，查询是否能报名
                for (Exam item : allExam) {
                    if(item.getUserType()==2&&
                            item.getStartTime().before(new Date(System.currentTimeMillis()))&&
                            item.getEndTime().after(new Date(System.currentTimeMillis()))
                    ){
                        targetExamId = item.getId();
                        status = 2;
                    }
                }
            }
            if(targetExamId!=0){
                ret.put("probationary",new ExamUserList(1,status,new int[]{targetExamId}));
            }else {
                ret.put("probationary",new ExamUserList(1,0,new int[]{}));
            }
        }

        if(ret.getOrDefault("applicant",null)==null){
            ret.put("applicant",new ExamUserList(1,0,new int[]{}));
        }
        if(ret.getOrDefault("activist",null)==null){
            ret.put("activist",new ExamUserList(2,0,new int[]{}));
        }
        if(ret.getOrDefault("probationary",null)==null){
            ret.put("probationary",new ExamUserList(3,0,new int[]{}));
        }


        return ret;
    }
}
