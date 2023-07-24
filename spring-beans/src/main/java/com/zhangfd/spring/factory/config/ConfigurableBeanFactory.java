package com.zhangfd.spring.factory.config;

import com.zhangfd.spring.beans.TypeConverter;
import com.zhangfd.spring.exception.NoSuchBeanDefinitionException;
import com.zhangfd.spring.factory.support.HierarchicalBeanFactory;
import com.zhangfd.spring.lang.Nullable;

import java.security.AccessControlContext;

public interface ConfigurableBeanFactory  extends HierarchicalBeanFactory {

    /**
     * Scope identifier for the standard singleton scope: {@value}.
     * <p>Custom scopes can be added via {@code registerScope}.
     * @see #registerScope
     */
    String SCOPE_SINGLETON = "singleton";

    /**
     * Scope identifier for the standard prototype scope: {@value}.
     * <p>Custom scopes can be added via {@code registerScope}.
     * @see #registerScope
     */
    String SCOPE_PROTOTYPE = "prototype";


    //获取一个AccessControlContext，不能为空
    AccessControlContext getAccessControlContext();

    //获取这个工厂的类加载器
    @Nullable
    ClassLoader getBeanClassLoader();

    //获取零时的类加载器
    @Nullable
    ClassLoader getTempClassLoader();

    // 返回被注册bean的的scope 单例or 原型 or 其他
    @Nullable
    Scope getRegisteredScope(String scopeName);


    boolean isCacheBeanMetadata();

    BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;


    void setTypeConverter(TypeConverter typeConverter);

}
