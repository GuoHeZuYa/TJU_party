package edu.twt.party.service.probationary.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.twt.party.dao.exam.ExamMapper;
import edu.twt.party.dao.probationary.ProbationaryPartyMemberCourseMapper;
import edu.twt.party.dao.probationary.ProbationaryPartyMemberStudentCourseMapper;
import edu.twt.party.dao.probationary.ProbationaryPartyMemberStudentMapper;
import edu.twt.party.dao.probationary.ProbationaryPartyMemberTrainMapper;
import edu.twt.party.pojo.exam.Exam;
import edu.twt.party.pojo.probationary.*;
import edu.twt.party.service.probationary.ProbationaryPartyMemberStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ProbationaryPartyMemberStudentServiceImpl extends ServiceImpl<ProbationaryPartyMemberStudentMapper, ProbationaryPartyMemberStudent>
    implements ProbationaryPartyMemberStudentService {
    @Autowired
    private ProbationaryPartyMemberStudentMapper studentMapper;
    @Autowired
    private ProbationaryPartyMemberStudentCourseMapper studentCourseMapper;
    @Autowired
//    @Qualifier("ExamMapper")
    private ProbationaryPartyMemberTrainMapper trainMapper;
    @Autowired
    private ProbationaryPartyMemberCourseMapper courseMapper;
    @Override
    public StudentVO getStudentBySnoAndTerm(String sno, Integer term) {
        QueryWrapper<ProbationaryPartyMemberStudent> wrapper = new QueryWrapper<>();
        wrapper .eq("sno",sno)
                .eq("term",term);
        ProbationaryPartyMemberStudent student = studentMapper.selectOne(wrapper);
        /* 如果报名了这期培训 */
        if(student == null) {
            return null;
        }
        /* 设置课程状态 */
        StudentVO studentVO = new StudentVO();
        studentVO.setTerm(term);
        studentVO.setSno(sno);
        studentVO.setPaperScore(student.getPaperScore());
        studentVO.setPracticeScore(student.getPracticeScore());
        List<StudentCourseVO> courseList = studentCourseMapper.getScoresBySnoAndTerm(sno, term);
        studentVO.setCourses(courseList);
        return studentVO;
    }
    @Override
    public List<StudentVO> getStudentListBySno(String sno) {
        List<StudentVO> scoreList = new ArrayList<>();
        for (Integer term: studentMapper.getTermsBySno(sno)) {
            scoreList.add(this.getStudentBySnoAndTerm(sno, term));
        }
        return scoreList;
    }

    @Override
    public List<StudentVO> getStudentListByTerm(Integer term) {
        List<StudentVO> scoreList = new ArrayList<>();
        for (String sno : studentMapper.getSnosByTerm(term)) {
            scoreList.add(this.getStudentBySnoAndTerm(sno, term));
        }
        return scoreList;
    }

    @Override
    @Transactional
    public Boolean cancelSignUp(String sno, Integer term) {
        QueryWrapper<ProbationaryPartyMemberStudent> wrapper = new QueryWrapper<>();
        wrapper .eq("deleted", true)
                .eq("term", term)
                .eq("sno", sno);
        //如果这个学生取消对这期培训取消报名过，则不能再次取消报名
        if(studentMapper.selectCount(wrapper) >= 1) {
            return false;
        }
        //删除学生报名信息
        wrapper.clear();
        wrapper .eq("term", term)
                .eq("sno", sno);
        studentMapper.delete(wrapper);
        //删除学生成绩信息
        QueryWrapper<ProbationaryPartyMemberStudentCourse> scoreWrapper = new QueryWrapper<>();
        scoreWrapper .eq("term", term)
                     .eq("sno", sno);
        studentCourseMapper.delete(scoreWrapper);
        return true;
    }

    @Override
    @Transactional
    public Boolean selectCourse(String sno, Integer cid) {
        ProbationaryPartyMemberCourse course = courseMapper.selectById(cid);
        if(course == null) {
            return false;
        }
        //未开放选课则不能选课
        Boolean canSelect = ProbationaryPartyMemberTrain.transStatus(
                trainMapper.getStatusByTerm(course.getTerm(), PPMConfig.USERT_TYPE), PPMConfig.SELECT) ;
        if(!canSelect) {
            return false;
        }
        return lateSelectCourse(sno, cid);
    }

    @Override
    @Transactional
    public Boolean cancelCourse(String sno, Integer cid) {
        QueryWrapper<ProbationaryPartyMemberStudentCourse> wrapper = new QueryWrapper<>();
        ProbationaryPartyMemberCourse course = courseMapper.selectById(cid);
        Date date = new Date();
        //如果课程已经上完则不能退课
        if(date.getTime() >= course.getEndTime().getTime()) {
            return false;
        }
        wrapper .eq("sno", sno)
                .eq("cid", cid)
                .eq("term", course.getTerm());
        ProbationaryPartyMemberStudentCourse score = studentCourseMapper.selectOne(wrapper);
        //未选课或者课程已经出分则不能退课
        if(score == null || !("-".equals(score.getScore()))) {
            return false;
        }
        QueryWrapper<ProbationaryPartyMemberStudent> studentWrapper = new QueryWrapper<>();
        studentWrapper
                .eq("term", course.getTerm())
                .eq("sno", sno)
                .eq("deleted", false);
        ProbationaryPartyMemberStudent student = studentMapper.selectOne(studentWrapper);
        if(course.getType()) {
            student.setElectiveCourseNum(false);
        } else {
            student.setRequiredCourseNum(student.getRequiredCourseNum() - 1);
        }
        studentMapper.update(student,studentWrapper);
        return studentCourseMapper.delete(wrapper) == 1;
    }

    @Transactional
    @Override
    public Boolean whetherSignedUp(String sno) {
        ProbationaryPartyMemberTrain train = trainMapper.getLatestTrain(PPMConfig.USERT_TYPE);
        train.getTerm();
        QueryWrapper<ProbationaryPartyMemberStudent> wrapper = new QueryWrapper<>();
        wrapper .eq("term",train.getTerm())
                .eq("sno", sno);
        ProbationaryPartyMemberStudent student = studentMapper.selectOne(wrapper);
        return !Objects.isNull(student);
    }

    @Override
    @Transactional
    public Boolean lateSelectCourse(String sno, Integer cid) {
        ProbationaryPartyMemberCourse course = courseMapper.selectById(cid);
        QueryWrapper<ProbationaryPartyMemberStudent> wrapper = new QueryWrapper<>();
        //如果学生未报名这次培训或者没有这个课程则选课失败
        if(course == null) {
            return false;
        }
        wrapper .eq("term", course.getTerm())
                .eq("sno", sno);
        ProbationaryPartyMemberStudent student = studentMapper.selectOne(wrapper);
        if(student == null) {
            return false;
        }
        // 课程已经开始则不能再选课
        Date curDate = new Date();
        if(curDate.getTime() >= course.getStartTime().getTime()) {
            return false;
        }
        //选修课已选则不能选课
        if(course.getType() && student.getElectiveCourseNum()) {
            return false;
        }
        //必修课已选3节则不能选课
        if(!course.getType() && student.getRequiredCourseNum() == 3) {
            return false;
        }
        if(course.getType()) {
            student.setElectiveCourseNum(true);
        } else {
            student.setRequiredCourseNum(student.getRequiredCourseNum() + 1);
        }
        ProbationaryPartyMemberStudentCourse score = new ProbationaryPartyMemberStudentCourse();
        score.setSno(sno);
        score.setTerm(course.getTerm());
        score.setCid(cid);
        score.setDeleted(false);
        score.setInherited(false);
        score.setScore("-");
        studentMapper.update(student,wrapper);
        return studentCourseMapper.insert(score) == 1;
    }

    private void inherit(ProbationaryPartyMemberStudent student, Integer term) {
        QueryWrapper<ProbationaryPartyMemberStudent> wrapper = new QueryWrapper<>();
        wrapper.eq("sno",student.getSno())
                .eq("term", term - 1);
        ProbationaryPartyMemberStudent last = studentMapper.selectOne(wrapper);
        student.setElectiveCourseNum(false);
        student.setRequiredCourseNum(0);
        //上一期未报名或者已继承则不能继承
        if(last == null || last.getInherited()) {
            student.setInherited(false);
            return;
        }
        student.setInherited(true);
        List<StudentCourseVO> scores = studentCourseMapper.getScoresBySnoAndTerm(student.getSno(), last.getTerm());
        for (StudentCourseVO score : scores) {
                //选修课通过或者必修课成绩大于60则继承
            if("P".equals(score.getScore()) || Integer.parseInt(score.getScore()) > 60) {
                ProbationaryPartyMemberStudentCourse newscore = new ProbationaryPartyMemberStudentCourse();
                newscore.setSno(last.getSno());
                newscore.setTerm(term);
                newscore.setCid(score.getCid());
                newscore.setInherited(true);
                newscore.setDeleted(false);
                newscore.setScore(score.getScore());
                studentCourseMapper.insert(newscore);
                if(score.getType()) {
                    student.setElectiveCourseNum(true);
                } else {
                    student.setRequiredCourseNum(student.getRequiredCourseNum() + 1);
                }
            }
        }
        student.setPaperScore(last.getPaperScore());
        student.setPracticeScore(last.getPracticeScore());
    }
    @Override
    @Transactional
    public Boolean signUp(String sno, Integer term) {
        ProbationaryPartyMemberTrain train = new ProbationaryPartyMemberTrain(trainMapper.selectOne(
                new QueryWrapper<ProbationaryPartyMemberTrain>()
                        .eq("times", term)
                        .eq("user_type",PPMConfig.USERT_TYPE)));
        //未开放报名或者没有这期培训则报名失败
        if(!train.isOpen()) {
            return false;
        }
        return this.lateSignUp(sno,term);
    }

    @Override
    @Transactional
    public Boolean lateSignUp(String sno, Integer term) {
        Boolean isOpen = ProbationaryPartyMemberTrain.transStatus(
                trainMapper.getStatusByTerm(term, PPMConfig.USERT_TYPE), PPMConfig.OPEN);
        //没有term这期培训则失败
        if(isOpen == null) {
            return false;
        }
        ProbationaryPartyMemberStudent student = studentMapper.selectOne(
                new QueryWrapper<ProbationaryPartyMemberStudent>().eq("sno",sno).eq("term",term));
        //已经报名则报名失败
        if(student != null) {
            return false;
        }

        student = new ProbationaryPartyMemberStudent();
        student.setSno(sno);
        student.setTerm(term);
        student.setDeleted(false);
        inherit(student, term);
        return studentMapper.insert(student) == 1;
    }

    @Override
    public Integer countStudentNumberByTerm(Integer term) {
        return studentMapper.selectCount(new QueryWrapper<ProbationaryPartyMemberStudent>().eq("term",term)).intValue();
    }

}
