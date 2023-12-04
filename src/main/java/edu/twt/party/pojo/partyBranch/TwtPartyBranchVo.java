package edu.twt.party.pojo.partyBranch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TwtPartyBranchVo extends TwtPartybranch{
    String collegeName;
}
