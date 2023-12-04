package edu.twt.party.service.probationary.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.twt.party.dao.probationary.ProbationaryPartyMemberCourseMapper;
import edu.twt.party.dao.probationary.ProbationaryPartyMemberTrainMapper;
import edu.twt.party.pojo.exam.Exam;
import edu.twt.party.pojo.probationary.CourseVO;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberCourse;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberTrain;
import edu.twt.party.pojo.probationary.CourseDTO;
import edu.twt.party.service.probationary.ProbationaryPartyMemberCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProbationaryPartyMemberCourseServiceImpl extends ServiceImpl<ProbationaryPartyMemberCourseMapper, ProbationaryPartyMemberCourse>
    implements ProbationaryPartyMemberCourseService {
    @Autowired
    ProbationaryPartyMemberCourseMapper courseMapper;
    @Autowired
    ProbationaryPartyMemberTrainMapper trainMapper;

    @Override
    @Transactional
    public Integer addCourse(CourseDTO courseDTO) {
        ProbationaryPartyMemberCourse course = new ProbationaryPartyMemberCourse();
        course.setDeleted(false);
        course.setLocation(courseDTO.getLocation());
        course.setName(courseDTO.getName());
        course.setStatus(0);
        course.setTeacher(courseDTO.getTeacher());
        course.setStartTime(courseDTO.getStartTime());
        course.setType(courseDTO.getType());
        course.setTerm(courseDTO.getTerm());
        course.setEndTime(courseDTO.getEndTime());

        /*
            当前培训未结束才能添加培训课程
         */
        Exam exam = trainMapper.getExamByTerm(course.getTerm(), PPMConfig.USERT_TYPE);
//        ProbationaryPartyMemberTrain train = new ProbationaryPartyMemberTrain(trainMapper.getExamByTerm(course.getTerm(), PPMConfig.USERT_TYPE));
        if(exam!= null && !ProbationaryPartyMemberTrain.transStatus(exam.getStatus(),PPMConfig.FINISH)) {
            return courseMapper.insert(course);
        } else {
            return 0;
        }
    }

    @Override
    public List<CourseVO> getCourseByTermAndType(Integer term, Boolean type) {
        return courseMapper.getCourseByTermAndType(term, type);
    }
}
