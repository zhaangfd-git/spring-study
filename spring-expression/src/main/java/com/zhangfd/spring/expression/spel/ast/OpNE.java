//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;


import com.zhangfd.spring.asm.Label;
import com.zhangfd.spring.asm.MethodVisitor;
import com.zhangfd.spring.expression.EvaluationContext;
import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.spel.CodeFlow;
import com.zhangfd.spring.expression.spel.ExpressionState;
import com.zhangfd.spring.expression.spel.support.BooleanTypedValue;

public class OpNE extends Operator {
    public OpNE(int startPos, int endPos, SpelNodeImpl... operands) {
        super("!=", startPos, endPos, operands);
        this.exitTypeDescriptor = "Z";
    }

    public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        Object leftValue = this.getLeftOperand().getValueInternal(state).getValue();
        Object rightValue = this.getRightOperand().getValueInternal(state).getValue();
        this.leftActualDescriptor = CodeFlow.toDescriptorFromObject(leftValue);
        this.rightActualDescriptor = CodeFlow.toDescriptorFromObject(rightValue);
        return BooleanTypedValue.forValue(!equalityCheck(state.getEvaluationContext(), leftValue, rightValue));
    }

    public boolean isCompilable() {
        SpelNodeImpl left = this.getLeftOperand();
        SpelNodeImpl right = this.getRightOperand();
        if (left.isCompilable() && right.isCompilable()) {
            String leftDesc = left.exitTypeDescriptor;
            String rightDesc = right.exitTypeDescriptor;
            DescriptorComparison dc = DescriptorComparison.checkNumericCompatibility(leftDesc, rightDesc, this.leftActualDescriptor, this.rightActualDescriptor);
            return !dc.areNumbers || dc.areCompatible;
        } else {
            return false;
        }
    }

    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        cf.loadEvaluationContext(mv);
        String leftDesc = this.getLeftOperand().exitTypeDescriptor;
        String rightDesc = this.getRightOperand().exitTypeDescriptor;
        boolean leftPrim = CodeFlow.isPrimitive(leftDesc);
        boolean rightPrim = CodeFlow.isPrimitive(rightDesc);
        cf.enterCompilationScope();
        this.getLeftOperand().generateCode(mv, cf);
        cf.exitCompilationScope();
        if (leftPrim) {
            CodeFlow.insertBoxIfNecessary(mv, leftDesc.charAt(0));
        }

        cf.enterCompilationScope();
        this.getRightOperand().generateCode(mv, cf);
        cf.exitCompilationScope();
        if (rightPrim) {
            CodeFlow.insertBoxIfNecessary(mv, rightDesc.charAt(0));
        }

        String operatorClassName = Operator.class.getName().replace('.', '/');
        String evaluationContextClassName = EvaluationContext.class.getName().replace('.', '/');
        mv.visitMethodInsn(184, operatorClassName, "equalityCheck", "(L" + evaluationContextClassName + ";Ljava/lang/Object;Ljava/lang/Object;)Z", false);
        Label notZero = new Label();
        Label end = new Label();
        mv.visitJumpInsn(154, notZero);
        mv.visitInsn(4);
        mv.visitJumpInsn(167, end);
        mv.visitLabel(notZero);
        mv.visitInsn(3);
        mv.visitLabel(end);
        cf.pushDescriptor("Z");
    }
}
