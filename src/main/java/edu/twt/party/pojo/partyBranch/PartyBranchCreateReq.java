package edu.twt.party.pojo.partyBranch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartyBranchCreateReq {
    Integer  partybranchAcademy;
    String partybranchSchoolyear;
    /**
     * 0是本科
     * 1是硕士
     * 2是博士
     */
    String partybranchType;
    String partybranchName;
}
