package edu.twt.party.service.course.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import edu.twt.party.dao.course.CourseMapper;
import edu.twt.party.exception.APIException;
import edu.twt.party.pojo.course.*;
import edu.twt.party.service.course.CourseServise;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author xsr
 * "20课时"相关（包括测试）
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class CourseServiceImpl implements CourseServise {
    @Resource
    CourseMapper courseMapper;

    /**
     * 在最后添加新的课程
     *
     * @param courseName   课程名称
     * @param courseDetail 课程描述
     * @return Boolean
     */
    @Override
    public Boolean addCourse(String courseName, String courseDetail) throws APIException {
        try {
            List<Course> presentList = courseMapper.getCourse();
            return courseMapper.addCourse(courseName, courseDetail,presentList.size() + 1) == 1;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("添加课程错误");
        }
    }

    /**
     * 在 position 后的位置添加课程
     *
     * @param courseName   课程名称
     * @param courseDetail 课程描述
     * @param position     要添加在后面的位置
     * @return Boolean
     */
    @Override
    public Boolean addCourseAfter(String courseName, String courseDetail, int position) {
        try {
            List<Course> presentList = courseMapper.getCourse();
            if (position > presentList.size() || position <= 0)
                throw new APIException("非法的添加位置");
            for (Course i : presentList) {
                if (i.getPosition() > position)
                    courseMapper.alterCoursePosition(i.getPosition() + 1, i.getCourseId());
            }
            return courseMapper.addCourse(courseName, courseDetail, position + 1) == 1;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("添加课程错误");
        }
    }

    /**
     * 修改课程信息（不包括课程文本）
     *
     * @param courseId        课程id
     * @param newCourseName   修改后的课程名称
     * @param newCourseDetail 修改后的课程描述
     * @return Boolean
     */
    @Override
    public Boolean alterCourseInfo(int courseId, String newCourseName, String newCourseDetail) {
        try {
            return courseMapper.alterCourseInfo(courseId, newCourseName, newCourseDetail) == 1;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("修改课程信息错误");
        }
    }

    /**
     * 删除课程
     *
     * @param courseId 课程id
     * @return Boolean
     */
    @Override
    public Boolean deleteCourse(int courseId) {
        try {
            int pos = courseMapper.getCourseById(courseId).getPosition();
            int a = courseMapper.deleteCourseExerciseByCourseId(courseId);
            int b = courseMapper.deleteCoursePassageByCourseId(courseId);
            int c = courseMapper.deleteCourse(courseId);
            List<Course> presentList = courseMapper.getCourse();
            for (Course i : presentList) {
                if (i.getPosition() > pos) {
                    courseMapper.alterCoursePosition(i.getPosition() - 1, i.getCourseId());
                }
            }
            return a > 0 && b > 0 && c > 0;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("删除课程错误");
        }
    }

    /**
     * 添加课程习题
     *
     * @param exercise       习题的实体类
     * @return Boolean
     */
    @Override
    public Boolean addCourseExercise(CourseExerciseUnit exercise) {
        try {
            int answer = 0;
            String[] option = {null, null, null, null, null};
            List<ExerciseOption> optionList = exercise.getOptions();
            for (int i = 0; i < optionList.size(); ++i) {
                answer += (optionList.get(i).getIsAns() << (4 - i));
                option[i] = optionList.get(i).getOption();
            }
            return courseMapper.addCourseExercise(exercise.getCourseId(), exercise.getExerciseType(), exercise.getQuestion(),
                    exercise.getValue(), answer, option[0], option[1], option[2],
                    option[3], option[4]) == 1;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("添加课程习题错误");
        }
    }

    /**
     * 修改课程习题
     *
     * @param newExercise       修改后的习题内容
     * @return Boolean
     */
    @Override
    public Boolean alterCourseExercise(CourseExerciseUnit newExercise) {
        try {
            int answer = 0;
            String[] option = {null, null, null, null, null};
            List<ExerciseOption> optionList = newExercise.getOptions();
            for (int i = 0; i < optionList.size(); ++i) {
                answer += (optionList.get(i).getIsAns() << (4 - i));
                option[i] = optionList.get(i).getOption();
            }
            return courseMapper.alterCourseExercise(newExercise.getExerciseType(), newExercise.getValue(), answer,
                    option[0], option[1], option[2],
                    option[3], option[4], newExercise.getExerciseId()) == 1;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("修改课程习题错误");
        }
    }

    /**
     * 删除课程习题
     *
     * @param exerciseIdList 要删除的课程习题 id 列表
     * @return Boolean
     */
    @Override
    public Boolean deleteCourseExercise(List<Integer> exerciseIdList) {
        try {
            for (Integer i : exerciseIdList) {
                courseMapper.deleteCourseExercise(i);
            }
            return true;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("删除课程错误");
        }
    }

    /**
     * 获取课程对应的习题列表
     *
     * @param courseId 课程id
     * @return list
     */
    @Override
    public List<CourseExerciseUnit> getCourseExercise(int courseId) {
        try {
            List<CourseExercise> exerciseList = courseMapper.getCourseExercise(courseId);
            List<CourseExerciseUnit> finalList = new ArrayList<>();
            for (CourseExercise i : exerciseList) {
                CourseExerciseUnit newExercise = new CourseExerciseUnit(i.getExerciseId(), i.getCourseId(),
                        i.getQuestion(), i.getExerciseType(), i.getValue(), null);
                newExercise.setOptions(new ArrayList<>());
                if (i.getOptionA() != null) {
                    newExercise.getOptions().add(new ExerciseOption(1,i.getOptionA(), (i.getExerciseAnswer()>>4)&0x1));
                }
                if (i.getOptionB() != null) {
                    newExercise.getOptions().add(new ExerciseOption(2,i.getOptionB(), (i.getExerciseAnswer()>>3)&0x1));
                }
                if (i.getOptionC() != null) {
                    newExercise.getOptions().add(new ExerciseOption(3,i.getOptionC(), (i.getExerciseAnswer()>>2)&0x1));
                }
                if (i.getOptionD() != null) {
                    newExercise.getOptions().add(new ExerciseOption(4,i.getOptionD(), (i.getExerciseAnswer()>>1)&0x1));
                }
                if (i.getOptionE() != null) {
                    newExercise.getOptions().add(new ExerciseOption(5,i.getOptionE(), (i.getExerciseAnswer())&0x1));
                }
                finalList.add(newExercise);
            }
            return finalList;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("获取习题错误");
        }
    }

    /**
     * 获取课程列表
     *
     * @return list
     */
    @Override
    public List<Course> getCourse() {
        try {
            return courseMapper.getCourse();
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("获取课程错误");
        }
    }

    /**
     * 为课程添加文章
     *
     * @param courseId 课程id
     * @param title    课程标题
     * @param text     文章对应的路径
     * @return Boolean
     */
    @Override
    public Boolean addCoursePassage(int courseId, String title, String text) {
        try {
            return courseMapper.addCoursePassage(courseId, text, title) == 1;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("添加课程文章错误");
        }
    }

    /**
     * 修改课程文章标题
     *
     * @param passageId 课程文章 id
     * @param newTitle 新的标题
     * @return Boolean
     */
    @Override
    public Boolean alterCoursePassageTitle(int passageId, String newTitle) {
        try {
            return courseMapper.alterCoursePassageTitle(passageId, newTitle) == 1;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("修改课程文章标题错误");
        }
    }

    /**
     * 修改课程文章正文
     *
     * @param passageId 课程文章 id
     * @param newText  正文
     * @return Boolean
     */
    @Override
    public Boolean alterCoursePassageText(int passageId, String newText) {
        try {
            return courseMapper.alterCoursePassageText(passageId, newText) == 1;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("修改课程文章正文错误");
        }
    }

    /**
     * 删除课程的文章
     *
     * @param passageIdList 要删除的id列表
     * @return Boolean
     */
    @Override
    public Boolean deleteCoursePassage(List<Integer> passageIdList) {
        try {
            for (Integer i : passageIdList) {
                courseMapper.deleteCoursePassage(i);
            }
            return true;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("删除课程文章错误");
        }
    }

    /**
     * 获取某一课程的所有文章
     *
     * @param courseId 要查询的课程id
     * @return List <CoursePassage>
     */
    @Override
    public List<CoursePassage> getCoursePassageList(int courseId) {
        try {
            return courseMapper.getCoursePassage(courseId);
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("获取课程文章列表错误");
        }
    }
    @Override
    public List<CoursePassage> getCoursePassageIds(int courseId) {
        try {
            return courseMapper.getCoursePassageIds(courseId);
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("获取课程文章列表错误");
        }
    }

    @Override
    public CoursePassage getCoursePassage(int id) {
        return courseMapper.getSinglePassage(id);
    }

    /**
     * 上传答案
     *
     * @param userId   用户id
     * @param courseId 课程id
     * @param answer   答案
     * @return Boolean
     */
    @Override
    public Boolean uploadExamAnswer(int userId, int courseId, String answer) {
        try {
            return null;
        } catch(APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("上传答案错误");
        }
    }

//    private int judge(List<CourseAnswer> courseAnswerList){
//        int score = 0;
//        for(CourseAnswer i: courseAnswerList){
//            CourseExerciseAnswer temp = courseMapper.getCourseExerciseAnswerByExerciseId(i.getExerciseId());
//            if(temp.getExerciseAnswer() == i.getExerciseAnswer()){
//                score += temp.getValue();
//            }
//        }
//        return score;
//    }
//
//    @Override
//    public List<CourseExercise> getCourseExercise(int courseId) {
//        List<CourseExercise> exerciseList = courseMapper.getExerciseByCourseId(courseId);
//        return exerciseList;
//    }
//
//    @Override
//    public boolean uploadExamAnswer(int userId, int courseId, String answer) {
//        List<CourseExercise> exerciseList = courseMapper.getExerciseByCourseId(courseId);
//        //解析json
//        JSONArray jsonList = JSONArray.parseArray(answer);
//        List<CourseAnswer> courseAnswerList = new ArrayList<>();
//        for (int i = 0; i < jsonList.size(); ++i) {
//            courseAnswerList.add(JSON.parseObject(jsonList.getString(i), CourseAnswer.class));
//        }
//        int score = judge(courseAnswerList);
//        courseMapper.uploadExamAnswer(courseId, userId, score, answer);
//        return true;
//    }

    @Override
    public List<CourseExerciseUnit> getUserCourseExercise(int userId,int courseId) {
        CoursePaper coursePaper = courseMapper.getPaperAnsSaved(userId,courseId);
        if(coursePaper!=null){
            return getUserCourseExerciseSaved(userId,courseId,coursePaper);
        }else {
            return getUserCourseExerciseNew(userId,courseId);
        }
    }
    public List<CourseExerciseUnit> getUserCourseExerciseNew(int userId,int courseId) {
        List<CourseExerciseUnit> courseExerciseUnits = getCourseExercise(courseId);
        ArrayList<Integer> answer = new ArrayList<>();
        Collections.shuffle(courseExerciseUnits);//打乱题目顺序
        for (CourseExerciseUnit item : courseExerciseUnits) {
            List<ExerciseOption> options = item.getOptions();
            Collections.shuffle(options);//打乱选项顺序
            int ans = 0;
            for (ExerciseOption option: options) {//生成一个答案列表
                if(option.getIsAns()==1){
                    ans+=1;
                }
                ans = ans << 1;
                option.setIsAns(0);//抹去是否是答案
            }
            answer.add(ans);
        }
        JSONArray paperDescription = new JSONArray();
        for (CourseExerciseUnit item : courseExerciseUnits) {
            List<ExerciseOption> options = item.getOptions();
            int optionDescription = 0;
            for (ExerciseOption option: options) {
                optionDescription+=option.getMarkId();
                optionDescription*=10;
            }
            JSONObject itemDescription = new JSONObject();
            itemDescription.put("k",item.getExerciseId());
            itemDescription.put("v",optionDescription);
            paperDescription.add(itemDescription);
        }
        courseMapper.addPaper(userId,courseId, JSON.toJSONString(answer),paperDescription.toJSONString());
        return courseExerciseUnits;
    }
    public List<CourseExerciseUnit> getUserCourseExerciseSaved(int userId,int courseId,CoursePaper coursePaper) {
        JSONObject[] paperContent = JSON.parseArray(coursePaper.getPaperContent()).toArray(JSONObject.class);
        List<CourseExercise> courseExerciseList = courseMapper.getCourseExercise(courseId);
        List<CourseExerciseUnit> finalList = new ArrayList<>();
        List<Integer> savedAnsList = JSON.parseArray(coursePaper.getTmpSavedAns()).toList(Integer.class);
        int count = 0;
        try{
            for (JSONObject item : paperContent) {
                int exerciseId = (Integer) item.get("k");
                for (CourseExercise exercise : courseExerciseList) {
                    if(exercise.getExerciseId()==exerciseId){
                        CourseExerciseUnit courseExerciseUnit = new CourseExerciseUnit(exerciseId,exercise.getCourseId(),exercise.getQuestion(), exercise.getExerciseType(), exercise.getValue(),new ArrayList<ExerciseOption>());
                        List<ExerciseOption> options = courseExerciseUnit.getOptions();
                        int optionDescription = (Integer) item.get("v");
                        int savedAns = 0;
                        if(savedAnsList.size() > count){
                            savedAns = savedAnsList.get(count);
                        }
                        while (optionDescription>0){
                            optionDescription/=10;
                            savedAns = savedAns>>1;
                            int markId = optionDescription%10;
                            if(markId==1){
                                options.add(new ExerciseOption(markId, exercise.getOptionA(), savedAns&0x1));
                            }else if(markId==2){
                                options.add(new ExerciseOption(markId, exercise.getOptionB(), savedAns&0x1));
                            }else if(markId==3){
                                options.add(new ExerciseOption(markId, exercise.getOptionC(), savedAns&0x1));
                            }else if(markId==4){
                                options.add(new ExerciseOption(markId, exercise.getOptionD(), savedAns&0x1));
                            }else if(markId==5){
                                options.add(new ExerciseOption(markId, exercise.getOptionE(), savedAns&0x1));
                            }
                        }
                        Collections.reverse(options);
                        finalList.add(courseExerciseUnit);
                        count++;
                    }
                }
            }
            if(count == courseExerciseList.size()){//题库是否相同
                return finalList;
            }else {
                courseMapper.updatePaperStatus(userId,courseId,3);
                return getUserCourseExerciseNew(userId, courseId);
            }
        }catch (Exception exception){
            //这里还需要把原来的卷子删掉
            courseMapper.updatePaperStatus(userId,courseId,3);
            return getUserCourseExerciseNew(userId, courseId);
        }
        
    }

    @Override
    public Boolean saveUserCourseExercise(int userId, int courseId, List<Integer> answer) {
        return courseMapper.savePaperAns(userId,courseId,JSON.toJSONString(answer));
    }

    @Override
    public CourseExerciseResult submitUserCourseExercise(int userId, int courseId, List<Integer> answer) {
        CoursePaper paper = courseMapper.getPaperAns(userId, courseId);
        List<Integer> correctAnsList = paper.getAnswerList();
        if(correctAnsList.size() != answer.size()){
            throw new APIException("答案数量不正确");
        }
        courseMapper.savePaperAns(userId,courseId,JSON.toJSONString(answer));
        int correct = 0;
        ArrayList<Boolean> detail = new ArrayList<>();
        for(int i=0;i<correctAnsList.size();i++){
            if(correctAnsList.get(i).equals(answer.get(i))){
                correct++;
                detail.add(Boolean.TRUE);
            }else{
                detail.add(Boolean.FALSE);
            }
        }
        int score = 100*correct/correctAnsList.size();
        CourseExerciseResult courseExerciseResult = new CourseExerciseResult(score,score>=60,detail);
        courseMapper.addExerciseResult(userId,courseId,paper.getId(),score,courseExerciseResult.getPass(),1);
        courseMapper.updatePaperStatus(userId,courseId,1);
        return courseExerciseResult;
    }
    @Override
    public List<CourseUser> getUserCourse(int userId,int startTimes){
        if(startTimes == -1){
            startTimes = courseMapper.getLastApplicantTimes()-1;
            if(startTimes<1)startTimes=1;
        }
        return courseMapper.getUserCourse(userId,startTimes);
    }

    @Override
    public Boolean isPassAllCourse(int userId) {
        List<CourseUser> courses = getUserCourse(userId,-1);
        for (CourseUser courseUser:courses) {
            if(!courseUser.isPass())return false;
        }
        return true;
    }

    @Override
    public Boolean sortCourses(int courseId, int to) {
        List<Course> courses = getCourse();
        int from = -1;
        for (Course item :
                courses) {
            if(item.getCourseId() == courseId){
                from = item.getPosition();
            }
        }
        if(from == -1)return false;
        if(from > to){//前移+1
            courseMapper.posMoveBefore(from,to);
        }else if(from < to){//后移-1
            courseMapper.posMoveAfter(from,to);
        }else {
            return false;
        }
        return courseMapper.alterCoursePosition(to, courseId)>0;
    }
}
