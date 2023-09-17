package com.zhangfd.spring.factory.support;

import com.zhangfd.spring.core.SimpleAliasRegistry;
import com.zhangfd.spring.exception.BeanCreationException;
import com.zhangfd.spring.factory.DisposableBean;
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

    /** 记录每个bean，需要依赖的其他bean集合 */
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

   //记录需要被dispose的bean，key是bean的名字，value是对应的实例对象
    private final Map<String, Object> disposableBeans = new LinkedHashMap<>();



    /** Flag that indicates whether we're currently within destroySingletons. */
    private boolean singletonsCurrentlyInDestruction = false;



    /** Map between containing bean names: bean name to Set of bean names that the bean contains. */
    private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap<>(16);

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        synchronized (this.disposableBeans) {
            this.disposableBeans.put(beanName, bean);
        }
    }

    protected void onSuppressedException(Exception ex) {
        synchronized (this.singletonObjects) {
            if (this.suppressedExceptions != null && this.suppressedExceptions.size() < SUPPRESSED_EXCEPTIONS_LIMIT) {
                this.suppressedExceptions.add(ex);
            }
        }
    }

    protected boolean isDependent(String beanName, String dependentBeanName) {
        synchronized (this.dependentBeanMap) {
            return isDependent(beanName, dependentBeanName, null);
        }
    }

    public String[] getDependentBeans(String beanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans == null) {
            return new String[0];
        }
        synchronized (this.dependentBeanMap) {
            return StringUtils.toStringArray(dependentBeans);
        }
    }


    private boolean isDependent(String beanName, String dependentBeanName, @Nullable Set<String> alreadySeen) {
        if (alreadySeen != null && alreadySeen.contains(beanName)) {
            return false;
        }
        String canonicalName = canonicalName(beanName);
        Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
        if (dependentBeans == null) {
            return false;
        }
        if (dependentBeans.contains(dependentBeanName)) {
            return true;
        }
        for (String transitiveDependency : dependentBeans) {
            if (alreadySeen == null) {
                alreadySeen = new HashSet<>();
            }
            alreadySeen.add(beanName);
            if (isDependent(transitiveDependency, dependentBeanName, alreadySeen)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether a dependent bean has been registered for the given name.
     * @param beanName the name of the bean to check
     */
    protected boolean hasDependentBean(String beanName) {
        return this.dependentBeanMap.containsKey(beanName);
    }

    @Nullable
    public Set<Exception> getSuppressedExceptions() {
        return suppressedExceptions;
    }

    public void setSuppressedExceptions(@Nullable Set<Exception> suppressedExceptions) {
        this.suppressedExceptions = suppressedExceptions;
    }

    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.remove(beanName);
        }
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

    /**
     * 1级缓存赋值
     * 2级、3级缓存删除
     * @param beanName
     * @param singletonObject
     */
    public void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }

    /**
     * 若1级缓存没有值，放入三级缓存，
     * 1、2级缓存删除值
     * @param beanName
     * @param singletonFactory
     */
    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        Assert.notNull(singletonFactory, "Singleton factory must not be null");
        synchronized (this.singletonObjects) {
            if (!this.singletonObjects.containsKey(beanName)) {
                this.singletonFactories.put(beanName, singletonFactory);
                this.earlySingletonObjects.remove(beanName);
                this.registeredSingletons.add(beanName);
            }
        }
    }


    public void registerContainedBean(String containedBeanName, String containingBeanName) {
        synchronized (this.containedBeanMap) {
            Set<String> containedBeans =
                    this.containedBeanMap.computeIfAbsent(containingBeanName, k -> new LinkedHashSet<>(8));
            if (!containedBeans.add(containedBeanName)) {
                return;
            }
        }
        registerDependentBean(containedBeanName, containingBeanName);
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

    public void destroySingletons() {
        if (logger.isTraceEnabled()) {
            logger.trace("Destroying singletons in " + this);
        }
        synchronized (this.singletonObjects) {
            this.singletonsCurrentlyInDestruction = true;
        }

        String[] disposableBeanNames;
        synchronized (this.disposableBeans) {
            disposableBeanNames = StringUtils.toStringArray(this.disposableBeans.keySet());
        }
        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            destroySingleton(disposableBeanNames[i]);
        }

        this.containedBeanMap.clear();
        this.dependentBeanMap.clear();
        this.dependenciesForBeanMap.clear();

        clearSingletonCache();
    }

    /**
     * Clear all cached singleton instances in this registry.
     * @since 4.3.15
     */
    protected void clearSingletonCache() {
        synchronized (this.singletonObjects) {
            this.singletonObjects.clear();
            this.singletonFactories.clear();
            this.earlySingletonObjects.clear();
            this.registeredSingletons.clear();
            this.singletonsCurrentlyInDestruction = false;
        }
    }

    public void destroySingleton(String beanName) {
        // Remove a registered singleton of the given name, if any.
        removeSingleton(beanName);

        // Destroy the corresponding DisposableBean instance.
        DisposableBean disposableBean;
        synchronized (this.disposableBeans) {
            disposableBean = (DisposableBean) this.disposableBeans.remove(beanName);
        }
        destroyBean(beanName, disposableBean);
    }



    /**
     * Destroy the given bean. Must destroy beans that depend on the given
     * bean before the bean itself. Should not throw any exceptions.
     * @param beanName the name of the bean
     * @param bean the bean instance to destroy
     */
    protected void destroyBean(String beanName, @Nullable DisposableBean bean) {
        // Trigger destruction of dependent beans first...
        Set<String> dependencies;
        synchronized (this.dependentBeanMap) {
            // Within full synchronization in order to guarantee a disconnected Set
            dependencies = this.dependentBeanMap.remove(beanName);
        }
        if (dependencies != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("Retrieved dependent beans for bean '" + beanName + "': " + dependencies);
            }
            for (String dependentBeanName : dependencies) {
                destroySingleton(dependentBeanName);
            }
        }

        // Actually destroy the bean now...
        if (bean != null) {
            try {
                bean.destroy();
            }
            catch (Throwable ex) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Destruction of bean with name '" + beanName + "' threw an exception", ex);
                }
            }
        }

        // Trigger destruction of contained beans...
        Set<String> containedBeans;
        synchronized (this.containedBeanMap) {
            // Within full synchronization in order to guarantee a disconnected Set
            containedBeans = this.containedBeanMap.remove(beanName);
        }
        if (containedBeans != null) {
            for (String containedBeanName : containedBeans) {
                destroySingleton(containedBeanName);
            }
        }

        // Remove destroyed bean from other beans' dependencies.
        synchronized (this.dependentBeanMap) {
            for (Iterator<Map.Entry<String, Set<String>>> it = this.dependentBeanMap.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, Set<String>> entry = it.next();
                Set<String> dependenciesToClean = entry.getValue();
                dependenciesToClean.remove(beanName);
                if (dependenciesToClean.isEmpty()) {
                    it.remove();
                }
            }
        }

        // Remove destroyed bean's prepared dependency information.
        this.dependenciesForBeanMap.remove(beanName);
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
        //先去一级缓存里查看是否存在（一级缓存里存的是完完整整的被实例化好的实例对象）
        Object singletonObject = this.singletonObjects.get(beanName);
        //若一级缓存里没有，并且这个bean被标注为正在创建
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
            //再去二级缓存查看是否存在，若存在则直接返回，不存在则继续去三级缓存里取值
            singletonObject = this.earlySingletonObjects.get(beanName);
            //若二级缓存也不存在的话
            if (singletonObject == null && allowEarlyReference) {
                synchronized (this.singletonObjects) {//此时给一级缓存加锁，这里使用的是double check机制
                    // Consistent creation of early reference within full singleton lock
                    singletonObject = this.singletonObjects.get(beanName);
                    if (singletonObject == null) {
                        singletonObject = this.earlySingletonObjects.get(beanName);
                        if (singletonObject == null) {//此时表明一、二级缓存都没有值
                            //去三级缓存里取值，三级缓存其实获取的是工厂ObjectFactory对象，需要通过getObject方法才能获取真实对象
                            //整个过程为啥会设计三级缓存呢？其实就是解决spring里所谓的循环依赖问题，比如A依赖B，B也依赖A
                            //1、正常情况下，先通过反射创建A对象，放入一个map2中，待填充值和初始化完成后放入另外一个map1
                            //在创建B的时候，先通过反射创建B对象放入map2， 发现依赖A实例，那么去map2中获取它赋值给B中的属性
                            // （即使此时A还没有实例化，但在堆栈里地址不会变，后面实例化完成也可啊），待B初始化完成后放入map1，然后A依赖的B
                            //再去map1获取值后进行赋值操作，完成后把A对象放入map1即可。

                            //参考https://blog.csdn.net/weixin_44129618/article/details/122839774
                            //2、不正常的情况经常发生啊，比如A依赖B，B也依赖A，并且A被aop了（被aop后回创建一个全新的对象存入一级缓存
                            // 有人说那我们在最开始就创建A的代理对象存入二级缓存，可是要注意spring实现的aop可是在创建完对象之后哈），
                            //这样貌似上面的方法就不能使用了，二级缓存完败。
                            //对此，spring做了特殊处理，引入所谓的三季缓存机制，
                            ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                            if (singletonFactory != null) {
                                singletonObject = singletonFactory.getObject();
                                //若三级缓存取到值，就要其放入二级缓存中，二级缓存里存的对象还没有初始化，填充值也没有完成；
                                this.earlySingletonObjects.put(beanName, singletonObject);
                                //从三级缓存中去掉
                                this.singletonFactories.remove(beanName);
                            }
                        }
                    }
                }
            }
        }
        return singletonObject;
    }

    /**
     * 获取单例对象，获取不到就去创建它
     * @param beanName
     * @param singletonFactory
     * @return
     */
    public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
        Assert.notNull(beanName, "Bean name must not be null");
        synchronized (this.singletonObjects) {//上来就锁定一级缓存
            Object singletonObject = this.singletonObjects.get(beanName);//查看一级缓存是否有值
            if (singletonObject == null) { //若为空
                if (this.singletonsCurrentlyInDestruction) {//而我们又在销毁这个bean则报错
                    throw new BeanCreationException(beanName,
                            "Singleton bean creation not allowed while singletons of this factory are in destruction " +
                                    "(Do not request a bean from a BeanFactory in a destroy method implementation!)");
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
                }
                //加入map，标识这个bean正在创建
                beforeSingletonCreation(beanName);
                boolean newSingleton = false;
                boolean recordSuppressedExceptions = (this.suppressedExceptions == null);
                if (recordSuppressedExceptions) {
                    this.suppressedExceptions = new LinkedHashSet<>();
                }
                try {
                    //调用ObjectFactory接口的实现类，创建对象
                    singletonObject = singletonFactory.getObject();
                    newSingleton = true;
                }catch (IllegalStateException ex) {//若报非法的异常，说明其他线程已经创建对象，这里再次从一级缓存获取对象。
                    // Has the singleton object implicitly appeared in the meantime ->
                    // if yes, proceed with it since the exception indicates that state.
                    singletonObject = this.singletonObjects.get(beanName);//再次从一级缓存获取对象
                    if (singletonObject == null) {
                        throw ex;
                    }
                }catch (BeanCreationException ex) {
                    if (recordSuppressedExceptions) {
                        for (Exception suppressedException : this.suppressedExceptions) {
                            ex.addRelatedCause(suppressedException);
                        }
                    }
                    throw ex;
                }finally {
                    if (recordSuppressedExceptions) {
                        this.suppressedExceptions = null;
                    }
                    afterSingletonCreation(beanName);
                }
                if (newSingleton) {//说明是第一次创建的单例，给放入一级缓存中，同时删除二、三级缓存
                    addSingleton(beanName, singletonObject);
                }
            }
            return singletonObject;
        }
    }

    protected void beforeSingletonCreation(String beanName) {
        if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.add(beanName)) {
            throw new BeanCreationException(beanName);
        }
    }
    protected void afterSingletonCreation(String beanName) {
        if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.remove(beanName)) {
            throw new IllegalStateException("Singleton '" + beanName + "' isn't currently in creation");
        }
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
