package com.zhangfd.spring;

import com.zhangfd.spring.core.MethodParameter;
import com.zhangfd.spring.core.convert.ConversionService;
import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.exception.InvalidPropertyException;
import com.zhangfd.spring.lang.Nullable;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

public class BeanWrapperImpl  implements  BeanWrapper{

    private int autoGrowCollectionLimit = Integer.MAX_VALUE;

    @Nullable
    Object wrappedObject; //被包装的实例

    @Nullable
    Object rootObject; //没有初始化的实例

   /* @Nullable
    private CachedIntrospectionResults cachedIntrospectionResults;
*/

    public void setBeanInstance(Object object) {
        this.wrappedObject = object;
        this.rootObject = object;
      //  this.typeConverterDelegate = new TypeConverterDelegate(this, this.wrappedObject);
        //setIntrospectionClass(object.getClass());
    }


   /* protected void setIntrospectionClass(Class<?> clazz) {
        if (this.cachedIntrospectionResults != null && this.cachedIntrospectionResults.getBeanClass() != clazz) {
            this.cachedIntrospectionResults = null;
        }
    }*/

    @Override
    public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit) {

    }

    @Override
    public int getAutoGrowCollectionLimit() {
        return 0;
    }

    @Override
    public Object getWrappedInstance() {
        return null;
    }

    @Override
    public Class<?> getWrappedClass() {
        return null;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return new PropertyDescriptor[0];
    }

    @Override
    public PropertyDescriptor getPropertyDescriptor(String propertyName) throws InvalidPropertyException {
        return null;
    }

    @Override
    public void setConversionService(ConversionService conversionService) {

    }

    @Override
    public ConversionService getConversionService() {
        return null;
    }

    @Override
    public void setExtractOldValueForEditor(boolean extractOldValueForEditor) {

    }

    @Override
    public boolean isExtractOldValueForEditor() {
        return false;
    }

    @Override
    public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {

    }

    @Override
    public boolean isAutoGrowNestedPaths() {
        return false;
    }

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType) {
        return null;
    }

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam) {
        return null;
    }

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType, Field field) {
        return null;
    }

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType, TypeDescriptor typeDescriptor) {
        return null;
    }
}
