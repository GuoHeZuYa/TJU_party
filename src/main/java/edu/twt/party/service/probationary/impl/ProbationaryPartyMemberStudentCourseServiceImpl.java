package edu.twt.party.service.probationary.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.twt.party.dao.probationary.ProbationaryPartyMemberCourseMapper;
import edu.twt.party.dao.probationary.ProbationaryPartyMemberStudentCourseMapper;
import edu.twt.party.pojo.probationary.*;
import edu.twt.party.service.probationary.ProbationaryPartyMemberStudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProbationaryPartyMemberStudentCourseServiceImpl extends ServiceImpl<ProbationaryPartyMemberStudentCourseMapper, ProbationaryPartyMemberStudentCourse>
    implements ProbationaryPartyMemberStudentCourseService {
    @Autowired
    private ProbationaryPartyMemberStudentCourseMapper studentCourseMapper;
    @Autowired
    private ProbationaryPartyMemberCourseMapper courseMapper;
    @Override
    public List<ScoreVO> getStudentListByTermAndCid(Integer term, Integer cid) {
        List<ScoreVO> scores = new ArrayList<>();
        QueryWrapper<ProbationaryPartyMemberStudentCourse> wrapper = new QueryWrapper<>();
        wrapper .eq("term", term)
                        .eq("cid", cid);
        for (ProbationaryPartyMemberStudentCourse studentCourse : studentCourseMapper.selectList(wrapper)) {
            ScoreVO scoreVO = new ScoreVO();
            scoreVO.setScore(studentCourse.getScore());
            scoreVO.setSno(studentCourse.getSno());
            scoreVO.setCid(cid);
            scoreVO.setTerm(term);
            scores.add(scoreVO);
        }
        return scores;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateScore(ScoreVO score) {
        QueryWrapper<ProbationaryPartyMemberStudentCourse> wrapper = new QueryWrapper<>();
        wrapper .eq("sno", score.getSno())
                .eq("cid", score.getCid())
                .eq("term", score.getTerm())
                .eq("deleted", false);
        ProbationaryPartyMemberStudentCourse  studentCourse = studentCourseMapper.selectOne(wrapper);
        studentCourse.setScore(score.getScore());
        return studentCourseMapper.update(studentCourse, wrapper) == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean adjustCourse(AdjustDTO adjustDTO) {
        QueryWrapper<ProbationaryPartyMemberStudentCourse> wrapper = new QueryWrapper<>();
        wrapper .eq("term", adjustDTO.getTerm())
                .eq("sno", adjustDTO.getSno())
                .eq("cid", adjustDTO.getOldCid())
                .eq("deleted", false);

        ProbationaryPartyMemberStudentCourse score = studentCourseMapper.selectOne(wrapper);
        //如果出分了或者没有选这门课
        if(score == null || !"-".equals(score.getScore())) {
            return false;
        }
        //调的课已经开始上了不能调课
        if(courseMapper.selectById(adjustDTO.getNewCid()).getStartTime().getTime() < System.currentTimeMillis()) {
            return false;
        }
        score.setCid(adjustDTO.getNewCid());
        return studentCourseMapper.update(score,wrapper) == 1;
    }

    @Override
    public List<StudentCourseInfo> getStudentCourseInfoByTerm(String sno, Integer term) {
        //获取当前期数的所有课程
        List<CourseVO> courses= courseMapper.getCourseByTerm(term,false);
        //获取当前期数下学生的选课情况
        QueryWrapper<ProbationaryPartyMemberStudentCourse> wrapper = new QueryWrapper<>();
        wrapper .eq("term",term)
                .eq("sno",sno);
        List<ProbationaryPartyMemberStudentCourse> studentCourses= studentCourseMapper.selectList(wrapper);
        List<StudentCourseInfo> result = new ArrayList<>();
        for (CourseVO course: courses) {
            StudentCourseInfo vo = new StudentCourseInfo(course, false);
            for (ProbationaryPartyMemberStudentCourse stuCourse : studentCourses) {
                if(vo.getCourseVO().getId().equals( stuCourse.getCid())) {
                    vo.setSelect(true);
                    break;
                }
            }
            result.add(vo);
        }
        return result;
    }
}
