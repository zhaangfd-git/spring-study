package com.zhangfd.spring.test;

import com.zhangfd.spring.factory.support.PropertyEditorRegistrySupport;

import java.beans.PropertyEditor;

public class PropertyEditorRegistrySupportTest {


    public static void main(String[] args) {
        PropertyEditorRegistrySupport  pe = new PropertyEditorRegistrySupport();
        pe.registerDefaultEditors();
        PropertyEditor defaultEditor = pe.getDefaultEditor(Integer.class);//获取Integer类型的转换器
        defaultEditor.setAsText("9"); //转换前的文本
        Object value = defaultEditor.getValue();  //已经把文本子字符“9”转换为数字9了


    }
}
