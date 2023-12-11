package com.zhangfd.spring.context.test;

import com.zhangfd.spring.context.expression.StandardBeanExpressionResolver;
import com.zhangfd.spring.factory.config.BeanExpressionContext;
import com.zhangfd.spring.factory.config.ConfigurableBeanFactory;
import com.zhangfd.spring.factory.config.Scope;
import com.zhangfd.spring.factory.support.DefaultListableBeanFactory;

/**
 * @author: zhangfd
 * @date: 2023/12/11 21:29
 * @version: 1.0
 * @describe:
 */
public class StandardBeanExpressionResolverTest {

    public static void main(String[] args) {

        StandardBeanExpressionResolver resolver = new StandardBeanExpressionResolver();
        BeanExpressionContextTest beanExpressionContextTest = new BeanExpressionContextTest(new DefaultListableBeanFactory(),null);
        resolver.evaluate("#{123}",beanExpressionContextTest);

    }
}

class BeanExpressionContextTest extends BeanExpressionContext{

    public BeanExpressionContextTest(ConfigurableBeanFactory beanFactory, Scope scope) {
        super(beanFactory, scope);
    }

    @Override
    public Object getObject(String key) {
        return new TestObject();
    }
}


class TestObject{


    private  String name ;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}