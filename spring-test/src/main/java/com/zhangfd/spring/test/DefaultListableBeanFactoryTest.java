package com.zhangfd.spring.test;

import com.zhangfd.spring.beans.PropertyEditorRegistrar;
import com.zhangfd.spring.beans.TypeConverter;
import com.zhangfd.spring.core.ParameterNameDiscoverer;
import com.zhangfd.spring.core.convert.ConversionService;
import com.zhangfd.spring.factory.DisposableBean;
import com.zhangfd.spring.factory.config.BeanExpressionResolver;
import com.zhangfd.spring.factory.config.BeanPostProcessor;
import com.zhangfd.spring.factory.config.Scope;
import com.zhangfd.spring.factory.support.DefaultListableBeanFactory;
import com.zhangfd.spring.factory.support.InstantiationStrategy;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.StringValueResolver;

import java.beans.PropertyEditor;
import java.lang.annotation.Annotation;

public class DefaultListableBeanFactoryTest {


    public static void main(String[] args) {
        DefaultListableBeanFactory de = new DefaultListableBeanFactory();
        de.clearMetadataCache();
        //判断容器中是否包含指定beanName的BeanDefinition
       de.containsBeanDefinition("beanName");
        //给定名字(bean的别名或真实名)，哦判断容器中是否汉包含该bean；
        de.containsBean("name");
        //根据bean的名字，注销该bean(从容器中把该单例除名)
        de.destroySingleton("beanName");
        //从容器中把该所有单例除名
        de.destroySingletons();
        //解决内部依赖问题
        //de.doResolveDependency(); de.resolveDependency(); de.doResolveDependency();
        //获取指定bean上的指定注解类型的注解信息
        //de.findAnnotationOnBean( "beanName", Class<A> annotationType);
        //冻结配置：不允许在向spring容器注册对象
        de.freezeConfiguration();
        //确保所有非懒加载的对象都已经创建实例对象（包含factoryBean创建的对象）
        de.preInstantiateSingletons();
        //移除已经定义的bean
        de.removeBeanDefinition("beanName");


        //getter方法
        //返回AutowireCandidateResolver的实现类SimpleAutowireCandidateResolver对象
        de.getAutowireCandidateResolver();
        //根据指定的beanName，获取BeanDefinition
        de.getBeanDefinition("beanName");
        de.getBeanDefinitionCount();
        de.getBeanDefinitionNames();

        //根据给定的名字或类类型，返回这个bean实例
        //de.getBean();

        //给定类类型，获取ObjectProvider对象
        //de.getBeanProvider(Class<T> requiredType);

        //给定类类型，比如给定接口名，获取所有实现类的实例名集合
        // de.getBeanNamesForType(Class<?> type);
        //给定类类型，比如给定接口名，获取所有实现类的实例集合
       // de.getBeansOfType( Class<T> type);

        //根据给定的注解类类型，返回使用了这个注解的beanName集合
        //  de.getBeanNamesForAnnotation(Class<? extends Annotation > annotationType);
        //根据给定的注解类类型，返回使用了这个注解的bean集合
        //de.getBeansWithAnnotation(Class<? extends Annotation> annotationType);

        de.getSerializationId();



        //register方法
        //指定的类类型，需要依赖autowiredValue值
        //de.registerResolvableDependency(Class<?> dependencyType, @Nullable Object autowiredValue);

        //给定类类型，移除出bean容器中去
        //de.resolveNamedBean(Class<T> requiredType);

        //给定beanName，依赖指定的dependentBeanName
        de.registerDependentBean("beanName",  "dependentBeanName");
        //注册指定的bean，需要被销毁
        //de.registerDisposableBean( "beanName", DisposableBean bean);
        //给beanName注册别名aliasName
        de.registerAlias("beanName",  "aliasName");

        //指定的类类型，注册之定义的属性编辑器
        //de.registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor > propertyEditorClass);

        //指定的beanName，注册scope
       // de.registerScope("beanName", Scope scope);


        //adder方法
        //指定的beanName，和singletonObject，注册到单例集合中去
        //de.addSingleton("beanName", Object singletonObject);

        //添加StringValueResolver实现类，解析@value属性
        //de.addEmbeddedValueResolver(StringValueResolver valueResolver);

        //添加BeanPostProcessor beanPostProcessor实现类
        //de.addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

        //添加PropertyEditorRegistrar registrar实现类
        //de.addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);

       // setter
        //设置BeanExpressionResolver resolver实现类
       // de.setBeanExpressionResolver(BeanExpressionResolver resolver);

        //设置ConversionService conversionService实现类
        //de.setConversionService(ConversionService conversionService);

        //设置InstantiationStrategy instantiationStrategy实现类，默认是CglibSubclassingInstantiationStrategy
        //用于创建对象的封装操作
       // de.setInstantiationStrategy(InstantiationStrategy instantiationStrategy);

        //设置类型转换器TypeConverter typeConverter的实现类
        //de.setTypeConverter(TypeConverter typeConverter);

        //设置 ParameterNameDiscoverer parameterNameDiscoverer的实现类，默认是DefaultParameterNameDiscoverer
        //用于发现方法或构造方法的参数信息
        //de.setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer;
    }

}
