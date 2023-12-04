//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression;


import com.zhangfd.spring.lang.Nullable;

public interface TypeComparator {
    boolean canCompare(@Nullable Object var1, @Nullable Object var2);

    int compare(@Nullable Object var1, @Nullable Object var2) throws EvaluationException;
}
