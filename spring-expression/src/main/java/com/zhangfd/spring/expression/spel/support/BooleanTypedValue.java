//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.support;


import com.zhangfd.spring.expression.TypedValue;

public final class BooleanTypedValue extends TypedValue {
    public static final BooleanTypedValue TRUE = new BooleanTypedValue(true);
    public static final BooleanTypedValue FALSE = new BooleanTypedValue(false);

    private BooleanTypedValue(boolean b) {
        super(b);
    }

    public static BooleanTypedValue forValue(boolean b) {
        return b ? TRUE : FALSE;
    }
}
