//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression;


import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.ObjectUtils;

public class TypedValue {
    public static final TypedValue NULL = new TypedValue((Object)null);
    @Nullable
    private final Object value;
    @Nullable
    private TypeDescriptor typeDescriptor;

    public TypedValue(@Nullable Object value) {
        this.value = value;
        this.typeDescriptor = null;
    }

    public TypedValue(@Nullable Object value, @Nullable TypeDescriptor typeDescriptor) {
        this.value = value;
        this.typeDescriptor = typeDescriptor;
    }

    @Nullable
    public Object getValue() {
        return this.value;
    }

    @Nullable
    public TypeDescriptor getTypeDescriptor() {
        if (this.typeDescriptor == null && this.value != null) {
            this.typeDescriptor = TypeDescriptor.forObject(this.value);
        }

        return this.typeDescriptor;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof TypedValue)) {
            return false;
        } else {
            TypedValue otherTv = (TypedValue)other;
            return ObjectUtils.nullSafeEquals(this.value, otherTv.value) && (this.typeDescriptor == null && otherTv.typeDescriptor == null || ObjectUtils.nullSafeEquals(this.getTypeDescriptor(), otherTv.getTypeDescriptor()));
        }
    }

    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.value);
    }

    public String toString() {
        return "TypedValue: '" + this.value + "' of [" + this.getTypeDescriptor() + "]";
    }
}
