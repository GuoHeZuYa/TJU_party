package edu.twt.party.annotation;




import edu.twt.party.constant.RoleType;
import org.apache.commons.compress.utils.Lists;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

/**
 * @author Guohezu
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtToken {
    boolean required() default true;
    RoleType[] roles() default {
            RoleType.COMMON_USER,
            RoleType.SECRETARY,
            RoleType.ORGANIZER,
            RoleType.PROPAGATOR,
            //栏目管理
            RoleType.COLUMN_ADMIN,
            //学院管理员
            RoleType.COLLEGE_ADMIN,
            //超管就是校级管理员
            RoleType.ROOT
    };
    //对于支书、组织委员、宣传委员、学院管理员生效,
    Integer collegeId = 0;

    //对栏目管理员生效
    List<Integer> collumnId = Lists.newArrayList();
    boolean onlyNeedId() default false;
}
