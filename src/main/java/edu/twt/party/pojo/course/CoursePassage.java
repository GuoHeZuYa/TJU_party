package edu.twt.party.pojo.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoursePassage {
    private int passageId;
    private int courseId;
    private String title; // 文章标题
    private String text; // 文章正文
    private int isDeleted;
    private String createdTime;
    private String updatedTime;
}
