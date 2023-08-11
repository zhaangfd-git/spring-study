package com.zhangfd.spring.test;

import com.zhangfd.spring.BeanWrapper;
import com.zhangfd.spring.BeanWrapperImpl;
import com.zhangfd.spring.MutablePropertyValues;
import com.zhangfd.spring.PropertyValue;
import com.zhangfd.spring.util.StringUtils;

import java.beans.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanWrapperImplTest {


    public static void main(String[] args) throws Exception {
        //test01();

        BeanWrapper beanWrapper = new BeanWrapperImpl(new BeanInfoTest());
        HashMap<String, List<Integer>> param = new HashMap<>();
        List<Integer>  tt = new ArrayList<>();
        tt.add(1);tt.add(2);param.put("test",tt);
        //给包装对象的属性赋值
        //  beanWrapper.setPropertyValue("param",param);

        beanWrapper.setPropertyValue(new PropertyValue("age","18"));

        Map map = new HashMap<>();
        map.put("name", "zhangmm");
        //可以把所有的属性值放入map中，自动帮做类型转换并赋值
        beanWrapper.setPropertyValues(map);

        MutablePropertyValues ps = new MutablePropertyValues();
        ps.addPropertyValue("param",param);
        beanWrapper.setPropertyValues(ps,true);

        Object wrappedInstance = beanWrapper.getWrappedInstance();

        //做类型转换:spring内置了大量了常用的属性编辑器，可自动完成类型转换工作
        BeanWrapper beanWrapper01 = new BeanWrapperImpl();
        Integer integer = beanWrapper01.convertIfNecessary("8", Integer.class);


    }



    public  static void test01() throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(BeanInfoTest.class);
        //1、获取实体的信息，
        BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
        beanDescriptor.getBeanClass();beanDescriptor.getName();

        //2、获取实体下的属性信息，包含可读函数和可写函数
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        //获取包装对象具体的属性
        for (PropertyDescriptor propertyDescriptor:propertyDescriptors) {
            if("class".equalsIgnoreCase(propertyDescriptor.getName()) || StringUtils.isEmpty(propertyDescriptor.getName())){
                continue;
            }
            //属性名
            System.out.println(propertyDescriptor.getName());
            //属性类型
            System.out.println(propertyDescriptor.getPropertyType());
            //属性对应的setter方法
            System.out.println(propertyDescriptor.getReadMethod());
            ////属性对应的getter方法
            System.out.println(propertyDescriptor.getWriteMethod());

        }
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
