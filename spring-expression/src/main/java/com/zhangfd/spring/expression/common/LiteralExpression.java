//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.common;


import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.expression.EvaluationContext;
import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.Expression;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.lang.Nullable;

public class LiteralExpression implements Expression {
    private final String literalValue;

    public LiteralExpression(String literalValue) {
        this.literalValue = literalValue;
    }

    public final String getExpressionString() {
        return this.literalValue;
    }

    public Class<?> getValueType(EvaluationContext context) {
        return String.class;
    }

    public String getValue() {
        return this.literalValue;
    }

    @Nullable
    public <T> T getValue(@Nullable Class<T> expectedResultType) throws EvaluationException {
        Object value = this.getValue();
        return ExpressionUtils.convertTypedValue((EvaluationContext)null, new TypedValue(value), expectedResultType);
    }

    public String getValue(@Nullable Object rootObject) {
        return this.literalValue;
    }

    @Nullable
    public <T> T getValue(@Nullable Object rootObject, @Nullable Class<T> desiredResultType) throws EvaluationException {
        Object value = this.getValue(rootObject);
        return ExpressionUtils.convertTypedValue((EvaluationContext)null, new TypedValue(value), desiredResultType);
    }

    public String getValue(EvaluationContext context) {
        return this.literalValue;
    }

    @Nullable
    public <T> T getValue(EvaluationContext context, @Nullable Class<T> expectedResultType) throws EvaluationException {
        Object value = this.getValue(context);
        return ExpressionUtils.convertTypedValue(context, new TypedValue(value), expectedResultType);
    }

    public String getValue(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
        return this.literalValue;
    }

    @Nullable
    public <T> T getValue(EvaluationContext context, @Nullable Object rootObject, @Nullable Class<T> desiredResultType) throws EvaluationException {
        Object value = this.getValue(context, rootObject);
        return ExpressionUtils.convertTypedValue(context, new TypedValue(value), desiredResultType);
    }

    public Class<?> getValueType() {
        return String.class;
    }

    public Class<?> getValueType(@Nullable Object rootObject) throws EvaluationException {
        return String.class;
    }

    public Class<?> getValueType(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
        return String.class;
    }

    public TypeDescriptor getValueTypeDescriptor() {
        return TypeDescriptor.valueOf(String.class);
    }

    public TypeDescriptor getValueTypeDescriptor(@Nullable Object rootObject) throws EvaluationException {
        return TypeDescriptor.valueOf(String.class);
    }

    public TypeDescriptor getValueTypeDescriptor(EvaluationContext context) {
        return TypeDescriptor.valueOf(String.class);
    }

    public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
        return TypeDescriptor.valueOf(String.class);
    }

    public boolean isWritable(@Nullable Object rootObject) throws EvaluationException {
        return false;
    }

    public boolean isWritable(EvaluationContext context) {
        return false;
    }

    public boolean isWritable(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
        return false;
    }

    public void setValue(@Nullable Object rootObject, @Nullable Object value) throws EvaluationException {
        throw new EvaluationException(this.literalValue, "Cannot call setValue() on a LiteralExpression");
    }

    public void setValue(EvaluationContext context, @Nullable Object value) throws EvaluationException {
        throw new EvaluationException(this.literalValue, "Cannot call setValue() on a LiteralExpression");
    }

    public void setValue(EvaluationContext context, @Nullable Object rootObject, @Nullable Object value) throws EvaluationException {
        throw new EvaluationException(this.literalValue, "Cannot call setValue() on a LiteralExpression");
    }
}
