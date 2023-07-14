package com.zhangfd.spring;

import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;
import com.zhangfd.spring.util.ObjectUtils;

public class PropertyValue {

    private final String name;

    @Nullable
    private final Object value;

    private boolean optional = false;

    private boolean converted = false;

    @Nullable
    private Object convertedValue;

    /** Package-visible field that indicates whether conversion is necessary. */
    @Nullable
    volatile Boolean conversionNecessary;

    /** Package-visible field for caching the resolved property path tokens. */
    @Nullable
    transient volatile Object resolvedTokens;


    /**
     * Create a new PropertyValue instance.
     * @param name the name of the property (never {@code null})
     * @param value the value of the property (possibly before type conversion)
     */
    public PropertyValue(String name, @Nullable Object value) {
        Assert.notNull(name, "Name must not be null");
        this.name = name;
        this.value = value;
    }




    /**
     * Return the name of the property.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the value of the property.
     * <p>Note that type conversion will <i>not</i> have occurred here.
     * It is the responsibility of the BeanWrapper implementation to
     * perform type conversion.
     */
    @Nullable
    public Object getValue() {
        return this.value;
    }



    /**
     * Set whether this is an optional value, that is, to be ignored
     * when no corresponding property exists on the target class.
     * @since 3.0
     */
    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    /**
     * Return whether this is an optional value, that is, to be ignored
     * when no corresponding property exists on the target class.
     * @since 3.0
     */
    public boolean isOptional() {
        return this.optional;
    }

    /**
     * Return whether this holder contains a converted value already ({@code true}),
     * or whether the value still needs to be converted ({@code false}).
     */
    public synchronized boolean isConverted() {
        return this.converted;
    }

    /**
     * Set the converted value of this property value,
     * after processed type conversion.
     */
    public synchronized void setConvertedValue(@Nullable Object value) {
        this.converted = true;
        this.convertedValue = value;
    }

    /**
     * Return the converted value of this property value,
     * after processed type conversion.
     */
    @Nullable
    public synchronized Object getConvertedValue() {
        return this.convertedValue;
    }


    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PropertyValue)) {
            return false;
        }
        PropertyValue otherPv = (PropertyValue) other;
        return (this.name.equals(otherPv.name) &&
                ObjectUtils.nullSafeEquals(this.value, otherPv.value) );/*&&
                ObjectUtils.nullSafeEquals(getSource(), otherPv.getSource()));*/
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
    }

    @Override
    public String toString() {
        return "bean property '" + this.name + "'";
    }
}
