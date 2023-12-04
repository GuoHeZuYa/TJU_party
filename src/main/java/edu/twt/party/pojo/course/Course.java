package edu.twt.party.pojo.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xsr
 * “二十课”课程
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private int courseId;
    private String courseName; // 课程名称
    private String courseDetail; // 课程信息
    private int position; // 课程顺序
    private String createdTime;
    private String updatedTime;
    private int isDeleted;
}
