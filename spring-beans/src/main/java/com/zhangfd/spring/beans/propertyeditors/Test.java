package com.zhangfd.spring.beans.propertyeditors;

import java.beans.PropertyEditor;

public class Test {

    public static void main(String[] args) {

        String ss = "8";
        PropertyEditor propertiesEditor = new CustomNumberEditor(Integer.class, true);
        propertiesEditor.setAsText(ss);

        //类型转换
        Integer conut = (Integer) propertiesEditor.getValue();

    }
}
