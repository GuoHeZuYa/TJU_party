package edu.twt.party.pojo.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollegeActivistTrain {
    @JsonIgnore
    private Integer id;
    private Integer collegeId;
    private String collegeName;
    private Integer examId;
    private Integer status;
    @JsonIgnore
    private Boolean is_delete;
    @JsonIgnore
    private Data createAt;
    @JsonIgnore
    private Data deleteAt;

}
