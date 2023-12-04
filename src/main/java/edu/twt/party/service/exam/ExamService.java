package edu.twt.party.service.exam;

import edu.twt.party.pojo.exam.CollegeActivistTrain;
import edu.twt.party.pojo.exam.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ExamService {
    boolean signupExam(int userId,int examId);
    List<ExamResult> getUserExams(int userId);
    ExamResult getDetailResult(int examId, int userId);
    boolean cancelExam(int examId,int userId);
    List<Exam> getExamList();
    Exam getExamById(int examId);
    boolean addExam(Date start, Date end,int times, String name, int userType, String content);
    boolean updateExam(int id,Date start, Date end,int times, String name, int userType, String content);
    boolean deleteExam(int id);
    boolean addResultBatchById(List<ExamResult> examResults);
    boolean addResultBatchBySno(List<ExamResult> examResults);
    List<ExamSignup> getExamUserById(int id);
    List<ExamResultVo> getExamResultsById(int examId,int academicId);
    List<ExamResultVo> getExamResultsById(int examId,int academicId,Boolean isPass);
    List<CollegeActivistTrain> getCollegesActivistStatus(Integer examId);
    CollegeActivistTrain getCollegeActivistStatus(Integer examId,Integer collegeId);
    boolean updateCollegeActivistStatus(Integer exmaId,Integer collegeId,Integer status);
    List<ExamResultAnalyseEntity> getExamResultAnalyses(int examId);
    HSSFWorkbook exportExamAnalyse(int examId);
    HSSFWorkbook exportExamAttendee(int examId);

    boolean[] getProbationaryStatus(int examId);
    boolean updateProbationaryStatus(int examId, Boolean[] status);
    HashMap<String,ExamUserList> getUserList(int userId);
}
