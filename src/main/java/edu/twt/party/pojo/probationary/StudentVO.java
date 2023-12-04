package edu.twt.party.pojo.probationary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentVO {
    private String sno;
    private Integer term;
    private Integer paperScore;
    private Integer practiceScore;
    private List<StudentCourseVO> courses;
}
