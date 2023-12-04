package edu.twt.party.pojo.course;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CoursePaper {
    int id;
    int courseId;
    int userId;
    String answer;
    String tmpSavedAns;
    String paperContent;

    public List<Integer> getAnswerList() {
        return JSON.parseArray(this.answer).toList(Integer.class);
    }
}
