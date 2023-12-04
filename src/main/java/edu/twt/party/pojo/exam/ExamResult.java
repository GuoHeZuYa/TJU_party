package edu.twt.party.pojo.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResult {
    private Integer id;
    private int examId;
    private Integer userId;
    private int score;
    private String detailScore;
    private int status;
    @JsonIgnore
    private Date createAt;
    private String comments;
    //for 批量插入
    private String sno;

}
