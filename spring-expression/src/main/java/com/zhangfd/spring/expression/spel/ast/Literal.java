//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;


import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.expression.spel.*;
import com.zhangfd.spring.lang.Nullable;

public abstract class Literal extends SpelNodeImpl {
    @Nullable
    private final String originalValue;

    public Literal(@Nullable String originalValue, int startPos, int endPos) {
        super(startPos, endPos, new SpelNodeImpl[0]);
        this.originalValue = originalValue;
    }

    @Nullable
    public final String getOriginalValue() {
        return this.originalValue;
    }

    public final TypedValue getValueInternal(ExpressionState state) throws SpelEvaluationException {
        return this.getLiteralValue();
    }

    public String toString() {
        return String.valueOf(this.getLiteralValue().getValue());
    }

    public String toStringAST() {
        return this.toString();
    }

    public abstract TypedValue getLiteralValue();

    public static Literal getIntLiteral(String numberToken, int startPos, int endPos, int radix) {
        try {
            int value = Integer.parseInt(numberToken, radix);
            return new IntLiteral(numberToken, startPos, endPos, value);
        } catch (NumberFormatException var5) {
            throw new InternalParseException(new SpelParseException(startPos, var5, SpelMessage.NOT_AN_INTEGER, new Object[]{numberToken}));
        }
    }

    public static Literal getLongLiteral(String numberToken, int startPos, int endPos, int radix) {
        try {
            long value = Long.parseLong(numberToken, radix);
            return new LongLiteral(numberToken, startPos, endPos, value);
        } catch (NumberFormatException var6) {
            throw new InternalParseException(new SpelParseException(startPos, var6, SpelMessage.NOT_A_LONG, new Object[]{numberToken}));
        }
    }

    public static Literal getRealLiteral(String numberToken, int startPos, int endPos, boolean isFloat) {
        try {
            if (isFloat) {
                float value = Float.parseFloat(numberToken);
                return new FloatLiteral(numberToken, startPos, endPos, value);
            } else {
                double value = Double.parseDouble(numberToken);
                return new RealLiteral(numberToken, startPos, endPos, value);
            }
        } catch (NumberFormatException var6) {
            throw new InternalParseException(new SpelParseException(startPos, var6, SpelMessage.NOT_A_REAL, new Object[]{numberToken}));
        }
    }
}
