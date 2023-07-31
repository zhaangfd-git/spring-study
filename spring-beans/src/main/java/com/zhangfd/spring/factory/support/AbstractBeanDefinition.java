package com.zhangfd.spring.factory.support;

import com.zhangfd.spring.MutablePropertyValues;
import com.zhangfd.spring.beans.BeanMetadataAttributeAccessor;
import com.zhangfd.spring.core.ResolvableType;
import com.zhangfd.spring.exception.BeanDefinitionValidationException;
import com.zhangfd.spring.factory.config.AutowireCapableBeanFactory;
import com.zhangfd.spring.factory.config.BeanDefinition;
import com.zhangfd.spring.factory.config.ConstructorArgumentValues;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.ClassUtils;
import com.zhangfd.spring.util.StringUtils;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

public abstract class AbstractBeanDefinition   extends BeanMetadataAttributeAccessor implements BeanDefinition, Cloneable{

    @Nullable
    private volatile Object beanClass;

    // 运行时方法被重写，说明使用了cglib代理，后面会据此判断是否是cglib代理生成的实体类
    private MethodOverrides methodOverrides = new MethodOverrides();


    private boolean lenientConstructorResolution = true;

    private int role = BeanDefinition.ROLE_APPLICATION;

    @Nullable
    private String description;

    /**
     * Constant for the default scope name: {@code ""}, equivalent to singleton
     * status unless overridden from a parent bean definition (if applicable).
     */
    public static final String SCOPE_DEFAULT = "";

    /**
     * Constant that indicates no external autowiring at all.
     * @see #setAutowireMode
     */
    public static final int AUTOWIRE_NO = AutowireCapableBeanFactory.AUTOWIRE_NO;

    /**
     * Constant that indicates autowiring bean properties by name.
     * @see #setAutowireMode
     */
    public static final int AUTOWIRE_BY_NAME = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;

    /**
     * Constant that indicates autowiring bean properties by type.
     * @see #setAutowireMode
     */
    public static final int AUTOWIRE_BY_TYPE = AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;

    /**
     * Constant that indicates autowiring a constructor.
     * @see #setAutowireMode
     */
    public static final int AUTOWIRE_CONSTRUCTOR = AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR;

    /**
     * Constant that indicates determining an appropriate autowire strategy
     * through introspection of the bean class.
     * @see #setAutowireMode
     * @deprecated as of Spring 3.0: If you are using mixed autowiring strategies,
     * use annotation-based autowiring for clearer demarcation of autowiring needs.
     */
    @Deprecated
    public static final int AUTOWIRE_AUTODETECT = AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT;

    //判断是否构造方法时public类型的
    private boolean nonPublicAccessAllowed = true;

    @Nullable
    private Supplier<?> instanceSupplier;

    /**
     * Constant that indicates no dependency check at all.
     * @see #setDependencyCheck
     */
    public static final int DEPENDENCY_CHECK_NONE = 0;

    /**
     * Constant that indicates dependency checking for object references.
     * @see #setDependencyCheck
     */
    public static final int DEPENDENCY_CHECK_OBJECTS = 1;

    /**
     * Constant that indicates dependency checking for "simple" properties.
     * @see #setDependencyCheck
     * @see org.springframework.beans.BeanUtils#isSimpleProperty
     */
    public static final int DEPENDENCY_CHECK_SIMPLE = 2;

    /**
     * Constant that indicates dependency checking for all properties
     * (object references as well as "simple" properties).
     * @see #setDependencyCheck
     */
    public static final int DEPENDENCY_CHECK_ALL = 3;

    @Nullable
    private String scope = SCOPE_DEFAULT;

    private boolean abstractFlag = false;

    @Nullable
    private Boolean lazyInit;

    private int autowireMode = AUTOWIRE_NO;

    private int dependencyCheck = DEPENDENCY_CHECK_NONE;

    @Nullable
    private String[] dependsOn;

    private boolean autowireCandidate = true;

    private boolean primary = false;

