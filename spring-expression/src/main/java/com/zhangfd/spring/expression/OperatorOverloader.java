//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression;


import com.zhangfd.spring.lang.Nullable;

public interface OperatorOverloader {
    boolean overridesOperation(Operation var1, @Nullable Object var2, @Nullable Object var3) throws EvaluationException;

    Object operate(Operation var1, @Nullable Object var2, @Nullable Object var3) throws EvaluationException;
}
