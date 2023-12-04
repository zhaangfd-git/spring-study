//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.support;


import com.zhangfd.spring.core.convert.ConversionException;
import com.zhangfd.spring.core.convert.ConversionService;
import com.zhangfd.spring.core.convert.DefaultConversionService;
import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.expression.TypeConverter;
import com.zhangfd.spring.expression.spel.SpelMessage;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;

public class StandardTypeConverter implements TypeConverter {
    private final ConversionService conversionService;

    public StandardTypeConverter() {
        this.conversionService = DefaultConversionService.getSharedInstance();
    }

    public StandardTypeConverter(ConversionService conversionService) {
        Assert.notNull(conversionService, "ConversionService must not be null");
        this.conversionService = conversionService;
    }

    public boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
        return this.conversionService.canConvert(sourceType, targetType);
    }

    @Nullable
    public Object convertValue(@Nullable Object value, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
        try {
            return this.conversionService.convert(value, sourceType, targetType);
        } catch (ConversionException var5) {
            throw new SpelEvaluationException(var5, SpelMessage.TYPE_CONVERSION_ERROR, new Object[]{sourceType != null ? sourceType.toString() : (value != null ? value.getClass().getName() : "null"), targetType.toString()});
        }
    }
}
