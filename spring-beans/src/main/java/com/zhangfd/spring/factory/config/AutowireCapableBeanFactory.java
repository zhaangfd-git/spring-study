package com.zhangfd.spring.factory.config;

import com.zhangfd.spring.BeansException;
import com.zhangfd.spring.beans.TypeConverter;
import com.zhangfd.spring.factory.BeanFactory;
import com.zhangfd.spring.lang.Nullable;

import java.util.Set;

public interface AutowireCapableBeanFactory  extends BeanFactory {

    /**
     * Constant that indicates no externally defined autowiring. Note that
     * BeanFactoryAware etc and annotation-driven injection will still be applied.
     * @see #createBean
     * @see #autowire
     * @see #autowireBeanProperties
     */
    int AUTOWIRE_NO = 0;

    /**
     * Constant that indicates autowiring bean properties by name
     * (applying to all bean property setters).
     * @see #createBean
     * @see #autowire
     * @see #autowireBeanProperties
     */
    int AUTOWIRE_BY_NAME = 1;

    /**
     * Constant that indicates autowiring bean properties by type
     * (applying to all bean property setters).
     * @see #createBean
     * @see #autowire
     * @see #autowireBeanProperties
     */
    int AUTOWIRE_BY_TYPE = 2;

    /**
     * Constant that indicates autowiring the greediest constructor that
     * can be satisfied (involves resolving the appropriate constructor).
     * @see #createBean
     * @see #autowire
     */
    int AUTOWIRE_CONSTRUCTOR = 3;

    /**
     * Constant that indicates determining an appropriate autowire strategy
     * through introspection of the bean class.
     * @see #createBean
     * @see #autowire
     * @deprecated as of Spring 3.0: If you are using mixed autowiring strategies,
     * prefer annotation-based autowiring for clearer demarcation of autowiring needs.
     */
    @Deprecated
    int AUTOWIRE_AUTODETECT = 4;

    /**
     * Suffix for the "original instance" convention when initializing an existing
     * bean instance: to be appended to the fully-qualified bean class name,
     * e.g. "com.mypackage.MyClass.ORIGINAL", in order to enforce the given instance
     * to be returned, i.e. no proxies etc.
     * @since 5.1
     * @see #initializeBean(Object, String)
     * @see #applyBeanPostProcessorsBeforeInitialization(Object, String)
     * @see #applyBeanPostProcessorsAfterInitialization(Object, String)
     */
    String ORIGINAL_INSTANCE_SUFFIX = ".ORIGINAL";


    @Nullable
    Object resolveDependency(DependencyDescriptor descriptor, @Nullable String requestingBeanName,
                             @Nullable Set<String> autowiredBeanNames, @Nullable TypeConverter typeConverter) throws BeansException;


}
