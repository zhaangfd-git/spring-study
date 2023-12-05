//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;


import com.zhangfd.spring.asm.MethodVisitor;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.expression.spel.CodeFlow;

public class NullLiteral extends Literal {
    public NullLiteral(int startPos, int endPos) {
        super((String)null, startPos, endPos);
        this.exitTypeDescriptor = "Ljava/lang/Object";
    }

    public TypedValue getLiteralValue() {
        return TypedValue.NULL;
    }

    public String toString() {
        return "null";
    }

    public boolean isCompilable() {
        return true;
    }

    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        mv.visitInsn(1);
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
