//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression;


import com.zhangfd.spring.lang.Nullable;

import java.util.List;

/**
 * 解析属性、方法、字段，并帮助执行类型转换
 */
public interface EvaluationContext {
    TypedValue getRootObject();

    List<PropertyAccessor> getPropertyAccessors();

    List<ConstructorResolver> getConstructorResolvers();

    List<MethodResolver> getMethodResolvers();

    @Nullable
    BeanResolver getBeanResolver();

    TypeLocator getTypeLocator();

    TypeConverter getTypeConverter();

    TypeComparator getTypeComparator();

    OperatorOverloader getOperatorOverloader();

    void setVariable(String var1, @Nullable Object var2);

    @Nullable
    Object lookupVariable(String var1);
}
