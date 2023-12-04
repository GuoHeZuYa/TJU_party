package edu.twt.party.pojo.classPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * @InterfaceName: TwtPartyGroup
 * @Description:
 * @Author: Guohezu
 * @Date: 2022/9/29 16:54
 * @Version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ClassVo {
    Integer classId;
    String className;
}
