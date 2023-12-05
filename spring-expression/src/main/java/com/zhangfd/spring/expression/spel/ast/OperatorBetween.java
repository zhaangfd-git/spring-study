//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;

import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.TypeComparator;
import com.zhangfd.spring.expression.spel.ExpressionState;
import com.zhangfd.spring.expression.spel.SpelEvaluationException;
import com.zhangfd.spring.expression.spel.SpelMessage;
import com.zhangfd.spring.expression.spel.support.BooleanTypedValue;

import java.util.List;


public class OperatorBetween extends Operator {
    public OperatorBetween(int startPos, int endPos, SpelNodeImpl... operands) {
        super("between", startPos, endPos, operands);
    }

    public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        Object left = this.getLeftOperand().getValueInternal(state).getValue();
        Object right = this.getRightOperand().getValueInternal(state).getValue();
        if (right instanceof List && ((List)right).size() == 2) {
            List<?> list = (List)right;
            Object low = list.get(0);
            Object high = list.get(1);
            TypeComparator comp = state.getTypeComparator();

            try {
                return BooleanTypedValue.forValue(comp.compare(left, low) >= 0 && comp.compare(left, high) <= 0);
            } catch (SpelEvaluationException var9) {
                var9.setPosition(this.getStartPosition());
                throw var9;
            }
        } else {
            throw new SpelEvaluationException(this.getRightOperand().getStartPosition(), SpelMessage.BETWEEN_RIGHT_OPERAND_MUST_BE_TWO_ELEMENT_LIST, new Object[0]);
        }
    }
}
