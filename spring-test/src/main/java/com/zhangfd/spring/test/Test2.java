package com.zhangfd.spring.test;

import com.zhangfd.spring.BeanWrapper;
import com.zhangfd.spring.BeanWrapperImpl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test2 {


    public static void main(String[] args) throws Exception {
       /* BeanInfo beanInfo = Introspector.getBeanInfo(BeanInfoTest.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();*/

        //1、可以利用beanWrapper#convertIfNecessary方法做类型转换
        BeanWrapper beanWrapper = new BeanWrapperImpl(new BeanInfoTest());
        //获取实例的所有属性
        PropertyDescriptor[] propertyDescriptors02 = beanWrapper.getPropertyDescriptors();
        Class<?> wrappedClass = beanWrapper.getWrappedClass();

        HashMap<String, List<Integer>> param = new HashMap<>();
        List<Integer>  tt = new ArrayList<>();
        tt.add(1);tt.add(2);param.put("test",tt);

        beanWrapper.setPropertyValue("param",param);
        beanWrapper.setPropertyValue("name","zhangfd");


        Object wrappedInstance = beanWrapper.getWrappedInstance();
        Integer integer = beanWrapper.convertIfNecessary("8", Integer.class);
    }



    public static class BeanInfoTest {

        public BeanInfoTest() {
        }

        private HashMap<String, List<Integer>> param;

        private String  name;


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
    }
}
