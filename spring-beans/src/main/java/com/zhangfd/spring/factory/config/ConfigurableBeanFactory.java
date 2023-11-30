package com.zhangfd.spring.factory.config;

import com.zhangfd.spring.beans.PropertyEditorRegistrar;
import com.zhangfd.spring.beans.TypeConverter;
import com.zhangfd.spring.core.convert.ConversionService;
import com.zhangfd.spring.exception.NoSuchBeanDefinitionException;
import com.zhangfd.spring.factory.BeanFactory;
import com.zhangfd.spring.factory.support.HierarchicalBeanFactory;
import com.zhangfd.spring.factory.support.PropertyEditorRegistry;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.StringValueResolver;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;

public interface ConfigurableBeanFactory  extends HierarchicalBeanFactory, SingletonBeanRegistry {

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

    void copyConfigurationFrom(ConfigurableBeanFactory otherFactory);

    void setBeanClassLoader(@Nullable ClassLoader beanClassLoader);

    void setCacheBeanMetadata(boolean cacheBeanMetadata);

    @Nullable
    BeanExpressionResolver getBeanExpressionResolver();

    String[] getRegisteredScopeNames();

    String[] getDependentBeans(String beanName);

    /**
     * Return the names of all beans that the specified bean depends on, if any.
     * @param beanName the name of the bean
     * @return the array of names of beans which the bean depends on,
     * or an empty array if none
     * @since 2.5
     */
    String[] getDependenciesForBean(String beanName);


    void setBeanExpressionResolver(@Nullable BeanExpressionResolver resolver);

    boolean isCacheBeanMetadata();

    BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;


    void setTypeConverter(TypeConverter typeConverter);

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * Return the current number of registered BeanPostProcessors, if any.
     */
    int getBeanPostProcessorCount();


    void setConversionService(@Nullable ConversionService conversionService);


    boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException;
    void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException;

    void setTempClassLoader(@Nullable ClassLoader tempClassLoader);

    void addEmbeddedValueResolver(StringValueResolver valueResolver);
    void registerScope(String scopeName, Scope scope);
    /**
     * Determine whether an embedded value resolver has been registered with this
     * bean factory, to be applied through {@link #resolveEmbeddedValue(String)}.
     * @since 4.3
     */
    boolean hasEmbeddedValueResolver();

    void destroyBean(String beanName, Object beanInstance);

    /**
     * Destroy the specified scoped bean in the current target scope, if any.
     * <p>Any exception that arises during destruction should be caught
     * and logged instead of propagated to the caller of this method.
     * @param beanName the name of the scoped bean
     */
    void destroyScopedBean(String beanName);
    void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass);

    /**
     * Initialize the given PropertyEditorRegistry with the custom editors
     * that have been registered with this BeanFactory.
     * @param registry the PropertyEditorRegistry to initialize
     */
    void copyRegisteredEditorsTo(PropertyEditorRegistry registry);
    void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);


    /**
     * Return the associated ConversionService, if any.
     * @since 3.0
     */
    @Nullable
    ConversionService getConversionService();

    @Nullable
    String resolveEmbeddedValue(String value);

    TypeConverter getTypeConverter();
    void destroySingletons();
}
