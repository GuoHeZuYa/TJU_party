package edu.twt.party.pojo.userProcess;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum UserProcessType {
    APPLICANT(0,"入党申请人"),
    ACTIVIST(1,"积极分子"),
    DEVELOP(2,"发展对象"),
    PROBATIONARY(3,"预备党员"),
    PARTY_MEMBER(4,"党员"),
    NONE(-1,"");
    @EnumValue
    private final int id;
    @JsonValue
    private final String name;
}
