package edu.twt.party.pojo.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xsr
 * 二十课对应的测试题目
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseExercise {
    private int exerciseId;
    private int courseId;
    private String question;
    private int exerciseType;  //题目类型；0表示单选，1表示多选
    private int value;  //题目分值
    private int exerciseAnswer; //题目答案，用五位二进制表示，第一到五位分别表示A到E
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String optionE;
    private String createdTime;
    private String updatedTime;
    private int is_deleted;
}
