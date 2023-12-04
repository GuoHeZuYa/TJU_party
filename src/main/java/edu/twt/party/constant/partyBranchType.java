package edu.twt.party.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName: partyBranchType
 * @Description:
 * @Author: 过河卒
 * @Date: 2022/11/20 19:39
 * @Version: 1.0
 */
@AllArgsConstructor
@Getter
public enum partyBranchType {

    undergraduate(1,"本科"),
    master(2,"硕士"),
    doctor(3,"博士");



    private Integer type;
    @JsonValue
    private String name;


}
