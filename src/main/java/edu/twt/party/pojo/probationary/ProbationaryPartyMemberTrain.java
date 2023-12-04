package edu.twt.party.pojo.probationary;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.twt.party.pojo.exam.Exam;
import edu.twt.party.service.probationary.impl.PPMConfig;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("exam_list")
public class ProbationaryPartyMemberTrain extends Exam{
    public ProbationaryPartyMemberTrain(Exam exam) {
        this.setStatus(exam.getStatus());
        this.setTimes(exam.getTimes());
        this.setName(exam.getName());
        this.setContent(exam.getContent());
        this.setCreateAt(exam.getCreateAt());
        this.setDeleteAt(exam.getDeleteAt());
        this.setDeleted(exam.getDeleted());
        this.setEndTime(exam.getEndTime());
        this.setStartTime(exam.getStartTime());
        this.setId(exam.getId());
        this.setUserType(exam.getUserType());
    }
    public ProbationaryPartyMemberTrain() {
        super();
    }
    /* 提取出某一个状态 */
    public static Boolean transStatus(Integer status, Integer s) {
        if(status == null) {
            return null;
        }
        return (status & s) == s;
    }
    /* 把某一个状态合并进总状态 */
    public static Integer mergeStatus(Integer status, Integer s,Boolean flag) {
        if(status == null) {
            return null;
        }
        if(flag) {
            return status | s;
        } else {
            return status & (~s);
        }
    }
    public void setTerm(int term) {
        this.setTimes(term);
    }
    @JsonIgnore
    public int getTerm() {
        return this.getTimes();
    }
    public Boolean isOpen() { //查询能否报名
        return transStatus(this.getStatus(), PPMConfig.OPEN);
    }
    public void setOpen(Boolean isOpen) { // 设置为能报名
        this.setStatus(mergeStatus(this.getStatus(), PPMConfig.OPEN, isOpen));
    }

    @JsonSerialize
    public Boolean canSelect() {
        return transStatus(this.getStatus(), PPMConfig.SELECT);
    }
    public void setSelect(Boolean canSelect) {
        this.setStatus(mergeStatus(this.getStatus(), PPMConfig.SELECT, canSelect));
    }

    public void setFinished(Boolean isFinished) {
        this.setStatus(mergeStatus(this.getStatus(), PPMConfig.FINISH, isFinished));
    }

    public Boolean isFinished() {
        return transStatus(this.getStatus(), PPMConfig.FINISH);
    }
}
