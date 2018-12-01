package com.oldmee.controller;

import com.oldmee.anotation.Controvice;
import com.oldmee.anotation.MyAutoWire;
import com.oldmee.anotation.MyParameter;
import com.oldmee.anotation.MyRequestPath;
import com.oldmee.service.HelloWorldService;

/**
 * @Author: R.oldmee
 * @Description:
 * @Date: Create in 17:52 2018/12/1
 */

@Controvice("myControllerTest")
public class MyControllerTest {

    @MyAutoWire("helloWorldService")
    private HelloWorldService helloWorldService;

    @MyRequestPath("/hello")
    public void hello(@MyParameter String name, @MyParameter int age) {
        System.out.println("name:" + name + ", age:" + age);
    }

}
