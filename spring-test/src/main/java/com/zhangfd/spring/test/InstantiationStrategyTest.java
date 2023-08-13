package com.zhangfd.spring.test;

import com.zhangfd.spring.BeanUtils;
import com.zhangfd.spring.core.ResolvableType;
import com.zhangfd.spring.factory.support.RootBeanDefinition;
import com.zhangfd.spring.factory.support.SimpleInstantiationStrategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.List;

public class InstantiationStrategyTest {

    public static void main(String[] args) throws Exception {

       // test1(String.class );
       // test2(String.class );
       // test3(String.class );

    }

    /**
     * 对给定的类，通过反射，创建实例
     * @throws Exception
     */
    public static   void  test1( Class<?> clazz ) throws Exception{
        Constructor<?> constructorToUse;
        if (System.getSecurityManager() != null) {
            constructorToUse = AccessController.doPrivileged(
                    (PrivilegedExceptionAction<Constructor<?>>) clazz::getDeclaredConstructor);
        }
        else {
            constructorToUse = clazz.getDeclaredConstructor();
        }

        Object o = BeanUtils.instantiateClass(constructorToUse);

        System.out.println(o);
    }

    /**
     * 对于cglib创建的类，通过反射，创建对象 :CglibSubclassingInstantiationStrategy
     * @param clazz CglibSubclassingInstantiationStrategy#instantiateWithMethodInjection(RootBeanDefinition, String, aBeanFactory)
     */
    public static   void   test2( Class<?> clazz ){

    }

    /**
     * 把以上两种类型创建实例的过程合并到一起: SimpleInstantiationStrategy
     * SimpleInstantiationStrategy#instantiate(RootBeanDefinition, String, BeanFactory)
     * @param clazz
     */
    public static   void   test3( Class<?> clazz ){
        RootBeanDefinition bd = new RootBeanDefinition(null);
        bd.setBeanClass(clazz);

        SimpleInstantiationStrategy simpleInstantiationStrategy = new SimpleInstantiationStrategy();
        Object instantiate = simpleInstantiationStrategy.instantiate(bd,null,null);
        System.out.println(instantiate);
    }




    /**
     * 有参数的构造方法进行实例化
     * @param clazz
     */
    public static   void   test5( Class<?> clazz ){
        RootBeanDefinition bd = new RootBeanDefinition(null);
        bd.setBeanClass(clazz);

        SimpleInstantiationStrategy simpleInstantiationStrategy = new SimpleInstantiationStrategy();
        Object instantiate = simpleInstantiationStrategy.instantiate(bd,null,null);
        System.out.println(instantiate);
    }


}
