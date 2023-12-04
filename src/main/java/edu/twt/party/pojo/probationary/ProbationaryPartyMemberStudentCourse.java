package edu.twt.party.pojo.probationary;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProbationaryPartyMemberStudentCourse {
    private String sno;
    private Integer cid;
    @TableLogic
    private Boolean deleted = false;
    private Boolean inherited = false;
    private String score;//选修成绩为P或NP 必修为数字
    private int term;
}
