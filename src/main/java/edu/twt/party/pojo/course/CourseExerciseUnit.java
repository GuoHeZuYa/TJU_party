package edu.twt.party.pojo.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseExerciseUnit {
    private int exerciseId;
    private int courseId;
    private String question;
    private int exerciseType;  // 题目类型；0表示单选，1表示多选
    private int value;  // 题目分值
    private List<ExerciseOption> options; // 题目选项
}
