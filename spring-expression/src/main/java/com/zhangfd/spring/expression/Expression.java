//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression;

import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.lang.Nullable;


public interface Expression {
    String getExpressionString();

    @Nullable
    Object getValue() throws EvaluationException;

    @Nullable
    <T> T getValue(@Nullable Class<T> var1) throws EvaluationException;

    @Nullable
    Object getValue(@Nullable Object var1) throws EvaluationException;

    @Nullable
    <T> T getValue(@Nullable Object var1, @Nullable Class<T> var2) throws EvaluationException;

    @Nullable
    Object getValue(EvaluationContext var1) throws EvaluationException;

    @Nullable
    Object getValue(EvaluationContext var1, @Nullable Object var2) throws EvaluationException;

    @Nullable
    <T> T getValue(EvaluationContext var1, @Nullable Class<T> var2) throws EvaluationException;

    @Nullable
    <T> T getValue(EvaluationContext var1, @Nullable Object var2, @Nullable Class<T> var3) throws EvaluationException;

    @Nullable
    Class<?> getValueType() throws EvaluationException;

    @Nullable
    Class<?> getValueType(@Nullable Object var1) throws EvaluationException;

    @Nullable
    Class<?> getValueType(EvaluationContext var1) throws EvaluationException;

    @Nullable
    Class<?> getValueType(EvaluationContext var1, @Nullable Object var2) throws EvaluationException;

    @Nullable
    TypeDescriptor getValueTypeDescriptor() throws EvaluationException;

    @Nullable
    TypeDescriptor getValueTypeDescriptor(@Nullable Object var1) throws EvaluationException;

    @Nullable
    TypeDescriptor getValueTypeDescriptor(EvaluationContext var1) throws EvaluationException;

    @Nullable
    TypeDescriptor getValueTypeDescriptor(EvaluationContext var1, @Nullable Object var2) throws EvaluationException;

    boolean isWritable(@Nullable Object var1) throws EvaluationException;

    boolean isWritable(EvaluationContext var1) throws EvaluationException;

    boolean isWritable(EvaluationContext var1, @Nullable Object var2) throws EvaluationException;

    void setValue(@Nullable Object var1, @Nullable Object var2) throws EvaluationException;

    void setValue(EvaluationContext var1, @Nullable Object var2) throws EvaluationException;

    void setValue(EvaluationContext var1, @Nullable Object var2, @Nullable Object var3) throws EvaluationException;
}
