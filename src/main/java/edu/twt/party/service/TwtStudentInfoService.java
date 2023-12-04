package edu.twt.party.service;

import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.classPojo.ClassVo;
import edu.twt.party.pojo.student.UserInfoBasic;
import edu.twt.party.pojo.student.StudentGroupByClass;
import edu.twt.party.pojo.student.StudentVo;
import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.pojo.user.AcntPass;

import java.util.List;

public interface TwtStudentInfoService extends BaseService {
    String login(AcntPass acntPass) throws Exception;

    TwtStudentInfo getMyInfo(String token);

    ResponseHelper<StudentVo>  getInfoBySno(String sno);

    ResponseHelper<List<StudentVo>>  getInfoList(Integer collegeId,Integer grade);

    ResponseHelper<List<ClassVo>> listClass(Integer collegeId,Integer grade);


    ResponseHelper<List<StudentGroupByClass>> listStudentByClass(List<Integer> idList);

    ResponseHelper<List<UserInfoBasic>> getUnameList(List<String> sno);





}
