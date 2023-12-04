package edu.twt.party.token;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LoginToken {
    boolean checkPrivate() default false;
}
