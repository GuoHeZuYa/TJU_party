package edu.twt.party.pojo.partyBranch;

import edu.twt.party.pojo.user.NameSno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName: batchUpdateBranchReq
 * @Description:
 * @Author: 过河卒
 * @Date: 2022/12/4 17:46
 * @Version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class BatchUpdateBranchReq {
    List<NameSno> nameSnoList;
    Integer targetBranchId;
}
