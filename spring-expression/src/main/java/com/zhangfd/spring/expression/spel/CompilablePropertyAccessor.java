//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel;

import com.zhangfd.spring.asm.MethodVisitor;
import com.zhangfd.spring.asm.Opcodes;
import com.zhangfd.spring.expression.PropertyAccessor;


public interface CompilablePropertyAccessor extends PropertyAccessor, Opcodes {
    boolean isCompilable();

    Class<?> getPropertyType();

    void generateCode(String var1, MethodVisitor var2, CodeFlow var3);
}
