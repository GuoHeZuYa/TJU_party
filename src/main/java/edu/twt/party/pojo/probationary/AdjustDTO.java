package edu.twt.party.pojo.probationary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdjustDTO {
    private Integer oldCid;
    private Integer newCid;
    private String sno;
    private Integer term;
}
