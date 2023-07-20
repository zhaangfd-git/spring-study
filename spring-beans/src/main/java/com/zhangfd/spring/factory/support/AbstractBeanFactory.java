package com.zhangfd.spring.factory.support;

import com.zhangfd.spring.BeanWrapper;
import com.zhangfd.spring.BeansException;
import com.zhangfd.spring.core.AttributeAccessor;
import com.zhangfd.spring.core.DecoratingClassLoader;
import com.zhangfd.spring.core.ResolvableType;
import com.zhangfd.spring.factory.*;
import com.zhangfd.spring.factory.config.*;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;
import com.zhangfd.spring.util.ClassUtils;
import com.zhangfd.spring.util.ObjectUtils;
import com.zhangfd.spring.util.StringUtils;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory  extends  FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    @Nullable
    private SecurityContextProvider securityContextProvider;


    private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<>(256);

   // 是否缓存了bean的元数据信息
    private boolean cacheBeanMetadata = true;

    @Nullable
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();


    @Nullable
    private ClassLoader tempClassLoader;


    @Nullable
    private BeanExpressionResolver beanExpressionResolver;


    protected void initBeanWrapper(BeanWrapper bw) {
       /* bw.setConversionService(getConversionService());
        registerCustomEditors(bw);*/
    }


    @Override
    public AccessControlContext getAccessControlContext() {
        return (this.securityContextProvider != null ?
                this.securityContextProvider.getAccessControlContext() :
                AccessController.getContext());
    }

    @Override
    @Nullable
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getType(name, true);
    }



    @Override
    @Nullable
    public Class<?> getType(String name, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException {
        String beanName = transformedBeanName(name);

        // Check manually registered singletons.
        Object beanInstance = getSingleton(beanName, false);
        if (beanInstance != null && beanInstance.getClass() != NullBean.class) {
            if (beanInstance instanceof FactoryBean && !BeanFactoryUtils.isFactoryDereference(name)) {
                return getTypeForFactoryBean((FactoryBean<?>) beanInstance);
            }
            else {
                return beanInstance.getClass();
            }
        }

        // No singleton instance found -> check bean definition.
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            // No bean definition found in this factory -> delegate to parent.
            return parentBeanFactory.getType(originalBeanName(name));
        }

        RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);

        // Check decorated bean definition, if any: We assume it'll be easier
        // to determine the decorated bean's type than the proxy's type.
        BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
        if (dbd != null && !BeanFactoryUtils.isFactoryDereference(name)) {
            RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
            Class<?> targetClass = predictBeanType(dbd.getBeanName(), tbd);
            if (targetClass != null && !FactoryBean.class.isAssignableFrom(targetClass)) {
                return targetClass;
            }
        }

        Class<?> beanClass = predictBeanType(beanName, mbd);

        // Check bean class whether we're dealing with a FactoryBean.
        if (beanClass != null && FactoryBean.class.isAssignableFrom(beanClass)) {
            if (!BeanFactoryUtils.isFactoryDereference(name)) {
                // If it's a FactoryBean, we want to look at what it creates, not at the factory class.
                return getTypeForFactoryBean(beanName, mbd, allowFactoryBeanInit).resolve();
            }
            else {
                return beanClass;
            }
        }
        else {
            return (!BeanFactoryUtils.isFactoryDereference(name) ? beanClass : null);
        }
    }

    protected ResolvableType getTypeForFactoryBean(String beanName, RootBeanDefinition mbd, boolean allowInit) {
        ResolvableType result = getTypeForFactoryBeanFromAttributes(mbd);
        if (result != ResolvableType.NONE) {
            return result;
        }

        if (allowInit && mbd.isSingleton()) {
            try {
                FactoryBean<?> factoryBean = doGetBean(FACTORY_BEAN_PREFIX + beanName, FactoryBean.class, null, true);
                Class<?> objectType = getTypeForFactoryBean(factoryBean);
                return (objectType != null ? ResolvableType.forClass(objectType) : ResolvableType.NONE);
            }
            catch (BeanCreationException ex) {
                if (ex.contains(BeanCurrentlyInCreationException.class)) {
                    logger.trace(LogMessage.format("Bean currently in creation on FactoryBean type check: %s", ex));
                }
                else if (mbd.isLazyInit()) {
                    logger.trace(LogMessage.format("Bean creation exception on lazy FactoryBean type check: %s", ex));
                }
                else {
                    logger.debug(LogMessage.format("Bean creation exception on eager FactoryBean type check: %s", ex));
                }
                onSuppressedException(ex);
            }
        }
        return ResolvableType.NONE;
    }

    protected <T> T doGetBean(
            String name, @Nullable Class<T> requiredType, @Nullable Object[] args, boolean typeCheckOnly)
            throws BeansException {

        return  null;
      /*  String beanName = transformedBeanName(name);
        Object bean;

        // Eagerly check singleton cache for manually registered singletons.
        Object sharedInstance = getSingleton(beanName);
        if (sharedInstance != null && args == null) {
            if (logger.isTraceEnabled()) {
                if (isSingletonCurrentlyInCreation(beanName)) {
                    logger.trace("Returning eagerly cached instance of singleton bean '" + beanName +
                            "' that is not fully initialized yet - a consequence of a circular reference");
                }
                else {
                    logger.trace("Returning cached instance of singleton bean '" + beanName + "'");
                }
            }
            bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
        }

        else {
            // Fail if we're already creating this bean instance:
            // We're assumably within a circular reference.
            if (isPrototypeCurrentlyInCreation(beanName)) {
                throw new BeanCurrentlyInCreationException(beanName);
            }

            // Check if bean definition exists in this factory.
            BeanFactory parentBeanFactory = getParentBeanFactory();
            if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
                // Not found -> check parent.
                String nameToLookup = originalBeanName(name);
                if (parentBeanFactory instanceof AbstractBeanFactory) {
                    return ((AbstractBeanFactory) parentBeanFactory).doGetBean(
                            nameToLookup, requiredType, args, typeCheckOnly);
                }
                else if (args != null) {
                    // Delegation to parent with explicit args.
                    return (T) parentBeanFactory.getBean(nameToLookup, args);
                }
                else if (requiredType != null) {
                    // No args -> delegate to standard getBean method.
                    return parentBeanFactory.getBean(nameToLookup, requiredType);
                }
                else {
                    return (T) parentBeanFactory.getBean(nameToLookup);
                }
            }

            if (!typeCheckOnly) {
                markBeanAsCreated(beanName);
            }

            try {
                RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
                checkMergedBeanDefinition(mbd, beanName, args);

                // Guarantee initialization of beans that the current bean depends on.
                String[] dependsOn = mbd.getDependsOn();
                if (dependsOn != null) {
                    for (String dep : dependsOn) {
                        if (isDependent(beanName, dep)) {
                            throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                    "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
                        }
                        registerDependentBean(dep, beanName);
                        try {
                            getBean(dep);
                        }
                        catch (NoSuchBeanDefinitionException ex) {
                            throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                    "'" + beanName + "' depends on missing bean '" + dep + "'", ex);
                        }
                    }
                }

                // Create bean instance.
                if (mbd.isSingleton()) {
                    sharedInstance = getSingleton(beanName, () -> {
                        try {
                            return createBean(beanName, mbd, args);
                        }
                        catch (BeansException ex) {
                            // Explicitly remove instance from singleton cache: It might have been put there
                            // eagerly by the creation process, to allow for circular reference resolution.
                            // Also remove any beans that received a temporary reference to the bean.
                            destroySingleton(beanName);
                            throw ex;
                        }
                    });
                    bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
                }

                else if (mbd.isPrototype()) {
                    // It's a prototype -> create a new instance.
                    Object prototypeInstance = null;
                    try {
                        beforePrototypeCreation(beanName);
                        prototypeInstance = createBean(beanName, mbd, args);
                    }
                    finally {
                        afterPrototypeCreation(beanName);
                    }
                    bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
                }

                else {
                    String scopeName = mbd.getScope();
                    if (!StringUtils.hasLength(scopeName)) {
                        throw new IllegalStateException("No scope name defined for bean ´" + beanName + "'");
                    }
                    Scope scope = this.scopes.get(scopeName);
                    if (scope == null) {
                        throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
                    }
                    try {
                        Object scopedInstance = scope.get(beanName, () -> {
                            beforePrototypeCreation(beanName);
                            try {
                                return createBean(beanName, mbd, args);
                            }
                            finally {
                                afterPrototypeCreation(beanName);
                            }
                        });
                        bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
                    }
                    catch (IllegalStateException ex) {
                        throw new BeanCreationException(beanName,
                                "Scope '" + scopeName + "' is not active for the current thread; consider " +
                                        "defining a scoped proxy for this bean if you intend to refer to it from a singleton",
                                ex);
                    }
                }
            }
            catch (BeansException ex) {
                cleanupAfterBeanCreationFailure(beanName);
                throw ex;
            }
        }

        // Check if required type matches the type of the actual bean instance.
        if (requiredType != null && !requiredType.isInstance(bean)) {
            try {
                T convertedBean = getTypeConverter().convertIfNecessary(bean, requiredType);
                if (convertedBean == null) {
                    throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
                }
                return convertedBean;
            }
            catch (TypeMismatchException ex) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Failed to convert bean '" + name + "' to required type '" +
                            ClassUtils.getQualifiedName(requiredType) + "'", ex);
                }
                throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
            }
        }
        return (T) bean;*/
    }
    ResolvableType getTypeForFactoryBeanFromAttributes(AttributeAccessor attributes) {
        Object attribute = attributes.getAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE);
        if (attribute instanceof ResolvableType) {
            return (ResolvableType) attribute;
        }
        if (attribute instanceof Class) {
            return ResolvableType.forClass((Class<?>) attribute);
        }
        return ResolvableType.NONE;
    }

    protected String transformedBeanName(String name) {
        return canonicalName(BeanFactoryUtils.transformedBeanName(name));
    }

    /**
     * Determine the original bean name, resolving locally defined aliases to canonical names.
     * @param name the user-specified name
     * @return the original bean name
     */
    protected String originalBeanName(String name) {
        String beanName = transformedBeanName(name);
        if (name.startsWith(FACTORY_BEAN_PREFIX)) {
            beanName = FACTORY_BEAN_PREFIX + beanName;
        }
        return beanName;
    }

    @Nullable
    protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) {
        Class<?> targetType = mbd.getTargetType();
        if (targetType != null) {
            return targetType;
        }
        if (mbd.getFactoryMethodName() != null) {
            return null;
        }
        return resolveBeanClass(mbd, beanName, typesToMatch);
    }


    @Nullable
    protected Class<?> resolveBeanClass(RootBeanDefinition mbd, String beanName, Class<?>... typesToMatch)
            throws CannotLoadBeanClassException {

        try {
            if (mbd.hasBeanClass()) {
                return mbd.getBeanClass();
            }
            if (System.getSecurityManager() != null) {
                return AccessController.doPrivileged((PrivilegedExceptionAction<Class<?>>)
                        () -> doResolveBeanClass(mbd, typesToMatch), getAccessControlContext());
            }
            else {
                return doResolveBeanClass(mbd, typesToMatch);
            }
        }
        catch (PrivilegedActionException pae) {
            ClassNotFoundException ex = (ClassNotFoundException) pae.getException();
            throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
        }
        catch (ClassNotFoundException ex) {
            throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
        }
        catch (LinkageError err) {
            throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), err);
        }
    }

    @Nullable
    private Class<?> doResolveBeanClass(RootBeanDefinition mbd, Class<?>... typesToMatch)
            throws ClassNotFoundException {

        ClassLoader beanClassLoader = getBeanClassLoader();
        ClassLoader dynamicLoader = beanClassLoader;
        boolean freshResolve = false;

        if (!ObjectUtils.isEmpty(typesToMatch)) {
            // When just doing type checks (i.e. not creating an actual instance yet),
            // use the specified temporary class loader (e.g. in a weaving scenario).
            ClassLoader tempClassLoader = getTempClassLoader();
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
            Object evaluated = evaluateBeanDefinitionString(className, mbd);
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
                        if (logger.isTraceEnabled()) {
                            logger.trace("Could not load class [" + className + "] from " + dynamicLoader + ": " + ex);
                        }
                    }
                }
                return ClassUtils.forName(className, dynamicLoader);
            }
        }

        // Resolve regularly, caching the result in the BeanDefinition...
        return mbd.resolveBeanClass(beanClassLoader);
    }


    @Nullable
    protected Object evaluateBeanDefinitionString(@Nullable String value, @Nullable BeanDefinition beanDefinition) {
        if (this.beanExpressionResolver == null) {
            return value;
        }

        Scope scope = null;
        if (beanDefinition != null) {
            String scopeName = beanDefinition.getScope();
            if (scopeName != null) {
                scope = getRegisteredScope(scopeName);
            }
        }
        return this.beanExpressionResolver.evaluate(value, new BeanExpressionContext(this, scope));
    }

    private final Map<String, Scope> scopes = new LinkedHashMap<>(8);

    @Override
    @Nullable
    public Scope getRegisteredScope(String scopeName) {
        Assert.notNull(scopeName, "Scope identifier must not be null");
        return this.scopes.get(scopeName);
    }



    @Override
    @Nullable
    public ClassLoader getTempClassLoader() {
        return this.tempClassLoader;
    }


    protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException {
        // Quick check on the concurrent map first, with minimal locking.
        RootBeanDefinition mbd = this.mergedBeanDefinitions.get(beanName);
        if (mbd != null && !mbd.stale) {
            return mbd;
        }
        return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
    }


    @Override
    @Nullable
    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }
    protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd)
            throws BeanDefinitionStoreException {

        return getMergedBeanDefinition(beanName, bd, null);
    }
    protected RootBeanDefinition getMergedBeanDefinition(
            String beanName, BeanDefinition bd, @Nullable BeanDefinition containingBd)
            throws BeanDefinitionStoreException {

        synchronized (this.mergedBeanDefinitions) {
            RootBeanDefinition mbd = null;
            RootBeanDefinition previous = null;

            // Check with full lock now in order to enforce the same merged instance.
            if (containingBd == null) {
                mbd = this.mergedBeanDefinitions.get(beanName);
            }

            if (mbd == null || mbd.stale) {
                previous = mbd;
                if (bd.getParentName() == null) {
                    // Use copy of given root bean definition.
                    if (bd instanceof RootBeanDefinition) {
                        mbd = ((RootBeanDefinition) bd).cloneBeanDefinition();
                    }
                    else {
                        mbd = new RootBeanDefinition(bd);
                    }
                }
                else {
                    // Child bean definition: needs to be merged with parent.
                    BeanDefinition pbd;
                    try {
                        String parentBeanName = transformedBeanName(bd.getParentName());
                        if (!beanName.equals(parentBeanName)) {
                            pbd = getMergedBeanDefinition(parentBeanName);
                        }
                        else {
                            BeanFactory parent = getParentBeanFactory();
                            if (parent instanceof ConfigurableBeanFactory) {
                                pbd = ((ConfigurableBeanFactory) parent).getMergedBeanDefinition(parentBeanName);
                            }else {
                                throw new NoSuchBeanDefinitionException(parentBeanName,
                                        "Parent name '" + parentBeanName + "' is equal to bean name '" + beanName +
                                                "': cannot be resolved without a ConfigurableBeanFactory parent");
                            }
                        }
                    }catch (NoSuchBeanDefinitionException ex) {
                        throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanName,
                                "Could not resolve parent bean definition '" + bd.getParentName() + "'", ex);
                    }
                    // Deep copy with overridden values.
                    mbd = new RootBeanDefinition(pbd);
                    mbd.overrideFrom(bd);
                }

                // Set default singleton scope, if not configured before.
                if (!StringUtils.hasLength(mbd.getScope())) {
                    mbd.setScope(SCOPE_SINGLETON);
                }

                // A bean contained in a non-singleton bean cannot be a singleton itself.
                // Let's correct this on the fly here, since this might be the result of
                // parent-child merging for the outer bean, in which case the original inner bean
                // definition will not have inherited the merged outer bean's singleton status.
                if (containingBd != null && !containingBd.isSingleton() && mbd.isSingleton()) {
                    mbd.setScope(containingBd.getScope());
                }

                // Cache the merged bean definition for the time being
                // (it might still get re-merged later on in order to pick up metadata changes)
                if (containingBd == null && isCacheBeanMetadata()) {
                    this.mergedBeanDefinitions.put(beanName, mbd);
                }
            }
            if (previous != null) {
                copyRelevantMergedBeanDefinitionCaches(previous, mbd);
            }
            return mbd;
        }
    }

    private void copyRelevantMergedBeanDefinitionCaches(RootBeanDefinition previous, RootBeanDefinition mbd) {
        if (ObjectUtils.nullSafeEquals(mbd.getBeanClassName(), previous.getBeanClassName()) &&
                ObjectUtils.nullSafeEquals(mbd.getFactoryBeanName(), previous.getFactoryBeanName()) &&
                ObjectUtils.nullSafeEquals(mbd.getFactoryMethodName(), previous.getFactoryMethodName())) {
            ResolvableType targetType = mbd.targetType;
            ResolvableType previousTargetType = previous.targetType;
            if (targetType == null || targetType.equals(previousTargetType)) {
                mbd.targetType = previousTargetType;
                mbd.isFactoryBean = previous.isFactoryBean;
                mbd.resolvedTargetType = previous.resolvedTargetType;
                mbd.factoryMethodReturnType = previous.factoryMethodReturnType;
                mbd.factoryMethodToIntrospect = previous.factoryMethodToIntrospect;
            }
        }
    }

    @Override
    public boolean isCacheBeanMetadata() {
        return this.cacheBeanMetadata;
    }

    protected abstract boolean containsBeanDefinition(String beanName);

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

}
