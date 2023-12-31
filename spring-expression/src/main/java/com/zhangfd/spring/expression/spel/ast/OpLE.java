//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;

import com.zhangfd.spring.asm.MethodVisitor;
import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.spel.CodeFlow;
import com.zhangfd.spring.expression.spel.ExpressionState;
import com.zhangfd.spring.expression.spel.support.BooleanTypedValue;
import com.zhangfd.spring.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;


public class OpLE extends Operator {
    public OpLE(int startPos, int endPos, SpelNodeImpl... operands) {
        super("<=", startPos, endPos, operands);
        this.exitTypeDescriptor = "Z";
    }

    public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        Object left = this.getLeftOperand().getValueInternal(state).getValue();
        Object right = this.getRightOperand().getValueInternal(state).getValue();
        this.leftActualDescriptor = CodeFlow.toDescriptorFromObject(left);
        this.rightActualDescriptor = CodeFlow.toDescriptorFromObject(right);
        if (left instanceof Number && right instanceof Number) {
            Number leftNumber = (Number)left;
            Number rightNumber = (Number)right;
            if (!(leftNumber instanceof BigDecimal) && !(rightNumber instanceof BigDecimal)) {
                if (!(leftNumber instanceof Double) && !(rightNumber instanceof Double)) {
                    if (!(leftNumber instanceof Float) && !(rightNumber instanceof Float)) {
                        if (!(leftNumber instanceof BigInteger) && !(rightNumber instanceof BigInteger)) {
                            if (!(leftNumber instanceof Long) && !(rightNumber instanceof Long)) {
                                if (!(leftNumber instanceof Integer) && !(rightNumber instanceof Integer)) {
                                    if (!(leftNumber instanceof Short) && !(rightNumber instanceof Short)) {
                                        return !(leftNumber instanceof Byte) && !(rightNumber instanceof Byte) ? BooleanTypedValue.forValue(leftNumber.doubleValue() <= rightNumber.doubleValue()) : BooleanTypedValue.forValue(leftNumber.byteValue() <= rightNumber.byteValue());
                                    } else {
                                        return BooleanTypedValue.forValue(leftNumber.shortValue() <= rightNumber.shortValue());
                                    }
                                } else {
                                    return BooleanTypedValue.forValue(leftNumber.intValue() <= rightNumber.intValue());
                                }
                            } else {
                                return BooleanTypedValue.forValue(leftNumber.longValue() <= rightNumber.longValue());
                            }
                        } else {
                            BigInteger leftBigInteger = (BigInteger) NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
                            BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
                            return BooleanTypedValue.forValue(leftBigInteger.compareTo(rightBigInteger) <= 0);
                        }
                    } else {
                        return BooleanTypedValue.forValue(leftNumber.floatValue() <= rightNumber.floatValue());
                    }
                } else {
                    return BooleanTypedValue.forValue(leftNumber.doubleValue() <= rightNumber.doubleValue());
                }
            } else {
                BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
                BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
                return BooleanTypedValue.forValue(leftBigDecimal.compareTo(rightBigDecimal) <= 0);
            }
        } else {
            return BooleanTypedValue.forValue(state.getTypeComparator().compare(left, right) <= 0);
        }
    }

    public boolean isCompilable() {
        return this.isCompilableOperatorUsingNumerics();
    }

    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        this.generateComparisonCode(mv, cf, 157, 163);
    }
}
