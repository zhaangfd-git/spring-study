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

public class CompositeStringExpression implements Expression {
    private final String expressionString;
    private final Expression[] expressions;

    public CompositeStringExpression(String expressionString, Expression[] expressions) {
        this.expressionString = expressionString;
        this.expressions = expressions;
    }

    public final String getExpressionString() {
        return this.expressionString;
    }

    public final Expression[] getExpressions() {
        return this.expressions;
    }

    public String getValue() throws EvaluationException {
        StringBuilder sb = new StringBuilder();
        Expression[] var2 = this.expressions;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Expression expression = var2[var4];
            String value = (String)expression.getValue(String.class);
            if (value != null) {
                sb.append(value);
            }
        }

        return sb.toString();
    }

    @Nullable
    public <T> T getValue(@Nullable Class<T> expectedResultType) throws EvaluationException {
        Object value = this.getValue();
        return ExpressionUtils.convertTypedValue((EvaluationContext)null, new TypedValue(value), expectedResultType);
    }

    public String getValue(@Nullable Object rootObject) throws EvaluationException {
        StringBuilder sb = new StringBuilder();
        Expression[] var3 = this.expressions;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Expression expression = var3[var5];
            String value = (String)expression.getValue(rootObject, String.class);
            if (value != null) {
                sb.append(value);
            }
        }

        return sb.toString();
    }

    @Nullable
    public <T> T getValue(@Nullable Object rootObject, @Nullable Class<T> desiredResultType) throws EvaluationException {
        Object value = this.getValue(rootObject);
        return ExpressionUtils.convertTypedValue((EvaluationContext)null, new TypedValue(value), desiredResultType);
    }

    public String getValue(EvaluationContext context) throws EvaluationException {
        StringBuilder sb = new StringBuilder();
        Expression[] var3 = this.expressions;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Expression expression = var3[var5];
            String value = (String)expression.getValue(context, String.class);
            if (value != null) {
                sb.append(value);
            }
        }

        return sb.toString();
    }

    @Nullable
    public <T> T getValue(EvaluationContext context, @Nullable Class<T> expectedResultType) throws EvaluationException {
        Object value = this.getValue(context);
        return ExpressionUtils.convertTypedValue(context, new TypedValue(value), expectedResultType);
    }

    public String getValue(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
        StringBuilder sb = new StringBuilder();
        Expression[] var4 = this.expressions;
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Expression expression = var4[var6];
            String value = (String)expression.getValue(context, rootObject, String.class);
            if (value != null) {
                sb.append(value);
            }
        }

        return sb.toString();
    }

    @Nullable
    public <T> T getValue(EvaluationContext context, @Nullable Object rootObject, @Nullable Class<T> desiredResultType) throws EvaluationException {
        Object value = this.getValue(context, rootObject);
        return ExpressionUtils.convertTypedValue(context, new TypedValue(value), desiredResultType);
    }

    public Class<?> getValueType() {
        return String.class;
    }

    public Class<?> getValueType(EvaluationContext context) {
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
        throw new EvaluationException(this.expressionString, "Cannot call setValue on a composite expression");
    }

    public void setValue(EvaluationContext context, @Nullable Object value) throws EvaluationException {
        throw new EvaluationException(this.expressionString, "Cannot call setValue on a composite expression");
    }

    public void setValue(EvaluationContext context, @Nullable Object rootObject, @Nullable Object value) throws EvaluationException {
        throw new EvaluationException(this.expressionString, "Cannot call setValue on a composite expression");
    }
}
