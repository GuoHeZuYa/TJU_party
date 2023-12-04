package edu.twt.party.pojo.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExamPunish {
    int id;
    Integer userId;
    Integer examId;
    Integer punishType;
    Integer punishTimes;
}
