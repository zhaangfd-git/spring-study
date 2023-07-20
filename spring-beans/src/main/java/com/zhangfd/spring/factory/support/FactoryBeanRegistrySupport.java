package com.zhangfd.spring.factory.support;

import com.zhangfd.spring.lang.Nullable;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * factoryBean注册器的支持者
 */
public abstract  class FactoryBeanRegistrySupport extends  DefaultSingletonBeanRegistry {

    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>(16);

/*
    @Nullable
    protected Class<?> getTypeForFactoryBean(FactoryBean<?> factoryBean) {
        try {
            if (System.getSecurityManager() != null) {
                return AccessController.doPrivileged(
                        (PrivilegedAction<Class<?>>) factoryBean::getObjectType, getAccessControlContext());
            }
            else {
                return factoryBean.getObjectType();
            }
        }
        catch (Throwable ex) {
            // Thrown from the FactoryBean's getObjectType implementation.
            logger.info("FactoryBean threw exception from getObjectType, despite the contract saying " +
                    "that it should return null if the type of its object cannot be determined yet", ex);
            return null;
        }
    }*/
}
