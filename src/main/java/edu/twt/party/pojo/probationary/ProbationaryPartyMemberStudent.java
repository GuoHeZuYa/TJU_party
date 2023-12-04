package edu.twt.party.pojo.probationary;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProbationaryPartyMemberStudent {
    private String sno;
    private Integer term;
    private Integer paperScore;
    private Integer practiceScore;
    private Integer requiredCourseNum = 0;
    private Boolean inherited = false;
    @TableLogic
    private Boolean deleted = false;
    private Boolean electiveCourseNum = false;
}
