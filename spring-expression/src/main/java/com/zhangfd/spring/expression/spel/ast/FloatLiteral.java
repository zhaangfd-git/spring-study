//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;


import com.zhangfd.spring.asm.MethodVisitor;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.expression.spel.CodeFlow;

public class FloatLiteral extends Literal {
    private final TypedValue value;

    public FloatLiteral(String payload, int startPos, int endPos, float value) {
        super(payload, startPos, endPos);
        this.value = new TypedValue(value);
        this.exitTypeDescriptor = "F";
    }

    public TypedValue getLiteralValue() {
        return this.value;
    }

    public boolean isCompilable() {
        return true;
    }

    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        mv.visitLdcInsn(this.value.getValue());
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
