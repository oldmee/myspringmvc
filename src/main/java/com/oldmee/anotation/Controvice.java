package com.oldmee.anotation;

import org.omg.SendingContext.RunTime;

import java.lang.annotation.*;

/**
 * @Author: R.oldmee
 * @Description:
 * @Date: Create in 10:25 2018/12/1
 */

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controvice {
    String value();
}
