package edu.twt.party.pojo.userProcess;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProcess {
    private Integer id;
    private Integer userId;
    private Integer processId;
    private Integer status;
    @JsonIgnore
    private Data createAt;
    @JsonIgnore
    private Data deleteAt;
    @JsonIgnore
    private Boolean isDeleted;
}
