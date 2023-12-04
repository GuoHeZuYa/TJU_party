package edu.twt.party.pojo.excelParse.applicant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantExcelImport {
    private Integer id;
    private String sno;
    private Integer score;
    private String detailScore;
    private Integer status;
    private Integer punish;
    private String comments;
    private Integer userId;
    private String name;
}
