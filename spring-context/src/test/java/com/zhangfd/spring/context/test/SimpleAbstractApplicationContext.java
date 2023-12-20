package com.zhangfd.spring.context.test;

import com.zhangfd.spring.BeansException;
import com.zhangfd.spring.context.ApplicationContext;
import com.zhangfd.spring.context.support.AbstractApplicationContext;
import com.zhangfd.spring.factory.config.ConfigurableListableBeanFactory;

/**
 * @author: zhangfd
 * @date: 2023/11/30 21:04
 * @version: 1.0
 * @describe:
 */

public class SimpleAbstractApplicationContext extends AbstractApplicationContext {


    @Override
    protected void refreshBeanFactory() throws BeansException, IllegalStateException {

    }

    @Override
    protected void closeBeanFactory() {

    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return null;
    }
}
