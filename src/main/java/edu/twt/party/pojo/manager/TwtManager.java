package edu.twt.party.pojo.manager;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import edu.twt.party.constant.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * (TwtManager)表实体类
 *
 * @author makejava
 * @since 2022-10-17 14:39:07
 */
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TwtManager extends Model<TwtManager> {

    @TableId(type = IdType.AUTO)
    private Integer managerId;

    private String managerName;

    private Integer managerAcademy;


    @TableField(value = "manager_type")
    private RoleType role;

    private String managerStatus;

    private String managerColumngrant;

    private Integer imgId;

    private Date lastLogintime;

    private Integer managerIsdeleted;

    private String oldName;

    private String newName;

    private String managerSno;

}

