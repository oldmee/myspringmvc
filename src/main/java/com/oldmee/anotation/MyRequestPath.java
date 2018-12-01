package com.oldmee.anotation;

import java.lang.annotation.*;

/**
 * @Author: R.oldmee
 * @Description:
 * @Date: Create in 13:27 2018/12/1
 */

@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyRequestPath {
    String value();
}
