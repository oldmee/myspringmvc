package com.oldmee.dispatchServlet;

import com.oldmee.anotation.Controvice;
import com.oldmee.anotation.MyAutoWire;
import com.oldmee.anotation.MyParameter;
import com.oldmee.anotation.MyRequestPath;
import com.oldmee.controller.MyControllerTest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: R.oldmee
 * @Description:
 * @Date: Create in 14:14 2018/12/1
 */
public class MyDisPatchServlet extends HttpServlet {

    private List<String> classNameWithPackageName = new ArrayList<String>();
    private Map<String, Object> beans = new HashMap();
    private Map<String, Object> urlControllers = new HashMap<>();
    private Map<String, Object> urlMethods = new HashMap<>();

    @Override
    public void init() {
        scanAllAndAddToMap("com.oldmee");
        createInstances();
        ReflectAll();
        createUrlMapping();
    }


    /**
     * 把所有的class扫描一遍放在classNameWithPackageName中
     *
     * @param scanPath
     */
    private void scanAllAndAddToMap(String scanPath) {
        URL url = this.getClass().getClassLoader().getResource(scanPath.replace(".", "/"));
        File file = new File(url.getFile());
        File[] files = file.listFiles();
        for (File fi : files) {
            if (fi.isDirectory()) {
                scanAllAndAddToMap(scanPath + "." + fi.getName());// 继续往directory下面搜索
            } else {
                classNameWithPackageName.add(scanPath + "." + fi.getName());// eg.  com.oldmee.anotation.Controvice.class
            }
        }
    }


    /**
     * 把被controvice注解标记的类实例化
     */
    private void createInstances() {
        for (String className : classNameWithPackageName) {
            try {
                Class<?> clazz = Class.forName(className.replace(".class", ""));
                if (clazz.isAnnotationPresent(Controvice.class)) { // 因为只有Controller和Service是类，需要实例化，所以要过滤一下
                    Object object = clazz.newInstance();
                    Controvice controvice = clazz.getDeclaredAnnotation(Controvice.class);

                    beans.put(controvice.value(), object);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 依赖注入操作，把controller中的service实例变量通过反射给实例化了
     */
    private void ReflectAll() {

        if (beans.size() < 0) {
            System.err.println("beans is empty");
        }

        for (Object object : beans.values()) {
            Class clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(MyAutoWire.class)) {
                    MyAutoWire myAutoWire = field.getDeclaredAnnotation(MyAutoWire.class);
                    String beanName = myAutoWire.value();
                    Object beanObject = beans.get(beanName);
                    field.setAccessible(true);
                    try {
                        field.set(object, beanObject);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    /**
     * 建立url与实体的对应关系
     */
    private void createUrlMapping() {

        for (Object object : beans.values()) {
            Class clazz = object.getClass();
            if (clazz.isAnnotationPresent(Controvice.class)) {
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(MyRequestPath.class)) { // 找到需要建立映射的方法
                        String urlPath = method.getAnnotation(MyRequestPath.class).value();
                        urlMethods.put(urlPath, method);
                    }
                }
            }
        }

    }


    /**
     * 把实参注入到形参中
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String contPath = req.getContextPath();
        String uri = req.getRequestURI();
        String url = uri.replace(contPath, "");

        Method method = (Method) urlMethods.get(url);
        Class<?>[] clazzs = method.getParameterTypes();
        Object[] args = new Object[clazzs.length];
        int index = 0;

        for(Class c : clazzs) {
            Annotation[] annotations = method.getParameterAnnotations()[index];
            for (Annotation annotation : annotations) {
                if (MyParameter.class.isAssignableFrom(annotation.getClass())) {
                    MyParameter parameter = (MyParameter) annotation;
                    args[index++] = req.getParameter(parameter.value());
                }
            }
        }

        Object o = beans.get("myControllerTest");//todo

        try {
            method.invoke(o, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
