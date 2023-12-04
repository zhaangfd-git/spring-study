//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel;


import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.lang.Nullable;

public interface SpelNode {
    @Nullable
    Object getValue(ExpressionState var1) throws EvaluationException;

    TypedValue getTypedValue(ExpressionState var1) throws EvaluationException;

    boolean isWritable(ExpressionState var1) throws EvaluationException;

    void setValue(ExpressionState var1, @Nullable Object var2) throws EvaluationException;

    String toStringAST();

    int getChildCount();

    SpelNode getChild(int var1);

    @Nullable
    Class<?> getObjectClass(@Nullable Object var1);

    int getStartPosition();

    int getEndPosition();
}
