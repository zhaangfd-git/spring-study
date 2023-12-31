//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.standard;

import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.expression.EvaluationContext;
import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.Expression;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.expression.common.ExpressionUtils;
import com.zhangfd.spring.expression.spel.*;
import com.zhangfd.spring.expression.spel.ast.SpelNodeImpl;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;

import java.util.concurrent.atomic.AtomicInteger;


public class SpelExpression implements Expression {
    private static final int INTERPRETED_COUNT_THRESHOLD = 100;
    private static final int FAILED_ATTEMPTS_THRESHOLD = 100;
    private final String expression;
    private final SpelNodeImpl ast;
    private final SpelParserConfiguration configuration;
    @Nullable
    private EvaluationContext evaluationContext;
    @Nullable
    private volatile CompiledExpression compiledAst;
    private final AtomicInteger interpretedCount = new AtomicInteger(0);
    private final AtomicInteger failedAttempts = new AtomicInteger(0);

    public SpelExpression(String expression, SpelNodeImpl ast, SpelParserConfiguration configuration) {
        this.expression = expression;
        this.ast = ast;
        this.configuration = configuration;
    }

    public void setEvaluationContext(EvaluationContext evaluationContext) {
        this.evaluationContext = evaluationContext;
    }

    public EvaluationContext getEvaluationContext() {
        if (this.evaluationContext == null) {
            this.evaluationContext = new StandardEvaluationContext();
        }

        return this.evaluationContext;
    }

    public String getExpressionString() {
        return this.expression;
    }

