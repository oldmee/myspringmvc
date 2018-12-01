package com.oldmee.anotation;

import java.lang.annotation.*;

/**
 * @Author: R.oldmee
 * @Description:
 * @Date: Create in 13:30 2018/12/1
 */

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAutoWire {
    String value();
}
