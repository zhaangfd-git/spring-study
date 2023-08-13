package com.zhangfd.spring.factory.support;

import com.zhangfd.spring.core.DecoratingClassLoader;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.ClassUtils;
import com.zhangfd.spring.util.ObjectUtils;

public class Test {

    public static void main(String[] args) {
        RootBeanDefinition mbd = new RootBeanDefinition();
        try {
            Class<?> beanClass = doResolveBeanClass(mbd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("=========");

    }

    @Nullable
    static Class<?> doResolveBeanClass(RootBeanDefinition mbd, Class<?>... typesToMatch)
            throws ClassNotFoundException {

        ClassLoader beanClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader dynamicLoader = beanClassLoader;
        boolean freshResolve = false;

        if (!ObjectUtils.isEmpty(typesToMatch)) {
            // When just doing type checks (i.e. not creating an actual instance yet),
            // use the specified temporary class loader (e.g. in a weaving scenario).
            ClassLoader tempClassLoader = Thread.currentThread().getContextClassLoader();
            if (tempClassLoader != null) {
                dynamicLoader = tempClassLoader;
                freshResolve = true;
                if (tempClassLoader instanceof DecoratingClassLoader) {
                    DecoratingClassLoader dcl = (DecoratingClassLoader) tempClassLoader;
                    for (Class<?> typeToMatch : typesToMatch) {
                        dcl.excludeClass(typeToMatch.getName());
                    }
                }
            }
        }

        String className = mbd.getBeanClassName();
        if (className != null) {
            Object evaluated = className;
            if (!className.equals(evaluated)) {
                // A dynamically resolved expression, supported as of 4.2...
                if (evaluated instanceof Class) {
                    return (Class<?>) evaluated;
                }
                else if (evaluated instanceof String) {
                    className = (String) evaluated;
                    freshResolve = true;
                }
                else {
                    throw new IllegalStateException("Invalid class name expression result: " + evaluated);
                }
            }
            if (freshResolve) {
                // When resolving against a temporary class loader, exit early in order
                // to avoid storing the resolved Class in the bean definition.
                if (dynamicLoader != null) {
                    try {
                        return dynamicLoader.loadClass(className);
                    }
                    catch (ClassNotFoundException ex) {
                     /*   if (logger.isTraceEnabled()) {
                            logger.trace("Could not load class [" + className + "] from " + dynamicLoader + ": " + ex);
                        }*/
                    }
                }
                return ClassUtils.forName(className, dynamicLoader);
            }
        }

        // Resolve regularly, caching the result in the BeanDefinition...
        return mbd.resolveBeanClass(beanClassLoader);
    }

}
