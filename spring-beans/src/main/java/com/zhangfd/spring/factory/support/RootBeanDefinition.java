package com.zhangfd.spring.factory.support;

import com.zhangfd.spring.core.ResolvableType;
import com.zhangfd.spring.factory.config.BeanDefinition;
import com.zhangfd.spring.factory.config.BeanDefinitionHolder;
import com.zhangfd.spring.lang.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Set;

public class RootBeanDefinition  extends  AbstractBeanDefinition   {


    /** Common lock for the four constructor fields below. */
    final Object constructorArgumentLock = new Object();

    /** Package-visible field for caching the resolved constructor or factory method. */
    @Nullable
    Executable resolvedConstructorOrFactoryMethod;

    //是否解决了构造参数的标识符
    boolean constructorArgumentsResolved = false;

    //构造方法里具体的参数值
    @Nullable
    Object[] resolvedConstructorArguments;

    //备用的构造方法参数值
    @Nullable
    Object[] preparedConstructorArguments;

    // 在实例化一个类时，如有有工程方法，那这里就是记录工厂方法的变量
    @Nullable
    volatile Method factoryMethodToIntrospect;

    //标识是否是独一无二的工厂方法
    boolean isFactoryMethodUnique = false;

    //觉得beanDefinition是否需要被合并
    volatile boolean stale;

    @Nullable
    volatile Boolean beforeInstantiationResolved;


    @Nullable
    private BeanDefinitionHolder decoratedDefinition;

    //这个包装的bean的类型
    @Nullable
    volatile Class<?> resolvedTargetType;

    @Nullable
    volatile ResolvableType targetType;

     // 是否是factoryBean的bean
    @Nullable
    volatile Boolean isFactoryBean;

    //factoryBean的返回值类型
    @Nullable
    volatile ResolvableType factoryMethodReturnType;


    final Object postProcessingLock = new Object();
    /** Package-visible field that indicates MergedBeanDefinitionPostProcessor having been applied. */
    boolean postProcessed = false;

    @Nullable
    private Set<String> externallyManagedInitMethods;

    //允许缓存
    boolean allowCaching = true;

    @Nullable
    private Set<String> externallyManagedDestroyMethods;


    public RootBeanDefinition() {
        super();
    }

    public RootBeanDefinition(@Nullable Class<?> beanClass) {
        super();
        setBeanClass(beanClass);
    }

    public RootBeanDefinition(RootBeanDefinition original) {
        super(original);
        this.decoratedDefinition = original.decoratedDefinition;
       // this.qualifiedElement = original.qualifiedElement;
        //this.allowCaching = original.allowCaching;
        this.isFactoryMethodUnique = original.isFactoryMethodUnique;
        this.targetType = original.targetType;
        this.factoryMethodToIntrospect = original.factoryMethodToIntrospect;
    }
    RootBeanDefinition(BeanDefinition original) {
        super(original);
    }
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

    public boolean isExternallyManagedInitMethod(String initMethod) {
        synchronized (this.postProcessingLock) {
            return (this.externallyManagedInitMethods != null &&
                    this.externallyManagedInitMethods.contains(initMethod));
        }
    }

    public boolean isExternallyManagedDestroyMethod(String destroyMethod) {
        synchronized (this.postProcessingLock) {
            return (this.externallyManagedDestroyMethods != null &&
                    this.externallyManagedDestroyMethods.contains(destroyMethod));
        }
    }

    @Override
    public RootBeanDefinition cloneBeanDefinition() {
        return new RootBeanDefinition(this);
    }


    @Override
    public String getResourceDescription() {
        return null;
    }

    @Override
    public BeanDefinition getOriginatingBeanDefinition() {
        return null;
    }


    @Nullable
    public Method getResolvedFactoryMethod() {
        return this.factoryMethodToIntrospect;
    }

    public boolean isFactoryMethod(Method candidate) {
        return candidate.getName().equals(getFactoryMethodName());
    }


    public void setDecoratedDefinition(@Nullable BeanDefinitionHolder decoratedDefinition) {
        this.decoratedDefinition = decoratedDefinition;
    }

    /**
     * Return the target definition that is being decorated by this bean definition, if any.
     */
    @Nullable
    public BeanDefinitionHolder getDecoratedDefinition() {
        return this.decoratedDefinition;
    }

    public void setTargetType(@Nullable Class<?> targetType) {
        this.targetType = (targetType != null ? ResolvableType.forClass(targetType) : null);
    }

    /**
     * Return the target type of this bean definition, if known
     * (either specified in advance or resolved on first instantiation).
     * @since 3.2.2
     */
    @Nullable
    public Class<?> getTargetType() {
        if (this.resolvedTargetType != null) {
            return this.resolvedTargetType;
        }
        ResolvableType targetType = this.targetType;
        return (targetType != null ? targetType.resolve() : null);
    }

    @Nullable
    public Constructor<?>[] getPreferredConstructors() {
        return null;
    }

}
