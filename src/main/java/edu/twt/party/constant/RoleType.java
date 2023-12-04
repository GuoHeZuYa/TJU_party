package edu.twt.party.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Guohezu
 */

@Getter
@AllArgsConstructor
public enum RoleType {

    COMMON_USER(0,"common_user"),
    SECRETARY(10,"团支书"),
    ORGANIZER(20,"组织委员"),
    PROPAGATOR(30,"宣传委员"),
    COLUMN_ADMIN(100,"项目管理员"),
    COLLEGE_ADMIN(120,"院级管理员"),
    ROOT(127,"超管");

    @EnumValue
    private final int typeNum;
    @JsonValue
    private final String typeName;


}
