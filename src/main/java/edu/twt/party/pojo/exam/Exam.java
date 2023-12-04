package edu.twt.party.pojo.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
    private Integer id;
    private String name;
    private int times;
    private Date startTime;
    private Date endTime;
    @JsonIgnore
    private Date deleteAt;
    @JsonIgnore
    private Date createAt;
    @JsonIgnore
    private Boolean deleted;
    private String content;
    private int userType;
    private Integer status;
}
