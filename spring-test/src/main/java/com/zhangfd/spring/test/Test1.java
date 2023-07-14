package com.zhangfd.spring.test;

import com.zhangfd.spring.BeanUtils;
import com.zhangfd.spring.factory.support.RootBeanDefinition;
import com.zhangfd.spring.factory.support.SimpleInstantiationStrategy;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

public class Test1 {

    public static void main(String[] args) throws Exception {

       // test1(String.class );
       // test2(String.class );
        test3(String.class );

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
     * 对于cglib创建的类，通过反射，创建对象
     * @param clazz
     */
    public static   void   test2( Class<?> clazz ){

    }

    /**
     * 把以上两种类型创建实例的过程合并到一起
     * @param clazz
     */
    public static   void   test3( Class<?> clazz ){
        RootBeanDefinition bd = new RootBeanDefinition();
        bd.setBeanClass(clazz);

        SimpleInstantiationStrategy simpleInstantiationStrategy = new SimpleInstantiationStrategy();
        Object instantiate = simpleInstantiationStrategy.instantiate(bd,null,null);
        System.out.println(instantiate);
    }


    /**
     *   ResolvableType 类学习
     */
    public static   void   test4(){

    }




}
