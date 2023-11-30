package com.zhangfd.spring.context.test;

import com.zhangfd.spring.BeansException;
import com.zhangfd.spring.context.ApplicationContext;
import com.zhangfd.spring.context.support.AbstractApplicationContext;
import com.zhangfd.spring.core.env.ConfigurableEnvironment;
import com.zhangfd.spring.factory.config.AutowireCapableBeanFactory;
import com.zhangfd.spring.factory.config.ConfigurableListableBeanFactory;

/**
 * @author: zhangfd
 * @date: 2023/11/30 21:04
 * @version: 1.0
 * @describe:
 */

public class SimpleAbstractApplicationContext extends AbstractApplicationContext {


    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getApplicationName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public long getStartupDate() {
        return 0;
    }

    @Override
    public ApplicationContext getParent() {
        return null;
    }

    @Override
    public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
        return null;
    }

    @Override
    public void publishEvent(Object event) {

    }

    @Override
    public void setId(String id) {

    }

    @Override
    public void setParent(ApplicationContext parent) {

    }

    @Override
    public void setEnvironment(ConfigurableEnvironment environment) {

    }



    @Override
    public void registerShutdownHook() {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    protected void refreshBeanFactory() throws BeansException, IllegalStateException {

    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return null;
    }
}
