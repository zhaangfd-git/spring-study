package com.zhangfd.spring.test;

import com.zhangfd.spring.beans.TypeConverterDelegate;
import com.zhangfd.spring.beans.propertyeditors.CustomNumberEditor;
import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.factory.support.PropertyEditorRegistrySupport;

import java.beans.PropertyEditor;

public class TypeConverterDelegateTest {

    public static void main(String[] args) {


        //把字符String类型转换为Integer类型
        TypeConverterDelegate de = new TypeConverterDelegate(new PropertyEditorRegistrySupport(),null);
        Integer integer = de.convertIfNecessary(null, null, "8", Integer.class, TypeDescriptor.valueOf(Integer.class));
        System.out.println(integer);
    }
}
