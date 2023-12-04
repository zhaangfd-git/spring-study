//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel;

import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.expression.*;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;
import com.zhangfd.spring.util.CollectionUtils;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


public class ExpressionState {
    private final EvaluationContext relatedContext;
    private final TypedValue rootObject;
    private final SpelParserConfiguration configuration;
    @Nullable
    private Deque<TypedValue> contextObjects;
    @Nullable
    private Deque<ExpressionState.VariableScope> variableScopes;
    @Nullable
    private ArrayDeque<TypedValue> scopeRootObjects;

    public ExpressionState(EvaluationContext context) {
        this(context, context.getRootObject(), new SpelParserConfiguration(false, false));
    }

    public ExpressionState(EvaluationContext context, SpelParserConfiguration configuration) {
        this(context, context.getRootObject(), configuration);
    }

    public ExpressionState(EvaluationContext context, TypedValue rootObject) {
        this(context, rootObject, new SpelParserConfiguration(false, false));
    }

    public ExpressionState(EvaluationContext context, TypedValue rootObject, SpelParserConfiguration configuration) {
        Assert.notNull(context, "EvaluationContext must not be null");
        Assert.notNull(configuration, "SpelParserConfiguration must not be null");
        this.relatedContext = context;
        this.rootObject = rootObject;
        this.configuration = configuration;
    }

    public TypedValue getActiveContextObject() {
        return CollectionUtils.isEmpty(this.contextObjects) ? this.rootObject : (TypedValue)this.contextObjects.element();
    }

    public void pushActiveContextObject(TypedValue obj) {
        if (this.contextObjects == null) {
            this.contextObjects = new ArrayDeque();
        }

        this.contextObjects.push(obj);
    }

    public void popActiveContextObject() {
        if (this.contextObjects == null) {
            this.contextObjects = new ArrayDeque();
        }

        try {
            this.contextObjects.pop();
        } catch (NoSuchElementException var2) {
            throw new IllegalStateException("Cannot pop active context object: stack is empty");
        }
    }

    public TypedValue getRootContextObject() {
        return this.rootObject;
    }

    public TypedValue getScopeRootContextObject() {
        return CollectionUtils.isEmpty(this.scopeRootObjects) ? this.rootObject : (TypedValue)this.scopeRootObjects.element();
    }

    public void setVariable(String name, @Nullable Object value) {
        this.relatedContext.setVariable(name, value);
    }

    public TypedValue lookupVariable(String name) {
        Object value = this.relatedContext.lookupVariable(name);
        return value != null ? new TypedValue(value) : TypedValue.NULL;
    }

    public TypeComparator getTypeComparator() {
        return this.relatedContext.getTypeComparator();
    }

    public Class<?> findType(String type) throws EvaluationException {
        return this.relatedContext.getTypeLocator().findType(type);
    }

    public Object convertValue(Object value, TypeDescriptor targetTypeDescriptor) throws EvaluationException {
        Object result = this.relatedContext.getTypeConverter().convertValue(value, TypeDescriptor.forObject(value), targetTypeDescriptor);
        if (result == null) {
            throw new IllegalStateException("Null conversion result for value [" + value + "]");
        } else {
            return result;
        }
    }

    public TypeConverter getTypeConverter() {
        return this.relatedContext.getTypeConverter();
    }

    @Nullable
    public Object convertValue(TypedValue value, TypeDescriptor targetTypeDescriptor) throws EvaluationException {
        Object val = value.getValue();
        return this.relatedContext.getTypeConverter().convertValue(val, TypeDescriptor.forObject(val), targetTypeDescriptor);
    }

    public void enterScope(Map<String, Object> argMap) {
        this.initVariableScopes().push(new ExpressionState.VariableScope(argMap));
        this.initScopeRootObjects().push(this.getActiveContextObject());
    }

    public void enterScope() {
        this.initVariableScopes().push(new ExpressionState.VariableScope(Collections.emptyMap()));
        this.initScopeRootObjects().push(this.getActiveContextObject());
    }

    public void enterScope(String name, Object value) {
        this.initVariableScopes().push(new ExpressionState.VariableScope(name, value));
        this.initScopeRootObjects().push(this.getActiveContextObject());
    }

    public void exitScope() {
        this.initVariableScopes().pop();
        this.initScopeRootObjects().pop();
    }

    public void setLocalVariable(String name, Object value) {
        ((ExpressionState.VariableScope)this.initVariableScopes().element()).setVariable(name, value);
    }

    @Nullable
    public Object lookupLocalVariable(String name) {
        Iterator var2 = this.initVariableScopes().iterator();

        ExpressionState.VariableScope scope;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            scope = (ExpressionState.VariableScope)var2.next();
        } while(!scope.definesVariable(name));

        return scope.lookupVariable(name);
    }

    private Deque<ExpressionState.VariableScope> initVariableScopes() {
        if (this.variableScopes == null) {
            this.variableScopes = new ArrayDeque();
            this.variableScopes.add(new ExpressionState.VariableScope());
        }

        return this.variableScopes;
    }

    private Deque<TypedValue> initScopeRootObjects() {
        if (this.scopeRootObjects == null) {
            this.scopeRootObjects = new ArrayDeque();
        }

        return this.scopeRootObjects;
    }

    public TypedValue operate(Operation op, @Nullable Object left, @Nullable Object right) throws EvaluationException {
        OperatorOverloader overloader = this.relatedContext.getOperatorOverloader();
        if (overloader.overridesOperation(op, left, right)) {
            Object returnValue = overloader.operate(op, left, right);
            return new TypedValue(returnValue);
        } else {
            String leftType = left == null ? "null" : left.getClass().getName();
            String rightType = right == null ? "null" : right.getClass().getName();
            throw new SpelEvaluationException(SpelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES, new Object[]{op, leftType, rightType});
        }
    }

    public List<PropertyAccessor> getPropertyAccessors() {
        return this.relatedContext.getPropertyAccessors();
    }

    public EvaluationContext getEvaluationContext() {
        return this.relatedContext;
    }

    public SpelParserConfiguration getConfiguration() {
        return this.configuration;
    }

    private static class VariableScope {
        private final Map<String, Object> vars = new HashMap();

        public VariableScope() {
        }

        public VariableScope(@Nullable Map<String, Object> arguments) {
            if (arguments != null) {
                this.vars.putAll(arguments);
            }

        }

        public VariableScope(String name, Object value) {
            this.vars.put(name, value);
        }

        public Object lookupVariable(String name) {
            return this.vars.get(name);
        }

        public void setVariable(String name, Object value) {
            this.vars.put(name, value);
        }

        public boolean definesVariable(String name) {
            return this.vars.containsKey(name);
        }
    }
}
