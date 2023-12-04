package edu.twt.party.pojo.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseExerciseResult {
    Integer score;
    Boolean pass;
    ArrayList<Boolean> detail;
}
