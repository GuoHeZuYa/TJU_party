package edu.twt.party.pojo.userProcess;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MainProcessFilterResult {
    Integer userId;
    String sno;
    String userName;
    Integer mainProcess;
}
