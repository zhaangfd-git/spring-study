package com.zhangfd.spring.test;

import com.zhangfd.spring.*;
import com.zhangfd.spring.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test2 {


    public static void main(String[] args) throws Exception {
       /* BeanInfo beanInfo = Introspector.getBeanInfo(BeanInfoTest.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();*/

        //1、可以利用beanWrapper#convertIfNecessary方法做类型转换
        BeanWrapper beanWrapper = new BeanWrapperImpl(new BeanInfoTest());
        //获取实例的所有属性
        PropertyDescriptor[] propertyDescriptors02 = beanWrapper.getPropertyDescriptors();
        //Class<?> wrappedClass = beanWrapper.getWrappedClass();

        //获取包装对象具体的属性
        for (PropertyDescriptor propertyDescriptor:propertyDescriptors02) {
            if("class".equalsIgnoreCase(propertyDescriptor.getName()) || StringUtils.isEmpty(propertyDescriptor.getName())){
                continue;
            }
            System.out.println(propertyDescriptor.getName());
            System.out.println(propertyDescriptor.getPropertyType());
            System.out.println(propertyDescriptor.getReadMethod());
            System.out.println(propertyDescriptor.getWriteMethod());
        }

        HashMap<String, List<Integer>> param = new HashMap<>();
        List<Integer>  tt = new ArrayList<>();
        tt.add(1);tt.add(2);param.put("test",tt);
        //给包装对象的属性赋值
      //  beanWrapper.setPropertyValue("param",param);

        beanWrapper.setPropertyValue(new PropertyValue("age","18"));

        Map map = new HashMap<>();
        map.put("name", "zhangmm");
        beanWrapper.setPropertyValues(map);

        MutablePropertyValues ps = new MutablePropertyValues();
        ps.addPropertyValue("param",param);
        beanWrapper.setPropertyValues(ps);

        Object wrappedInstance = beanWrapper.getWrappedInstance();

        //做类型转换:spring内置了大量了常用的属性编辑器，可自动完成类型转换工作
        BeanWrapper beanWrapper01 = new BeanWrapperImpl();
        Integer integer = beanWrapper01.convertIfNecessary("8", Integer.class);

        //还可以定义自己的属性编辑器
        //beanWrapper01.registerCustomEditor();
    }



    public static class BeanInfoTest {

        public BeanInfoTest() {
        }

        private HashMap<String, List<Integer>> param;

        private String  name;


        private Integer  age;

        public HashMap<String, List<Integer>> getParam() {
            return param;
        }

        public void setParam(HashMap<String, List<Integer>> param) {
            this.param = param;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
