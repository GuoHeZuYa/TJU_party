package edu.twt.party.pojo.partyBranch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @InterfaceName: TwtPartyGroup
 * @Description:
 * @Author: Guohezu
 * @Date: 2022/9/27 14:46
 * @Version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class PartyBranchGroupVo {
    Integer collegeId;
    String collegeName;
    Integer branchCount;
    List<TwtPartyBranchVo> list;
}
