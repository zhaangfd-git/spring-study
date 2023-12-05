//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;


import com.zhangfd.spring.asm.MethodVisitor;
import com.zhangfd.spring.asm.Type;
import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.expression.spel.CodeFlow;
import com.zhangfd.spring.expression.spel.ExpressionState;
import com.zhangfd.spring.expression.spel.SpelEvaluationException;
import com.zhangfd.spring.expression.spel.SpelMessage;
import com.zhangfd.spring.expression.spel.support.BooleanTypedValue;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;

public class OperatorInstanceof extends Operator {
    @Nullable
    private Class<?> type;

    public OperatorInstanceof(int startPos, int endPos, SpelNodeImpl... operands) {
        super("instanceof", startPos, endPos, operands);
    }

    @Override
    public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        SpelNodeImpl rightOperand = getRightOperand();
        TypedValue left = getLeftOperand().getValueInternal(state);
        TypedValue right = rightOperand.getValueInternal(state);
        Object leftValue = left.getValue();
        Object rightValue = right.getValue();
        BooleanTypedValue result;
        if (rightValue == null || !(rightValue instanceof Class)) {
            throw new SpelEvaluationException(getRightOperand().getStartPosition(),
                    SpelMessage.INSTANCEOF_OPERATOR_NEEDS_CLASS_OPERAND,
                    (rightValue == null ? "null" : rightValue.getClass().getName()));
        }
        Class<?> rightClass = (Class<?>) rightValue;
        if (leftValue == null) {
            result = BooleanTypedValue.FALSE;  // null is not an instanceof anything
        }
        else {
            result = BooleanTypedValue.forValue(rightClass.isAssignableFrom(leftValue.getClass()));
        }
        this.type = rightClass;
        if (rightOperand instanceof TypeReference) {
            // Can only generate bytecode where the right operand is a direct type reference,
            // not if it is indirect (for example when right operand is a variable reference)
            this.exitTypeDescriptor = "Z";
        }
        return result;
    }

    public boolean isCompilable() {
        return this.exitTypeDescriptor != null && this.getLeftOperand().isCompilable();
    }

    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        this.getLeftOperand().generateCode(mv, cf);
        CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor());
        Assert.state(this.type != null, "No type available");
        if (this.type.isPrimitive()) {
            mv.visitInsn(87);
            mv.visitInsn(3);
        } else {
            mv.visitTypeInsn(193, Type.getInternalName(this.type));
        }

        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
