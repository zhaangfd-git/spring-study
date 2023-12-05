//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;


import com.zhangfd.spring.asm.MethodVisitor;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.expression.spel.CodeFlow;
import com.zhangfd.spring.util.Assert;

public class IntLiteral extends Literal {
    private final TypedValue value;

    public IntLiteral(String payload, int startPos, int endPos, int value) {
        super(payload, startPos, endPos);
        this.value = new TypedValue(value);
        this.exitTypeDescriptor = "I";
    }

    public TypedValue getLiteralValue() {
        return this.value;
    }

    public boolean isCompilable() {
        return true;
    }

    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        Integer intValue = (Integer)this.value.getValue();
        Assert.state(intValue != null, "No int value");
        if (intValue == -1) {
            mv.visitInsn(2);
        } else if (intValue >= 0 && intValue < 6) {
            mv.visitInsn(3 + intValue);
        } else {
            mv.visitLdcInsn(intValue);
        }

        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
