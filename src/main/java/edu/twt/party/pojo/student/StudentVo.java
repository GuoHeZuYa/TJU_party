package edu.twt.party.pojo.student;

import edu.twt.party.pojo.classPojo.ClassVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: StudentVo
 * @Description:
 * @Author: 过河卒
 * @Date: 2022/9/28 12:48
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StudentVo {
    Integer uid;
    String stuNum;
    String uName;
    ClassVo classVo;
    /**
     * 部分奇怪的majorId有字母
     */
    String majorId;
    String majorName;
    Integer collegeId;
    String collegeName;
    Integer partyBranchId;
    String partyBranchName;

}
