package edu.twt.party.pojo.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseOption {
    @JsonIgnore
    private  int markId;
    private String option;
    private int isAns;
}
