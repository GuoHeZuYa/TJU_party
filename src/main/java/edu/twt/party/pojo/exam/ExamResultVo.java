package edu.twt.party.pojo.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExamResultVo extends ExamResult{
    String name;
    String sno;
    String major;
}
