package edu.twt.party.pojo.student;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (BUserinfo)表实体类
 *
 * @author makejava
 * @since 2022-09-11 16:26:10
 */
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class BUserinfo extends Model<BUserinfo> {
    
    private Integer id;
    
    private String usernumb;
    
    private String username;
    
    private Integer type;
    
    private Integer partybranchid;
    
    private Integer departmentid;
    
    private Integer collegeid;
    
    private String province;
    
    private String lastschool;
    
    private String oldcollegeid;
    
    private String major;
    
    private Integer classid;
    
    private String stuintime;
    
    private Integer grade;
    
    private Integer gradeadd;
    
    private String state;
    
    private String politicalface;
    
    private String stucity;
    
    private String majorname;
}

