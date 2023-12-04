package edu.twt.party.pojo.probationary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreVO {
    private String sno;
    private Integer term;
    private Integer cid;
    private String score;

}
