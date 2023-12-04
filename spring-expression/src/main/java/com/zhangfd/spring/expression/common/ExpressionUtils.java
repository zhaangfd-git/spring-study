//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.common;

import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.expression.EvaluationContext;
import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.TypeConverter;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.ClassUtils;


public abstract class ExpressionUtils {
    public ExpressionUtils() {
    }

    @Nullable
    public static <T> T convertTypedValue(@Nullable EvaluationContext context, TypedValue typedValue, @Nullable Class<T> targetType) {
        Object value = typedValue.getValue();
        if (targetType == null) {
            return (T) value;
        } else if (context != null) {
            return (T) context.getTypeConverter().convertValue(value, typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(targetType));
        } else if (ClassUtils.isAssignableValue(targetType, value)) {
            return (T) value;
        } else {
            throw new EvaluationException("Cannot convert value '" + value + "' to type '" + targetType.getName() + "'");
        }
    }

    public static int toInt(TypeConverter typeConverter, TypedValue typedValue) {
        return (Integer)convertValue(typeConverter, typedValue, Integer.class);
    }

    public static boolean toBoolean(TypeConverter typeConverter, TypedValue typedValue) {
        return (Boolean)convertValue(typeConverter, typedValue, Boolean.class);
    }

    public static double toDouble(TypeConverter typeConverter, TypedValue typedValue) {
        return (Double)convertValue(typeConverter, typedValue, Double.class);
    }

    public static long toLong(TypeConverter typeConverter, TypedValue typedValue) {
        return (Long)convertValue(typeConverter, typedValue, Long.class);
    }

    public static char toChar(TypeConverter typeConverter, TypedValue typedValue) {
        return (Character)convertValue(typeConverter, typedValue, Character.class);
    }

    public static short toShort(TypeConverter typeConverter, TypedValue typedValue) {
        return (Short)convertValue(typeConverter, typedValue, Short.class);
    }

    public static float toFloat(TypeConverter typeConverter, TypedValue typedValue) {
        return (Float)convertValue(typeConverter, typedValue, Float.class);
    }

    public static byte toByte(TypeConverter typeConverter, TypedValue typedValue) {
        return (Byte)convertValue(typeConverter, typedValue, Byte.class);
    }

    private static <T> T convertValue(TypeConverter typeConverter, TypedValue typedValue, Class<T> targetType) {
        Object result = typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(targetType));
        if (result == null) {
            throw new IllegalStateException("Null conversion result for value [" + typedValue.getValue() + "]");
        } else {
            return (T) result;
        }
    }
}
