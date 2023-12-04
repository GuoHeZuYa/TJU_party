package edu.twt.party.pojo.studygroup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyGroup {
    int id;
    String name;
    int partyBranchId;
    int status;
    @JsonIgnore
    Date createAt;
    @JsonIgnore
    Date deleteAt;
    @JsonIgnore
    boolean isDelete;
}
