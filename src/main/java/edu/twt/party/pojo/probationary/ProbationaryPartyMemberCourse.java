package edu.twt.party.pojo.probationary;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProbationaryPartyMemberCourse {
    private Integer id;
    private String name;
    private Integer term;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date endTime;
    private String location;
    private Integer status;
    private Boolean type;//0 必修 1 选修
    @JsonIgnore
    private Date UpdateTime;
    @JsonIgnore
    @TableLogic
    private Boolean deleted = false;
    private String teacher;
}
