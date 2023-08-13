package com.zhangfd.spring.factory.support;

import com.zhangfd.spring.core.SimpleAliasRegistry;
import com.zhangfd.spring.factory.ObjectFactory;
import com.zhangfd.spring.factory.config.SingletonBeanRegistry;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;
import com.zhangfd.spring.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例注册器
 */
public class DefaultSingletonBeanRegistry  extends SimpleAliasRegistry implements SingletonBeanRegistry {

    /**存放实例化并初始化话的实例对象(包含单例、原型等)，第一级缓存 */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /** 缓存单例，目的是解决spring的所谓循环依赖问题，第三级缓存. */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    /** 把通过反射创建的对象先放在这里，此时还没初始化，第二级缓存 */
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);

    /** 存放所有的单例bean的名字 */
    private final Set<String> registeredSingletons = new LinkedHashSet<>(256);

    private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);

    private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);


    /** Names of beans that are currently in creation. */
    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));




    //保存创建实例时的异常信息
    @Nullable
    private Set<Exception> suppressedExceptions;


    //最大支持的保存异常信息个数
    private static final int SUPPRESSED_EXCEPTIONS_LIMIT = 100;



    /** Names of beans currently excluded from in creation checks. */
    private final Set<String> inCreationCheckExclusions =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));



    protected void onSuppressedException(Exception ex) {
        synchronized (this.singletonObjects) {
            if (this.suppressedExceptions != null && this.suppressedExceptions.size() < SUPPRESSED_EXCEPTIONS_LIMIT) {
                this.suppressedExceptions.add(ex);
            }
        }
    }

    @Nullable
    public Set<Exception> getSuppressedExceptions() {
        return suppressedExceptions;
    }

    public void setSuppressedExceptions(@Nullable Set<Exception> suppressedExceptions) {
        this.suppressedExceptions = suppressedExceptions;
    }


    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        Assert.notNull(beanName, "Bean name must not be null");
        Assert.notNull(singletonObject, "Singleton object must not be null");
        synchronized (this.singletonObjects) {
            Object oldObject = this.singletonObjects.get(beanName);
            if (oldObject != null) {
                throw new IllegalStateException("Could not register object [" + singletonObject +
                        "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
            }
            addSingleton(beanName, singletonObject);
        }
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }


    @Override
    @Nullable
    public Object getSingleton(String beanName) {
        return getSingleton(beanName, true);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        synchronized (this.singletonObjects) {
            return StringUtils.toStringArray(this.registeredSingletons);
        }
    }

    @Override
    public int getSingletonCount() {
        synchronized (this.singletonObjects) {
            return this.registeredSingletons.size();
        }
    }

    @Override
    public Object getSingletonMutex() {
        return this.singletonObjects;
    }

    /**
     * Return the (raw) singleton object registered under the given name.
     * <p>Checks already instantiated singletons and also allows for an early
     * reference to a currently created singleton (resolving a circular reference).
     * @param beanName the name of the bean to look for
     * @param allowEarlyReference whether early references should be created or not
     * @return the registered singleton object, or {@code null} if none found
     */
    @Nullable
    protected Object getSingleton(String beanName, boolean allowEarlyReference) {
        // Quick check for existing instance without full singleton lock
        Object singletonObject = this.singletonObjects.get(beanName);
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null && allowEarlyReference) {
                synchronized (this.singletonObjects) {
                    // Consistent creation of early reference within full singleton lock
                    singletonObject = this.singletonObjects.get(beanName);
                    if (singletonObject == null) {
                        singletonObject = this.earlySingletonObjects.get(beanName);
                        if (singletonObject == null) {
                            ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                            if (singletonFactory != null) {
                                singletonObject = singletonFactory.getObject();
                                this.earlySingletonObjects.put(beanName, singletonObject);
                                this.singletonFactories.remove(beanName);
                            }
                        }
                    }
                }
            }
        }
        return singletonObject;
    }

    protected boolean isActuallyInCreation(String beanName) {
        return isSingletonCurrentlyInCreation(beanName);
    }

    /**
     * Return whether the specified singleton bean is currently in creation
     * (within the entire factory).
     * @param beanName the name of the bean
     */
    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }
    public boolean isCurrentlyInCreation(String beanName) {
        Assert.notNull(beanName, "Bean name must not be null");
        return (!this.inCreationCheckExclusions.contains(beanName) && isActuallyInCreation(beanName));
    }

    public void registerDependentBean(String beanName, String dependentBeanName) {
        String canonicalName = canonicalName(beanName);

        synchronized (this.dependentBeanMap) {
            Set<String> dependentBeans =
                    this.dependentBeanMap.computeIfAbsent(canonicalName, k -> new LinkedHashSet<>(8));
            if (!dependentBeans.add(dependentBeanName)) {
                return;
            }
        }

        synchronized (this.dependenciesForBeanMap) {
            Set<String> dependenciesForBean =
                    this.dependenciesForBeanMap.computeIfAbsent(dependentBeanName, k -> new LinkedHashSet<>(8));
            dependenciesForBean.add(canonicalName);
        }
    }
}
