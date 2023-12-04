//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression;

import java.util.List;

import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.lang.Nullable;

@FunctionalInterface
public interface ConstructorResolver {
    @Nullable
    ConstructorExecutor resolve(EvaluationContext var1, String var2, List<TypeDescriptor> var3) throws AccessException;
}
