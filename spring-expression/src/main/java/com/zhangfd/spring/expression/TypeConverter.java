//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression;

import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.lang.Nullable;


public interface TypeConverter {
    boolean canConvert(@Nullable TypeDescriptor var1, TypeDescriptor var2);

    @Nullable
    Object convertValue(@Nullable Object var1, @Nullable TypeDescriptor var2, TypeDescriptor var3);
}