    @Nullable
    private String factoryBeanName;

    @Nullable
    private String factoryMethodName;

    @Nullable
    private ConstructorArgumentValues constructorArgumentValues;

    @Nullable
    private MutablePropertyValues propertyValues;


    @Nullable
    private String initMethodName;

    @Nullable
    private String destroyMethodName;

    private boolean synthetic = false;

    protected AbstractBeanDefinition(BeanDefinition original) {
//        setParentName(original.getParentName());
//        setBeanClassName(original.getBeanClassName());
//        setScope(original.getScope());
//        setAbstract(original.isAbstract());
//        setFactoryBeanName(original.getFactoryBeanName());
//        setFactoryMethodName(original.getFactoryMethodName());
//        setRole(original.getRole());
//        setSource(original.getSource());
//        copyAttributesFrom(original);
//
//        if (original instanceof AbstractBeanDefinition) {
//            AbstractBeanDefinition originalAbd = (AbstractBeanDefinition) original;
//            if (originalAbd.hasBeanClass()) {
//                setBeanClass(originalAbd.getBeanClass());
//            }
//            if (originalAbd.hasConstructorArgumentValues()) {
//                setConstructorArgumentValues(new ConstructorArgumentValues(original.getConstructorArgumentValues()));
//            }
//            if (originalAbd.hasPropertyValues()) {
//                setPropertyValues(new MutablePropertyValues(original.getPropertyValues()));
//            }
//            if (originalAbd.hasMethodOverrides()) {
//                setMethodOverrides(new MethodOverrides(originalAbd.getMethodOverrides()));
//            }
//            Boolean lazyInit = originalAbd.getLazyInit();
//            if (lazyInit != null) {
//                setLazyInit(lazyInit);
//            }
//            setAutowireMode(originalAbd.getAutowireMode());
//            setDependencyCheck(originalAbd.getDependencyCheck());
//            setDependsOn(originalAbd.getDependsOn());
//            setAutowireCandidate(originalAbd.isAutowireCandidate());
//            setPrimary(originalAbd.isPrimary());
//            copyQualifiersFrom(originalAbd);
//            setInstanceSupplier(originalAbd.getInstanceSupplier());
//            setNonPublicAccessAllowed(originalAbd.isNonPublicAccessAllowed());
//            setLenientConstructorResolution(originalAbd.isLenientConstructorResolution());
//            setInitMethodName(originalAbd.getInitMethodName());
//            setEnforceInitMethod(originalAbd.isEnforceInitMethod());
//            setDestroyMethodName(originalAbd.getDestroyMethodName());
//            setEnforceDestroyMethod(originalAbd.isEnforceDestroyMethod());
//            setSynthetic(originalAbd.isSynthetic());
//            setResource(originalAbd.getResource());
//        }
//        else {
//            setConstructorArgumentValues(new ConstructorArgumentValues(original.getConstructorArgumentValues()));
//            setPropertyValues(new MutablePropertyValues(original.getPropertyValues()));
//            setLazyInit(original.isLazyInit());
//            setResourceDescription(original.getResourceDescription());
//        }
    }

    /**
     * Create a new AbstractBeanDefinition with default settings.
     */
    protected AbstractBeanDefinition() {
        this(null, null);
    }

    /**
     * Create a new AbstractBeanDefinition with the given
     * constructor argument values and property values.
     */
    protected AbstractBeanDefinition(@Nullable ConstructorArgumentValues cargs, @Nullable MutablePropertyValues pvs) {
        this.constructorArgumentValues = cargs;
        this.propertyValues = pvs;
    }

    public void prepareMethodOverrides() throws BeanDefinitionValidationException {
        // Check that lookup methods exist and determine their overloaded status.
        if (hasMethodOverrides()) {
            getMethodOverrides().getOverrides().forEach(this::prepareMethodOverride);
        }
    }

