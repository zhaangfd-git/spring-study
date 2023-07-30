package com.zhangfd.spring.factory.support;

import com.zhangfd.spring.BeanWrapper;
import com.zhangfd.spring.core.DefaultParameterNameDiscoverer;
import com.zhangfd.spring.core.ParameterNameDiscoverer;
import com.zhangfd.spring.exception.BeanCreationException;
import com.zhangfd.spring.factory.config.AutowireCapableBeanFactory;
import com.zhangfd.spring.lang.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory  implements AutowireCapableBeanFactory {


    @Nullable
    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();


   //创建实例的策略：普通的SimpleInstantiationStrategy 及CglibCglibSubclassingInstantiationStrategy测
    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();


    /** Cache of unfinished FactoryBean instances: FactoryBean name to BeanWrapper. */
    private final ConcurrentMap<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();


    @Nullable
    public ParameterNameDiscoverer getParameterNameDiscoverer() {
        return parameterNameDiscoverer;
    }

    public void setParameterNameDiscoverer(@Nullable ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    /**
     * Return the instantiation strategy to use for creating bean instances.
     */
    protected InstantiationStrategy getInstantiationStrategy() {
        return this.instantiationStrategy;
    }


    protected Object doCreateBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
            throws BeanCreationException {

        BeanWrapper instanceWrapper = null;
        if (mbd.isSingleton()) {
            instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
        }

        if (instanceWrapper == null) {
            instanceWrapper = createBeanInstance(beanName, mbd, args);
        }




        return  null;


    }

    protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {



        return null;
    }


    }
