//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;


import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.expression.spel.SpelEvaluationException;
import com.zhangfd.spring.expression.spel.SpelMessage;
import com.zhangfd.spring.lang.Nullable;

public interface ValueRef {
    TypedValue getValue();

    void setValue(@Nullable Object var1);

    boolean isWritable();

    public static class TypedValueHolderValueRef implements ValueRef {
        private final TypedValue typedValue;
        private final SpelNodeImpl node;

        public TypedValueHolderValueRef(TypedValue typedValue, SpelNodeImpl node) {
            this.typedValue = typedValue;
            this.node = node;
        }

        public TypedValue getValue() {
            return this.typedValue;
        }

        public void setValue(@Nullable Object newValue) {
            throw new SpelEvaluationException(this.node.getStartPosition(), SpelMessage.NOT_ASSIGNABLE, new Object[]{this.node.toStringAST()});
        }

        public boolean isWritable() {
            return false;
        }
    }

    public static class NullValueRef implements ValueRef {
        static final ValueRef.NullValueRef INSTANCE = new ValueRef.NullValueRef();

        public NullValueRef() {
        }

        public TypedValue getValue() {
            return TypedValue.NULL;
        }

        public void setValue(@Nullable Object newValue) {
            throw new SpelEvaluationException(0, SpelMessage.NOT_ASSIGNABLE, new Object[]{"null"});
        }

        public boolean isWritable() {
            return false;
        }
    }
}
