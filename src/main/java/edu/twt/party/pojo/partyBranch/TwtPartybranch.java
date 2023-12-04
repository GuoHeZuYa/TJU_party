package edu.twt.party.pojo.partyBranch;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import edu.twt.party.pojo.student.UserInfoBasic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * (TwtPartybranch)表实体类
 *
 * @author makejava
 * @since 2022-09-11 14:58:16
 */
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TwtPartybranch extends Model<TwtPartybranch> {


    @TableId(type = IdType.AUTO)
    private Integer partybranchId;

    private String partybranchName;

    //以下三项均为uid,故是Integer
    //支书
    private UserInfoBasic partybranchSecretary;
    //组织委员
    private UserInfoBasic partybranchOrganizer;
    //宣传委员
    private UserInfoBasic partybranchPropagator;

    private Integer partybranchAcademy;

    private String partybranchType;

    private String partybranchSchoolyear;

    private Date partybranchEstablishtime;

    //支部删除操作...
    private Integer partybranchIsdeleted;
    //这是什么
    private Integer partybranchTotalScore;

    private Integer partybranchTotalReply;


    private Integer partybranchTotalTopic;

    private Integer partybranchTotalAct;
    //正式党员数量
    private Integer regularCount;
    //预备党员
    private Integer prepareCount;
    //发展对象人数
    private Integer developCount;
    //总人数
    private Integer totalCount;
    //积极分子+团推优的总人数
    private Integer greatActiveCount;
    //积极分子
    private Integer activeCount;
    //申请人和非申请人数量
    private Integer applicantCount;

    private Integer greatCount;



    //原本是使用123对应,现在直接用字符串存储
    //后续可以改成枚举类
    public TwtPartybranch format(){
        if("1".equals(this.getPartybranchType())){
            this.setPartybranchType("本科");
        }else if ("2".equals(this.getPartybranchType())){
            this.setPartybranchType("硕士");
        }else if ("3".equals(this.getPartybranchType())){
            this.setPartybranchType("博士");
        }else{
            this.setPartybranchType("其他");
        }
        return this;
    }

}