    protected void prepareMethodOverride(MethodOverride mo) throws BeanDefinitionValidationException {
        int count = ClassUtils.getMethodCountForName(getBeanClass(), mo.getMethodName());
        if (count == 0) {
            throw new BeanDefinitionValidationException(
                    "Invalid method override: no method with name '" + mo.getMethodName() +
                            "' on class [" + getBeanClassName() + "]");
        }
        else if (count == 1) {
            // Mark override as not overloaded, to avoid the overhead of arg type checking.
            mo.setOverloaded(false);
        }
    }



    /**
     * Specify a callback for creating an instance of the bean,
     * as an alternative to a declaratively specified factory method.
     * <p>If such a callback is set, it will override any other constructor
     * or factory method metadata. However, bean property population and
     * potential annotation-driven injection will still apply as usual.
     * @since 5.0
     * @see #setConstructorArgumentValues(ConstructorArgumentValues)
     * @see #setPropertyValues(MutablePropertyValues)
     */
    public void setInstanceSupplier(@Nullable Supplier<?> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    /**
     * Return a callback for creating an instance of the bean, if any.
     * @since 5.0
     */
    @Nullable
    public Supplier<?> getInstanceSupplier() {
        return this.instanceSupplier;
    }

    // 运行时方法被重写，说明使用了cglib代理，后面会据此判断是否是cglib代理生成的实体类
    public boolean hasMethodOverrides() {
        return !this.methodOverrides.isEmpty();
    }


    public Class<?> getBeanClass() throws IllegalStateException {
        Object beanClassObject = this.beanClass;
        if (beanClassObject == null) {
            throw new IllegalStateException("No bean class specified on bean definition");
        }
        if (!(beanClassObject instanceof Class)) {
            throw new IllegalStateException(
                    "Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
        }
        return (Class<?>) beanClassObject;
    }

    public void overrideFrom(BeanDefinition other) {
        if (StringUtils.hasLength(other.getBeanClassName())) {
            setBeanClassName(other.getBeanClassName());
        }
        if (StringUtils.hasLength(other.getScope())) {
            setScope(other.getScope());
        }
        setAbstract(other.isAbstract());
        if (StringUtils.hasLength(other.getFactoryBeanName())) {
            setFactoryBeanName(other.getFactoryBeanName());
        }
        if (StringUtils.hasLength(other.getFactoryMethodName())) {
            setFactoryMethodName(other.getFactoryMethodName());
        }
        setRole(other.getRole());
       // setSource(other.getSource());
        //copyAttributesFrom(other);

        if (other instanceof AbstractBeanDefinition) {
            AbstractBeanDefinition otherAbd = (AbstractBeanDefinition) other;
            if (otherAbd.hasBeanClass()) {
                setBeanClass(otherAbd.getBeanClass());
            }
            if (otherAbd.hasConstructorArgumentValues()) {
                //getConstructorArgumentValues().addArgumentValues(other.getConstructorArgumentValues());
            }
            if (otherAbd.hasPropertyValues()) {
               // getPropertyValues().addPropertyValues(other.getPropertyValues());
            }
            if (otherAbd.hasMethodOverrides()) {
                getMethodOverrides().addOverrides(otherAbd.getMethodOverrides());
            }
            Boolean lazyInit = otherAbd.getLazyInit();
            if (lazyInit != null) {
                setLazyInit(lazyInit);
            }
            setAutowireMode(otherAbd.getAutowireMode());
            setDependencyCheck(otherAbd.getDependencyCheck());
            setDependsOn(otherAbd.getDependsOn());
            setAutowireCandidate(otherAbd.isAutowireCandidate());
            setPrimary(otherAbd.isPrimary());
          //  copyQualifiersFrom(otherAbd);
           // setInstanceSupplier(otherAbd.getInstanceSupplier());
            setNonPublicAccessAllowed(otherAbd.isNonPublicAccessAllowed());
            setLenientConstructorResolution(otherAbd.isLenientConstructorResolution());
            if (otherAbd.getInitMethodName() != null) {
                setInitMethodName(otherAbd.getInitMethodName());
              //  setEnforceInitMethod(otherAbd.isEnforceInitMethod());
            }
            if (otherAbd.getDestroyMethodName() != null) {
                setDestroyMethodName(otherAbd.getDestroyMethodName());
               // setEnforceDestroyMethod(otherAbd.isEnforceDestroyMethod());
            }
            //setSynthetic(otherAbd.isSynthetic());
           // setResource(otherAbd.getResource());
        }
        else {
          //  getConstructorArgumentValues().addArgumentValues(other.getConstructorArgumentValues());
          //  getPropertyValues().addPropertyValues(other.getPropertyValues());
            setLazyInit(other.isLazyInit());
          //  setResourceDescription(other.getResourceDescription());
        }
    }


    public void setAbstract(boolean abstractFlag) {
        this.abstractFlag = abstractFlag;
    }

    /**
     * Specify the bean class name of this bean definition.
     */
    @Override
    public void setBeanClassName(@Nullable String beanClassName) {
        this.beanClass = beanClassName;
    }

    /**
     * Return the current bean class name of this bean definition.
     */
    @Override
    @Nullable
    public String getBeanClassName() {
        Object beanClassObject = this.beanClass;
        if (beanClassObject instanceof Class) {
            return ((Class<?>) beanClassObject).getName();
        }
        else {
            return (String) beanClassObject;
        }
    }
    /**
     * Specify the class for this bean.
     * @see #setBeanClassName(String)
     */
    public void setBeanClass(@Nullable Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * Return a resolvable type for this bean definition.
     * <p>This implementation delegates to {@link #getBeanClass()}.
     * @since 5.2
     */
    @Override
    public ResolvableType getResolvableType() {
        return (hasBeanClass() ? ResolvableType.forClass(getBeanClass()) : ResolvableType.NONE);
    }
    public boolean hasBeanClass() {
        return (this.beanClass instanceof Class);
    }

    @Override
    public void setScope(@Nullable String scope) {
        this.scope = scope;
    }



    @Override
    @Nullable
    public String getScope() {
        return this.scope;
    }

    @Override
    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(this.scope) || SCOPE_DEFAULT.equals(this.scope);
    }

    /**
     * Return whether this a <b>Prototype</b>, with an independent instance
     * returned for each call.
     * @see #SCOPE_PROTOTYPE
     */
    @Override
    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(this.scope);
    }


    /**
     * Return whether this bean is "abstract", i.e. not meant to be instantiated
     * itself but rather just serving as parent for concrete child bean definitions.
     */
    @Override
    public boolean isAbstract() {
        return this.abstractFlag;
    }

    /**
     * Set whether this bean should be lazily initialized.
     * <p>If {@code false}, the bean will get instantiated on startup by bean
     * factories that perform eager initialization of singletons.
     */
    @Override
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    /**
     * Return whether this bean should be lazily initialized, i.e. not
     * eagerly instantiated on startup. Only applicable to a singleton bean.
     * @return whether to apply lazy-init semantics ({@code false} by default)
     */
    @Override
    public boolean isLazyInit() {
        return (this.lazyInit != null && this.lazyInit.booleanValue());
    }

    /**
     * Return whether this bean should be lazily initialized, i.e. not
     * eagerly instantiated on startup. Only applicable to a singleton bean.
     * @return the lazy-init flag if explicitly set, or {@code null} otherwise
     * @since 5.2
     */
    @Nullable
    public Boolean getLazyInit() {
        return this.lazyInit;
    }

    /**
     * Set the autowire mode. This determines whether any automagical detection
     * and setting of bean references will happen. Default is AUTOWIRE_NO
     * which means there won't be convention-based autowiring by name or type
     * (however, there may still be explicit annotation-driven autowiring).
     * @param autowireMode the autowire mode to set.
     * Must be one of the constants defined in this class.
     * @see #AUTOWIRE_NO
     * @see #AUTOWIRE_BY_NAME
     * @see #AUTOWIRE_BY_TYPE
     * @see #AUTOWIRE_CONSTRUCTOR
     * @see #AUTOWIRE_AUTODETECT
     */
    public void setAutowireMode(int autowireMode) {
        this.autowireMode = autowireMode;
    }

    /**
     * Return the autowire mode as specified in the bean definition.
     */
    public int getAutowireMode() {
        return this.autowireMode;
    }



    /**
     * Return the resolved autowire code,
     * (resolving AUTOWIRE_AUTODETECT to AUTOWIRE_CONSTRUCTOR or AUTOWIRE_BY_TYPE).
     * @see #AUTOWIRE_AUTODETECT
     * @see #AUTOWIRE_CONSTRUCTOR
     * @see #AUTOWIRE_BY_TYPE
     */
    public int getResolvedAutowireMode() {
        if (this.autowireMode == AUTOWIRE_AUTODETECT) {
            // Work out whether to apply setter autowiring or constructor autowiring.
            // If it has a no-arg constructor it's deemed to be setter autowiring,
            // otherwise we'll try constructor autowiring.
            Constructor<?>[] constructors = getBeanClass().getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() == 0) {
                    return AUTOWIRE_BY_TYPE;
                }
            }
            return AUTOWIRE_CONSTRUCTOR;
        }
        else {
            return this.autowireMode;
        }
    }

