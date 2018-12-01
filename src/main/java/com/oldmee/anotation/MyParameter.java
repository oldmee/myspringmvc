package com.oldmee.anotation;

import java.lang.annotation.*;

/**
 * @Author: R.oldmee
 * @Description:
 * @Date: Create in 13:31 2018/12/1
 */

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyParameter {
    String value();
}
