//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;


import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.expression.spel.ExpressionState;

public class Assign extends SpelNodeImpl {
    public Assign(int startPos, int endPos, SpelNodeImpl... operands) {
        super(startPos, endPos, operands);
    }

    public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        TypedValue newValue = this.children[1].getValueInternal(state);
        this.getChild(0).setValue(state, newValue.getValue());
        return newValue;
    }

    public String toStringAST() {
        return this.getChild(0).toStringAST() + "=" + this.getChild(1).toStringAST();
    }
}
