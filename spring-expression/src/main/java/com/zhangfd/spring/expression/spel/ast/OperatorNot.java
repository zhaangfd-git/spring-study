//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;


import com.zhangfd.spring.asm.Label;
import com.zhangfd.spring.asm.MethodVisitor;
import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.spel.CodeFlow;
import com.zhangfd.spring.expression.spel.ExpressionState;
import com.zhangfd.spring.expression.spel.SpelEvaluationException;
import com.zhangfd.spring.expression.spel.SpelMessage;
import com.zhangfd.spring.expression.spel.support.BooleanTypedValue;

public class OperatorNot extends SpelNodeImpl {
    public OperatorNot(int startPos, int endPos, SpelNodeImpl operand) {
        super(startPos, endPos, new SpelNodeImpl[]{operand});
        this.exitTypeDescriptor = "Z";
    }

    public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        try {
            Boolean value = (Boolean)this.children[0].getValue(state, Boolean.class);
            if (value == null) {
                throw new SpelEvaluationException(SpelMessage.TYPE_CONVERSION_ERROR, new Object[]{"null", "boolean"});
            } else {
                return BooleanTypedValue.forValue(!value);
            }
        } catch (SpelEvaluationException var3) {
            var3.setPosition(this.getChild(0).getStartPosition());
            throw var3;
        }
    }

    public String toStringAST() {
        return "!" + this.getChild(0).toStringAST();
    }

    public boolean isCompilable() {
        SpelNodeImpl child = this.children[0];
        return child.isCompilable() && CodeFlow.isBooleanCompatible(child.exitTypeDescriptor);
    }

    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        this.children[0].generateCode(mv, cf);
        cf.unboxBooleanIfNecessary(mv);
        Label elseTarget = new Label();
        Label endOfIf = new Label();
        mv.visitJumpInsn(154, elseTarget);
        mv.visitInsn(4);
        mv.visitJumpInsn(167, endOfIf);
        mv.visitLabel(elseTarget);
        mv.visitInsn(3);
        mv.visitLabel(endOfIf);
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
