package edu.twt.party.pojo.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamUserList {
    int type;//0-3对应网上党课学习、申请人、积极分子、预备党员培训
    int status;//0未报名默认状态，1已通过，2可报名，3已报名
    int[] extraData;

}
