package com.zhangfd.spring.test;

import com.zhangfd.spring.factory.support.DefaultListableBeanFactory;

public class DefaultListableBeanFactoryTest {


    public static void main(String[] args) {
        DefaultListableBeanFactory de = new DefaultListableBeanFactory();
        de.clearMetadataCache();

       /* de.containsBeanDefinition();
        de.containsBean();
        de.destroySingleton();
        de.destroySingletons();
        de.doResolveDependency();
        de.findAnnotationOnBean();
        de.freezeConfiguration();
        de.preInstantiateSingletons();
        de.resolveDependency();
        de.removeBeanDefinition();
        de.doResolveDependency();*/

        //getter方法
        /*de.getAutowireCandidateResolver();
        de.getBeanDefinition();
        de.getBeanDefinitionCount();
        de.getBeanDefinitionNames();
        de.getBeanNamesForAnnotation();
        de.getBean();
        de.getBeanNamesForType();
        de.getBeanProvider();
        de.getBeansOfType();
        de.getBeansWithAnnotation();
        de.getDependencyComparator();
        de.getSerializationId();*/



        //register方法
       /* de.registerResolvableDependency();
        de.resolveNamedBean();
        de.registerDependentBean();
        de.registerDisposableBean();
        de.registerAlias();
        de.registerCustomEditor();
        de.registerScope();*/


        //adder方法
       /* de.addSingleton();
        de.addEmbeddedValueResolver();
        de.addBeanPostProcessor();
        de.addPropertyEditorRegistrar();*/

       // setter
      /*  de.setBeanExpressionResolver();
        de.setConversionService();
        de.setInstantiationStrategy();
        de.setTypeConverter();
        de.setParameterNameDiscoverer();
        de.setInstantiationStrategy();*/
    }

}
