package edu.twt.party.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page {
    private Integer pageTotalCount;
    private Integer itemTotalCount;
}
