package edu.twt.party.pojo.probationary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseVO {
    private Integer cid;
    private Integer term;
    private String courseName;
    private Boolean type;
    private Boolean inherited;
    String score;
    private Integer courseStatus;
}
