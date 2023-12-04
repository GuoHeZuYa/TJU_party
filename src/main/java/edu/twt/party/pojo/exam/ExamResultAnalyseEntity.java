package edu.twt.party.pojo.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResultAnalyseEntity {
    int id;
    String collegename;
    int passNum;
    int absentNum;
    int cheatNum;
    int allNum;
    float passRatePresent;
    float PassRateAll;
    float absentRate;
    int presentNum;

    public float getPassRatePresent() {
        return 1.0f*getPassNum()/getPresentNum();
    }

    public float getPassRateAll() {
        return 1.0f*getPassNum()/getAllNum();
    }

    public float getAbsentRate() {
        return 1.0f*getAbsentNum()/getAllNum();
    }
    public int getPresentNum() {
        return getAllNum()-getAbsentNum();
    }

}