    public abstract AbstractBeanDefinition cloneBeanDefinition();

    @Nullable
    public Class<?> resolveBeanClass(@Nullable ClassLoader classLoader) throws ClassNotFoundException {
        String className = getBeanClassName();
        if (className == null) {
            return null;
        }
        Class<?> resolvedClass = ClassUtils.forName(className, classLoader);
        this.beanClass = resolvedClass;
        return resolvedClass;
    }

    /**
     * Set the dependency check code.
     * @param dependencyCheck the code to set.
     * Must be one of the four constants defined in this class.
     * @see #DEPENDENCY_CHECK_NONE
     * @see #DEPENDENCY_CHECK_OBJECTS
     * @see #DEPENDENCY_CHECK_SIMPLE
     * @see #DEPENDENCY_CHECK_ALL
     */
    public void setDependencyCheck(int dependencyCheck) {
        this.dependencyCheck = dependencyCheck;
    }

    /**
     * Return the dependency check code.
     */
    public int getDependencyCheck() {
        return this.dependencyCheck;
    }

    /**
     * Set the names of the beans that this bean depends on being initialized.
     * The bean factory will guarantee that these beans get initialized first.
     * <p>Note that dependencies are normally expressed through bean properties or
     * constructor arguments. This property should just be necessary for other kinds
     * of dependencies like statics (*ugh*) or database preparation on startup.
     */
    @Override
    public void setDependsOn(@Nullable String... dependsOn) {
        this.dependsOn = dependsOn;
    }

