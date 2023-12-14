package com.zhangfd.spring.context.test;

import com.zhangfd.spring.beans.PropertyEditorRegistrar;
import com.zhangfd.spring.beans.support.ResourceEditorRegistrar;
import com.zhangfd.spring.core.env.StandardEnvironment;
import com.zhangfd.spring.core.io.support.PathMatchingResourcePatternResolver;
import com.zhangfd.spring.factory.support.PropertyEditorRegistrySupport;

/**
 * @author: zhangfd
 * @date: 2023/12/11 23:03
 * @version: 1.0
 * @describe:
 */
public class ResourceEditorRegistrarTest {

    public static void main(String[] args) {
        //主要是添加类型转换器，主要是把字符串传唤为java对象，
        ResourceEditorRegistrar registrar = new ResourceEditorRegistrar(new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader()),new StandardEnvironment());

        registrar.registerCustomEditors(new PropertyEditorRegistrySupport());

        //PropertyEditorRegistrar的实现类是ResourceEditorRegistrar
        //目的是 可以自定义PropertyEditorRegistry实现类，
        // spring已经给了非常好用的默认实现类PropertyEditorRegistrySupport


        //PropertyEditorRegistry接口的目的是包装PropertyEditor接口，它的默认实现类PropertyEditorSupport
        //就是属性类型转换器
    }

}
