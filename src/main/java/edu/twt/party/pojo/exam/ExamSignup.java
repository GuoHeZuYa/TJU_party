package edu.twt.party.pojo.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamSignup {
    private int id;
    private String sno;
    private String name;
    private Integer academyId;
    private String academyName;

    private int examId;
    private int userId;
    private Date signupTime;
    @JsonIgnore
    private boolean isCancel;
    @JsonIgnore
    private Date createAt;
    @JsonIgnore
    private Date deleteAt;
    @JsonIgnore
    private boolean deleted;
}