    @Nullable
    public Object getValue() throws EvaluationException {
        CompiledExpression compiledAst = this.compiledAst;
        if (compiledAst != null) {
            try {
                EvaluationContext context = this.getEvaluationContext();
                return compiledAst.getValue(context.getRootObject().getValue(), context);
            } catch (Throwable var4) {
                if (this.configuration.getCompilerMode() != SpelCompilerMode.MIXED) {
                    throw new SpelEvaluationException(var4, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
                }
            }

            this.compiledAst = null;
            this.interpretedCount.set(0);
        }

        ExpressionState expressionState = new ExpressionState(this.getEvaluationContext(), this.configuration);
        Object result = this.ast.getValue(expressionState);
        this.checkCompile(expressionState);
        return result;
    }

    @Nullable
    public <T> T getValue(@Nullable Class<T> expectedResultType) throws EvaluationException {
        CompiledExpression compiledAst = this.compiledAst;
        if (compiledAst != null) {
            try {
                EvaluationContext context = this.getEvaluationContext();
                Object result = compiledAst.getValue(context.getRootObject().getValue(), context);
                if (expectedResultType == null) {
                    return (T) result;
                }

                return ExpressionUtils.convertTypedValue(this.getEvaluationContext(), new TypedValue(result), expectedResultType);
            } catch (Throwable var5) {
                if (this.configuration.getCompilerMode() != SpelCompilerMode.MIXED) {
                    throw new SpelEvaluationException(var5, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
                }
            }

            this.compiledAst = null;
            this.interpretedCount.set(0);
        }

        ExpressionState expressionState = new ExpressionState(this.getEvaluationContext(), this.configuration);
        TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
        this.checkCompile(expressionState);
        return ExpressionUtils.convertTypedValue(expressionState.getEvaluationContext(), typedResultValue, expectedResultType);
    }

    @Nullable
    public Object getValue(@Nullable Object rootObject) throws EvaluationException {
        CompiledExpression compiledAst = this.compiledAst;
        if (compiledAst != null) {
            try {
                return compiledAst.getValue(rootObject, this.getEvaluationContext());
            } catch (Throwable var5) {
                if (this.configuration.getCompilerMode() != SpelCompilerMode.MIXED) {
                    throw new SpelEvaluationException(var5, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
                }
            }

            this.compiledAst = null;
            this.interpretedCount.set(0);
        }

        ExpressionState expressionState = new ExpressionState(this.getEvaluationContext(), this.toTypedValue(rootObject), this.configuration);
        Object result = this.ast.getValue(expressionState);
        this.checkCompile(expressionState);
        return result;
    }

    @Nullable
    public <T> T getValue(@Nullable Object rootObject, @Nullable Class<T> expectedResultType) throws EvaluationException {
        CompiledExpression compiledAst = this.compiledAst;
        if (compiledAst != null) {
            try {
                Object result = compiledAst.getValue(rootObject, this.getEvaluationContext());
                if (expectedResultType == null) {
                    return (T) result;
                }

                return ExpressionUtils.convertTypedValue(this.getEvaluationContext(), new TypedValue(result), expectedResultType);
            } catch (Throwable var6) {
                if (this.configuration.getCompilerMode() != SpelCompilerMode.MIXED) {
                    throw new SpelEvaluationException(var6, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
                }
            }

            this.compiledAst = null;
            this.interpretedCount.set(0);
        }

        ExpressionState expressionState = new ExpressionState(this.getEvaluationContext(), this.toTypedValue(rootObject), this.configuration);
        TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
        this.checkCompile(expressionState);
        return ExpressionUtils.convertTypedValue(expressionState.getEvaluationContext(), typedResultValue, expectedResultType);
    }

    @Nullable
    public Object getValue(EvaluationContext context) throws EvaluationException {
        Assert.notNull(context, "EvaluationContext is required");
        CompiledExpression compiledAst = this.compiledAst;
        if (compiledAst != null) {
            try {
                return compiledAst.getValue(context.getRootObject().getValue(), context);
            } catch (Throwable var5) {
                if (this.configuration.getCompilerMode() != SpelCompilerMode.MIXED) {
                    throw new SpelEvaluationException(var5, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
                }
            }

            this.compiledAst = null;
            this.interpretedCount.set(0);
        }

        ExpressionState expressionState = new ExpressionState(context, this.configuration);
        Object result = this.ast.getValue(expressionState);
        this.checkCompile(expressionState);
        return result;
    }

    @Nullable
    public <T> T getValue(EvaluationContext context, @Nullable Class<T> expectedResultType) throws EvaluationException {
        Assert.notNull(context, "EvaluationContext is required");
        CompiledExpression compiledAst = this.compiledAst;
        if (compiledAst != null) {
            try {
                Object result = compiledAst.getValue(context.getRootObject().getValue(), context);
                if (expectedResultType != null) {
                    return ExpressionUtils.convertTypedValue(context, new TypedValue(result), expectedResultType);
                }

                return (T) result;
            } catch (Throwable var6) {
                if (this.configuration.getCompilerMode() != SpelCompilerMode.MIXED) {
                    throw new SpelEvaluationException(var6, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
                }
            }

            this.compiledAst = null;
            this.interpretedCount.set(0);
        }

        ExpressionState expressionState = new ExpressionState(context, this.configuration);
        TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
        this.checkCompile(expressionState);
        return ExpressionUtils.convertTypedValue(context, typedResultValue, expectedResultType);
    }

    @Nullable
    public Object getValue(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
        Assert.notNull(context, "EvaluationContext is required");
        CompiledExpression compiledAst = this.compiledAst;
        if (compiledAst != null) {
            try {
                return compiledAst.getValue(rootObject, context);
            } catch (Throwable var6) {
                if (this.configuration.getCompilerMode() != SpelCompilerMode.MIXED) {
                    throw new SpelEvaluationException(var6, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
                }
            }

            this.compiledAst = null;
            this.interpretedCount.set(0);
        }

        ExpressionState expressionState = new ExpressionState(context, this.toTypedValue(rootObject), this.configuration);
        Object result = this.ast.getValue(expressionState);
        this.checkCompile(expressionState);
        return result;
    }

    @Nullable
    public <T> T getValue(EvaluationContext context, @Nullable Object rootObject, @Nullable Class<T> expectedResultType) throws EvaluationException {
        Assert.notNull(context, "EvaluationContext is required");
        CompiledExpression compiledAst = this.compiledAst;
        if (compiledAst != null) {
            try {
                Object result = compiledAst.getValue(rootObject, context);
                if (expectedResultType != null) {
                    return ExpressionUtils.convertTypedValue(context, new TypedValue(result), expectedResultType);
                }

                return (T) result;
            } catch (Throwable var7) {
                if (this.configuration.getCompilerMode() != SpelCompilerMode.MIXED) {
                    throw new SpelEvaluationException(var7, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
                }
            }

            this.compiledAst = null;
            this.interpretedCount.set(0);
        }

        ExpressionState expressionState = new ExpressionState(context, this.toTypedValue(rootObject), this.configuration);
        TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
        this.checkCompile(expressionState);
        return ExpressionUtils.convertTypedValue(context, typedResultValue, expectedResultType);
    }

    @Nullable
    public Class<?> getValueType() throws EvaluationException {
        return this.getValueType(this.getEvaluationContext());
    }

    @Nullable
    public Class<?> getValueType(@Nullable Object rootObject) throws EvaluationException {
        return this.getValueType(this.getEvaluationContext(), rootObject);
    }

    @Nullable
    public Class<?> getValueType(EvaluationContext context) throws EvaluationException {
        Assert.notNull(context, "EvaluationContext is required");
        ExpressionState expressionState = new ExpressionState(context, this.configuration);
        TypeDescriptor typeDescriptor = this.ast.getValueInternal(expressionState).getTypeDescriptor();
        return typeDescriptor != null ? typeDescriptor.getType() : null;
    }

    @Nullable
    public Class<?> getValueType(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
        ExpressionState expressionState = new ExpressionState(context, this.toTypedValue(rootObject), this.configuration);
        TypeDescriptor typeDescriptor = this.ast.getValueInternal(expressionState).getTypeDescriptor();
        return typeDescriptor != null ? typeDescriptor.getType() : null;
    }

    @Nullable
    public TypeDescriptor getValueTypeDescriptor() throws EvaluationException {
        return this.getValueTypeDescriptor(this.getEvaluationContext());
    }

    @Nullable
    public TypeDescriptor getValueTypeDescriptor(@Nullable Object rootObject) throws EvaluationException {
        ExpressionState expressionState = new ExpressionState(this.getEvaluationContext(), this.toTypedValue(rootObject), this.configuration);
        return this.ast.getValueInternal(expressionState).getTypeDescriptor();
    }

    @Nullable
    public TypeDescriptor getValueTypeDescriptor(EvaluationContext context) throws EvaluationException {
        Assert.notNull(context, "EvaluationContext is required");
        ExpressionState expressionState = new ExpressionState(context, this.configuration);
        return this.ast.getValueInternal(expressionState).getTypeDescriptor();
    }

    @Nullable
    public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
        Assert.notNull(context, "EvaluationContext is required");
        ExpressionState expressionState = new ExpressionState(context, this.toTypedValue(rootObject), this.configuration);
        return this.ast.getValueInternal(expressionState).getTypeDescriptor();
    }

    public boolean isWritable(@Nullable Object rootObject) throws EvaluationException {
        return this.ast.isWritable(new ExpressionState(this.getEvaluationContext(), this.toTypedValue(rootObject), this.configuration));
    }

    public boolean isWritable(EvaluationContext context) throws EvaluationException {
        Assert.notNull(context, "EvaluationContext is required");
        return this.ast.isWritable(new ExpressionState(context, this.configuration));
    }

    public boolean isWritable(EvaluationContext context, @Nullable Object rootObject) throws EvaluationException {
        Assert.notNull(context, "EvaluationContext is required");
        return this.ast.isWritable(new ExpressionState(context, this.toTypedValue(rootObject), this.configuration));
    }

    public void setValue(@Nullable Object rootObject, @Nullable Object value) throws EvaluationException {
        this.ast.setValue(new ExpressionState(this.getEvaluationContext(), this.toTypedValue(rootObject), this.configuration), value);
    }

    public void setValue(EvaluationContext context, @Nullable Object value) throws EvaluationException {
        Assert.notNull(context, "EvaluationContext is required");
        this.ast.setValue(new ExpressionState(context, this.configuration), value);
    }

    public void setValue(EvaluationContext context, @Nullable Object rootObject, @Nullable Object value) throws EvaluationException {
        Assert.notNull(context, "EvaluationContext is required");
        this.ast.setValue(new ExpressionState(context, this.toTypedValue(rootObject), this.configuration), value);
    }

    private void checkCompile(ExpressionState expressionState) {
        this.interpretedCount.incrementAndGet();
        SpelCompilerMode compilerMode = expressionState.getConfiguration().getCompilerMode();
        if (compilerMode != SpelCompilerMode.OFF) {
            if (compilerMode == SpelCompilerMode.IMMEDIATE) {
                if (this.interpretedCount.get() > 1) {
                    this.compileExpression();
                }
            } else if (this.interpretedCount.get() > 100) {
                this.compileExpression();
            }
        }

    }

    public boolean compileExpression() {
        CompiledExpression compiledAst = this.compiledAst;
        if (compiledAst != null) {
            return true;
        } else if (this.failedAttempts.get() > 100) {
            return false;
        } else {
            synchronized(this) {
                if (this.compiledAst != null) {
                    return true;
                } else {
                    SpelCompiler compiler = SpelCompiler.getCompiler(this.configuration.getCompilerClassLoader());
                    compiledAst = compiler.compile(this.ast);
                    if (compiledAst != null) {
                        this.compiledAst = compiledAst;
                        return true;
                    } else {
                        this.failedAttempts.incrementAndGet();
                        return false;
                    }
                }
            }
        }
    }

    public void revertToInterpreted() {
        this.compiledAst = null;
        this.interpretedCount.set(0);
        this.failedAttempts.set(0);
    }

    public SpelNode getAST() {
        return this.ast;
    }

    public String toStringAST() {
        return this.ast.toStringAST();
    }

    private TypedValue toTypedValue(@Nullable Object object) {
        return object != null ? new TypedValue(object) : TypedValue.NULL;
    }
}