    /**
     * Return the bean names that this bean depends on.
     */
    @Override
    @Nullable
    public String[] getDependsOn() {
        return this.dependsOn;
    }

    /**
     * Set whether this bean is a candidate for getting autowired into some other bean.
     * <p>Note that this flag is designed to only affect type-based autowiring.
     * It does not affect explicit references by name, which will get resolved even
     * if the specified bean is not marked as an autowire candidate. As a consequence,
     * autowiring by name will nevertheless inject a bean if the name matches.
     * @see #AUTOWIRE_BY_TYPE
     * @see #AUTOWIRE_BY_NAME
     */
    @Override
    public void setAutowireCandidate(boolean autowireCandidate) {
        this.autowireCandidate = autowireCandidate;
    }

    /**
     * Return whether this bean is a candidate for getting autowired into some other bean.
     */
    @Override
    public boolean isAutowireCandidate() {
        return this.autowireCandidate;
    }

    /**
     * Set whether this bean is a primary autowire candidate.
     * <p>If this value is {@code true} for exactly one bean among multiple
     * matching candidates, it will serve as a tie-breaker.
     */
    @Override
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    /**
     * Return whether this bean is a primary autowire candidate.
     */
    @Override
    public boolean isPrimary() {
        return this.primary;
    }
    @Override
    public void setFactoryBeanName(@Nullable String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    /**
     * Return the factory bean name, if any.
     */
    @Override
    @Nullable
    public String getFactoryBeanName() {
        return this.factoryBeanName;
    }

    /**
     * Specify a factory method, if any. This method will be invoked with
     * constructor arguments, or with no arguments if none are specified.
     * The method will be invoked on the specified factory bean, if any,
     * or otherwise as a static method on the local bean class.
     * @see #setFactoryBeanName
     * @see #setBeanClassName
     */
    @Override
    public void setFactoryMethodName(@Nullable String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    /**
     * Return a factory method, if any.
     */
    @Override
    @Nullable
    public String getFactoryMethodName() {
        return this.factoryMethodName;
    }

    /**
     * Specify constructor argument values for this bean.
     */
    public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues = constructorArgumentValues;
    }

    /**
     * Return constructor argument values for this bean (never {@code null}).
     */
    @Override
    public ConstructorArgumentValues getConstructorArgumentValues() {
        if (this.constructorArgumentValues == null) {
            this.constructorArgumentValues = new ConstructorArgumentValues();
        }
        return this.constructorArgumentValues;
    }

    /**
     * Return if there are constructor argument values defined for this bean.
     */
    @Override
    public boolean hasConstructorArgumentValues() {
        return (this.constructorArgumentValues != null && !this.constructorArgumentValues.isEmpty());
    }

    /**
     * Specify property values for this bean, if any.
     */
    public void setPropertyValues(MutablePropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    /**
     * Return property values for this bean (never {@code null}).
     */
    @Override
    public MutablePropertyValues getPropertyValues() {
        if (this.propertyValues == null) {
            this.propertyValues = new MutablePropertyValues();
        }
        return this.propertyValues;
    }

    /**
     * Return if there are property values values defined for this bean.
     * @since 5.0.2
     */
    @Override
    public boolean hasPropertyValues() {
        return (this.propertyValues != null && !this.propertyValues.isEmpty());
    }

    /**
     * Specify method overrides for the bean, if any.
     */
    public void setMethodOverrides(MethodOverrides methodOverrides) {
        this.methodOverrides = methodOverrides;
    }

    /**
     * Return information about methods to be overridden by the IoC
     * container. This will be empty if there are no method overrides.
     * <p>Never returns {@code null}.
     */
    public MethodOverrides getMethodOverrides() {
        return this.methodOverrides;
    }
    @Override
    public void setDestroyMethodName(@Nullable String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    /**
     * Return the name of the destroy method.
     */
    @Override
    @Nullable
    public String getDestroyMethodName() {
        return this.destroyMethodName;
    }


    @Override
    public void setRole(int role) {
        this.role = role;
    }

    /**
     * Return the role hint for this {@code BeanDefinition}.
     */
    @Override
    public int getRole() {
        return this.role;
    }

    /**
     * Set a human-readable description of this bean definition.
     */
    @Override
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    /**
     * Return a human-readable description of this bean definition.
     */
    @Override
    @Nullable
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setInitMethodName(@Nullable String initMethodName) {
        this.initMethodName = initMethodName;
    }

    /**
     * Return the name of the initializer method.
     */
    @Override
    @Nullable
    public String getInitMethodName() {
        return this.initMethodName;
    }

    public boolean isNonPublicAccessAllowed() {
        return nonPublicAccessAllowed;
    }

    public void setNonPublicAccessAllowed(boolean nonPublicAccessAllowed) {
        this.nonPublicAccessAllowed = nonPublicAccessAllowed;
    }

    public boolean isLenientConstructorResolution() {
        return lenientConstructorResolution;
    }

    public void setLenientConstructorResolution(boolean lenientConstructorResolution) {
        this.lenientConstructorResolution = lenientConstructorResolution;
    }

    public boolean isSynthetic() {
        return synthetic;
    }

    public void setSynthetic(boolean synthetic) {
        this.synthetic = synthetic;
    }
}
