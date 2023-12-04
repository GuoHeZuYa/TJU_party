package edu.twt.party.pojo.probationary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseInfo {
    private CourseVO courseVO;
    private Boolean select;
}
