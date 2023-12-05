//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;


import com.zhangfd.spring.asm.Label;
import com.zhangfd.spring.asm.MethodVisitor;
import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.expression.spel.CodeFlow;
import com.zhangfd.spring.expression.spel.ExpressionState;
import com.zhangfd.spring.expression.spel.SpelEvaluationException;
import com.zhangfd.spring.expression.spel.SpelMessage;
import com.zhangfd.spring.expression.spel.support.BooleanTypedValue;
import com.zhangfd.spring.lang.Nullable;

public class OpAnd extends Operator {
    public OpAnd(int startPos, int endPos, SpelNodeImpl... operands) {
        super("and", startPos, endPos, operands);
        this.exitTypeDescriptor = "Z";
    }

    public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        return !this.getBooleanValue(state, this.getLeftOperand()) ? BooleanTypedValue.FALSE : BooleanTypedValue.forValue(this.getBooleanValue(state, this.getRightOperand()));
    }

    private boolean getBooleanValue(ExpressionState state, SpelNodeImpl operand) {
        try {
            Boolean value = (Boolean)operand.getValue(state, Boolean.class);
            this.assertValueNotNull(value);
            return value;
        } catch (SpelEvaluationException var4) {
            var4.setPosition(operand.getStartPosition());
            throw var4;
        }
    }

    private void assertValueNotNull(@Nullable Boolean value) {
        if (value == null) {
            throw new SpelEvaluationException(SpelMessage.TYPE_CONVERSION_ERROR, new Object[]{"null", "boolean"});
        }
    }

    public boolean isCompilable() {
        SpelNodeImpl left = this.getLeftOperand();
        SpelNodeImpl right = this.getRightOperand();
        return left.isCompilable() && right.isCompilable() && CodeFlow.isBooleanCompatible(left.exitTypeDescriptor) && CodeFlow.isBooleanCompatible(right.exitTypeDescriptor);
    }

    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        Label elseTarget = new Label();
        Label endOfIf = new Label();
        cf.enterCompilationScope();
        this.getLeftOperand().generateCode(mv, cf);
        cf.unboxBooleanIfNecessary(mv);
        cf.exitCompilationScope();
        mv.visitJumpInsn(154, elseTarget);
        mv.visitLdcInsn(0);
        mv.visitJumpInsn(167, endOfIf);
        mv.visitLabel(elseTarget);
        cf.enterCompilationScope();
        this.getRightOperand().generateCode(mv, cf);
        cf.unboxBooleanIfNecessary(mv);
        cf.exitCompilationScope();
        mv.visitLabel(endOfIf);
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
