package edu.twt.party.pojo.student;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * (TwtStudentInfo)表实体类
 *
 * @author makejava
 * @since 2022-09-08 11:09:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("twt_student_info")
@Accessors(chain = true)
public class TwtStudentInfo extends Model<TwtStudentInfo> {

    @TableId
    private Integer id;
    
    private String sno;
    
    private Integer academyId;
    
    private Integer partybranchId;
    
    private Integer isPass20;
    
    private Date pass20Time;
    
    private Integer isClear20;
    
    private Integer lockedTestId;
    
    private Integer applicantIslocked;
    
    private Integer applicantFailedtimes;
    
    private String captainOfgroup;
    
    private Date applyGrouptime;
    
    private Date activeRoletime;
    
    private Date groupExectime;
    
    private Date developTargettime;
    
    private Date allTraintime;
    
    private Date dataCompletetime;
    
    private Date reportTime;
    
    private Date devShowStarttime;
    
    private Date votePasstime;
    
    private Date talkPasstime;
    
    private Date probPassedtime;
    
    private Date activityPassetime;
    
    private Date realShowStarttime;
    
    private Date turnRealMeetingtime;
    
    private Date approvePassedtime;
    
    private Date partymemberTime;
    
    private Integer thoughtReportcount;
    
    private Integer personalReportcount;
    
    private Integer mainStatus;
    
    private Integer isInit;





}

