//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression;

import java.util.List;

import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.lang.Nullable;
;

public interface MethodResolver {
    @Nullable
    MethodExecutor resolve(EvaluationContext var1, Object var2, String var3, List<TypeDescriptor> var4) throws AccessException;
}
