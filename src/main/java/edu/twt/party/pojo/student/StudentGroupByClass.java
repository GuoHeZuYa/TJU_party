package edu.twt.party.pojo.student;

import edu.twt.party.pojo.classPojo.ClassVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StudentGroupByClass {
    ClassVo classVo;
    List<StudentVo> studentList;
}
