package edu.twt.party.pojo.manager;

import edu.twt.party.constant.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: AuthorizeReq
 * @Description:
 * @Author: 过河卒
 * @Date: 2023/1/29 19:48
 * @Version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class AuthorizeReq {
    //授权对象的天外天账号
    String targetNum;
    Integer targetAcademy;
    RoleType roleType;
    //栏目管理员才有，表示管理的栏目
    String managerColumn;
}
