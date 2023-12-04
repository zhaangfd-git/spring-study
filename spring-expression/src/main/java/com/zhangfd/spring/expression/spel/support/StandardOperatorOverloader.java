//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.support;

import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.Operation;
import com.zhangfd.spring.expression.OperatorOverloader;
import com.zhangfd.spring.lang.Nullable;


public class StandardOperatorOverloader implements OperatorOverloader {
    public StandardOperatorOverloader() {
    }

    public boolean overridesOperation(Operation operation, @Nullable Object leftOperand, @Nullable Object rightOperand) throws EvaluationException {
        return false;
    }

    public Object operate(Operation operation, @Nullable Object leftOperand, @Nullable Object rightOperand) throws EvaluationException {
        throw new EvaluationException("No operation overloaded by default");
    }
}
