//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;

import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.spel.ExpressionState;
import com.zhangfd.spring.expression.spel.SpelEvaluationException;
import com.zhangfd.spring.expression.spel.SpelMessage;
import com.zhangfd.spring.expression.spel.support.BooleanTypedValue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class OperatorMatches extends Operator {
    private static final int PATTERN_ACCESS_THRESHOLD = 1000000;
    private final ConcurrentMap<String, Pattern> patternCache = new ConcurrentHashMap();

    public OperatorMatches(int startPos, int endPos, SpelNodeImpl... operands) {
        super("matches", startPos, endPos, operands);
    }

    public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        SpelNodeImpl leftOp = this.getLeftOperand();
        SpelNodeImpl rightOp = this.getRightOperand();
        String left = (String)leftOp.getValue(state, String.class);
        Object right = this.getRightOperand().getValue(state);
        if (left == null) {
            throw new SpelEvaluationException(leftOp.getStartPosition(), SpelMessage.INVALID_FIRST_OPERAND_FOR_MATCHES_OPERATOR, new Object[]{null});
        } else if (!(right instanceof String)) {
            throw new SpelEvaluationException(rightOp.getStartPosition(), SpelMessage.INVALID_SECOND_OPERAND_FOR_MATCHES_OPERATOR, new Object[]{right});
        } else {
            try {
                String rightString = (String)right;
                Pattern pattern = (Pattern)this.patternCache.get(rightString);
                if (pattern == null) {
                    pattern = Pattern.compile(rightString);
                    this.patternCache.putIfAbsent(rightString, pattern);
                }

                Matcher matcher = pattern.matcher(new OperatorMatches.MatcherInput(left, new OperatorMatches.AccessCount()));
                return BooleanTypedValue.forValue(matcher.matches());
            } catch (PatternSyntaxException var9) {
                throw new SpelEvaluationException(rightOp.getStartPosition(), var9, SpelMessage.INVALID_PATTERN, new Object[]{right});
            } catch (IllegalStateException var10) {
                throw new SpelEvaluationException(rightOp.getStartPosition(), var10, SpelMessage.FLAWED_PATTERN, new Object[]{right});
            }
        }
    }

    private static class MatcherInput implements CharSequence {
        private final CharSequence value;
        private OperatorMatches.AccessCount access;

        public MatcherInput(CharSequence value, OperatorMatches.AccessCount access) {
            this.value = value;
            this.access = access;
        }

        public char charAt(int index) {
            this.access.check();
            return this.value.charAt(index);
        }

        public CharSequence subSequence(int start, int end) {
            return new OperatorMatches.MatcherInput(this.value.subSequence(start, end), this.access);
        }

        public int length() {
            return this.value.length();
        }

        public String toString() {
            return this.value.toString();
        }
    }

    private static class AccessCount {
        private int count;

        private AccessCount() {
        }

        public void check() throws IllegalStateException {
            if (this.count++ > 1000000) {
                throw new IllegalStateException("Pattern access threshold exceeded");
            }
        }
    }
}
