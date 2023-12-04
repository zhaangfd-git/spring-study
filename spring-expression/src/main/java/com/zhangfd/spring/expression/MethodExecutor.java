package com.zhangfd.spring.expression;

public interface MethodExecutor {
    TypedValue execute(EvaluationContext var1, Object var2, Object... var3) throws AccessException;
}