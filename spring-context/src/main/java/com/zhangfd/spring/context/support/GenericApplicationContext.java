package com.zhangfd.spring.context.support;

import com.zhangfd.spring.BeansException;
import com.zhangfd.spring.context.ApplicationContext;
import com.zhangfd.spring.factory.config.AutowireCapableBeanFactory;
import com.zhangfd.spring.factory.config.ConfigurableListableBeanFactory;
import com.zhangfd.spring.factory.support.DefaultListableBeanFactory;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;
import com.zhangfd.spring.util.ObjectUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: zhangfd
 * @date: 2023/12/4 20:42
 * @version: 1.0
 * @describe:
 */
public class GenericApplicationContext  extends AbstractApplicationContext{


    private final DefaultListableBeanFactory beanFactory;

    private final AtomicBoolean refreshed = new AtomicBoolean();

    private String id = ObjectUtils.identityToString(this);

    public GenericApplicationContext() {
        this.beanFactory = new DefaultListableBeanFactory();
    }

    /**
     * Create a new GenericApplicationContext with the given DefaultListableBeanFactory.
     * @param beanFactory the DefaultListableBeanFactory instance to use for this context
     * @see #
     * @see #refresh
     */
    public GenericApplicationContext(DefaultListableBeanFactory beanFactory) {
        Assert.notNull(beanFactory, "BeanFactory must not be null");
        this.beanFactory = beanFactory;
    }

    public GenericApplicationContext(@Nullable ApplicationContext parent) {
        this();
        setParent(parent);
    }


    public GenericApplicationContext(DefaultListableBeanFactory beanFactory, ApplicationContext parent) {
        this(beanFactory);
        setParent(parent);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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
    public void setParent(ApplicationContext parent) {

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

        if (!this.refreshed.compareAndSet(false, true)) {
            throw new IllegalStateException(
                    "GenericApplicationContext does not support multiple refresh attempts: just call 'refresh' once");
        }
        this.beanFactory.setSerializationId(getId());
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }
}
