package com.zhangfd.spring.factory.support;

import com.zhangfd.spring.factory.FactoryBean;
import com.zhangfd.spring.lang.Nullable;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * factoryBean注册器的支持者
 */
public abstract  class FactoryBeanRegistrySupport extends  DefaultSingletonBeanRegistry {

    //存放通过FactoryBean创建的代理的真实对象的bean---对象
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>(16);


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
          /*  logger.info("FactoryBean threw exception from getObjectType, despite the contract saying " +
                    "that it should return null if the type of its object cannot be determined yet", ex);
           */ return null;
        }
    }

    @Override
    protected void removeSingleton(String beanName) {
        synchronized (getSingletonMutex()) {
            super.removeSingleton(beanName);
            this.factoryBeanObjectCache.remove(beanName);
        }
    }


    protected AccessControlContext getAccessControlContext() {
        return AccessController.getContext();
    }
}
