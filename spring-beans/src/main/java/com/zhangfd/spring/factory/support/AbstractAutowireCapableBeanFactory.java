package com.zhangfd.spring.factory.support;

import com.zhangfd.spring.core.DefaultParameterNameDiscoverer;
import com.zhangfd.spring.core.ParameterNameDiscoverer;
import com.zhangfd.spring.factory.config.AutowireCapableBeanFactory;
import com.zhangfd.spring.lang.Nullable;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory  implements AutowireCapableBeanFactory {


    @Nullable
    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();


   //创建实例的策略：普通的SimpleInstantiationStrategy 及cglibCglibSubclassingInstantiationStrategy测
    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();


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
}
