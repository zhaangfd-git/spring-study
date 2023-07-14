package com.zhangfd.spring.factory.support;

import com.zhangfd.spring.factory.config.BeanDefinition;
import com.zhangfd.spring.lang.Nullable;

import java.lang.reflect.Executable;

public class RootBeanDefinition  extends  AbstractBeanDefinition{


    /** Common lock for the four constructor fields below. */
    final Object constructorArgumentLock = new Object();

    /** Package-visible field for caching the resolved constructor or factory method. */
    @Nullable
    Executable resolvedConstructorOrFactoryMethod;


    @Override
    public String getParentName() {
        return null;
    }

    @Override
    public void setParentName(@Nullable String parentName) {
        if (parentName != null) {
            throw new IllegalArgumentException("Root bean cannot be changed into a child bean with parent reference");
        }
    }


    @Override
    public String getResourceDescription() {
        return null;
    }

    @Override
    public BeanDefinition getOriginatingBeanDefinition() {
        return null;
    }
}
