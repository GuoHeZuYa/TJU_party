package edu.twt.party.pojo.classPojo;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @InterfaceName: TwtPartyGroup
 * @Description:
 * @Author: Guohezu
 * @Date: 2022/9/29 16:46
 * @Version: 1.0
 */
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class BClass extends Model<BClass> {
    
    private Integer id;
    
    private String classname;
    
    private Integer collegeid;
    
    private Integer collegecode;
    
    private Integer grade;
    
    private String state;
    
    private Integer studentcount;


}

