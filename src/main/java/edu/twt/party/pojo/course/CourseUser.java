package edu.twt.party.pojo.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseUser {
    int courseId;
    String courseName;
    String courseDetail;
    int position;
    boolean isPass;
    Integer lastPassTime;
}
