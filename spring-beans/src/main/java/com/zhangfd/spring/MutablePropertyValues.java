package com.zhangfd.spring;

import java.io.Serializable;

public class MutablePropertyValues  implements PropertyValues, Serializable  {
    @Override
    public PropertyValue[] getPropertyValues() {
        return new PropertyValue[0];
    }

    @Override
    public PropertyValue getPropertyValue(String propertyName) {
        return null;
    }

    @Override
    public PropertyValues changesSince(PropertyValues old) {
        return null;
    }

    @Override
    public boolean contains(String propertyName) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
