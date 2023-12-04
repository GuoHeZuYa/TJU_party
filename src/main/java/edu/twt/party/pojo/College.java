package edu.twt.party.pojo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.twt.party.pojo.partyBranch.TwtPartybranch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class College extends Model<College> {
    private Integer id;
    private String collegeName;
    private String shortName;
    private String code;
    @JsonIgnore
    private String state;
    @JsonIgnore
    private Integer userCount;
}
