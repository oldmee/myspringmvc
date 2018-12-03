package com.oldmee.service;

import com.oldmee.anotation.Controvice;

/**
 * @Author: R.oldmee
 * @Description:
 * @Date: Create in 13:35 2018/12/1
 */

@Controvice("helloWorldService")
public class HelloWorldService {
    public String say(String name, int age) {
        return "your name is" + name + ", the age is " + age;
    }
}
