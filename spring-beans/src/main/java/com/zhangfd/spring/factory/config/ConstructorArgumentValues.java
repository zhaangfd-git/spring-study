package com.zhangfd.spring.factory.config;

import com.zhangfd.spring.BeanMetadataElement;
import com.zhangfd.spring.Mergeable;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;
import com.zhangfd.spring.util.ClassUtils;
import com.zhangfd.spring.util.ObjectUtils;

import java.util.*;

public class ConstructorArgumentValues {

    private final Map<Integer, ValueHolder> indexedArgumentValues = new LinkedHashMap<>();

    private final List<ValueHolder> genericArgumentValues = new ArrayList<>();

    public ConstructorArgumentValues() {
    }

    /**
     * Deep copy constructor.
     * @param original the ConstructorArgumentValues to copy
     */
    public ConstructorArgumentValues(ConstructorArgumentValues original) {
        addArgumentValues(original);
    }


    public void addArgumentValues(@Nullable ConstructorArgumentValues other) {
        if (other != null) {
            other.indexedArgumentValues.forEach(
                    (index, argValue) -> addOrMergeIndexedArgumentValue(index, argValue.copy())
            );
            other.genericArgumentValues.stream()
                    .filter(valueHolder -> !this.genericArgumentValues.contains(valueHolder))
                    .forEach(valueHolder -> addOrMergeGenericArgumentValue(valueHolder.copy()));
        }
    }

    public Map<Integer, ValueHolder> getIndexedArgumentValues() {
        return Collections.unmodifiableMap(this.indexedArgumentValues);
    }

    public List<ValueHolder> getGenericArgumentValues() {
        return Collections.unmodifiableList(this.genericArgumentValues);
    }

    public int getArgumentCount() {
        return (this.indexedArgumentValues.size() + this.genericArgumentValues.size());
    }

    /**
     * Return if this holder does not contain any argument values,
     * neither indexed ones nor generic ones.
     */
    public boolean isEmpty() {
        return (this.indexedArgumentValues.isEmpty() && this.genericArgumentValues.isEmpty());
    }


    public void addIndexedArgumentValue(int index, ValueHolder newValue) {
        Assert.isTrue(index >= 0, "Index must not be negative");
        Assert.notNull(newValue, "ValueHolder must not be null");
        addOrMergeIndexedArgumentValue(index, newValue);
    }

    private void addOrMergeIndexedArgumentValue(Integer key, ValueHolder newValue) {
        ValueHolder currentValue = this.indexedArgumentValues.get(key);
        if (currentValue != null && newValue.getValue() instanceof Mergeable) {
            Mergeable mergeable = (Mergeable) newValue.getValue();
            if (mergeable.isMergeEnabled()) {
                newValue.setValue(mergeable.merge(currentValue.getValue()));
            }
        }
        this.indexedArgumentValues.put(key, newValue);
    }

    public void addGenericArgumentValue(ValueHolder newValue) {
        Assert.notNull(newValue, "ValueHolder must not be null");
        if (!this.genericArgumentValues.contains(newValue)) {
            addOrMergeGenericArgumentValue(newValue);
        }
    }

    private void addOrMergeGenericArgumentValue(ValueHolder newValue) {
        if (newValue.getName() != null) {
            for (Iterator<ValueHolder> it = this.genericArgumentValues.iterator(); it.hasNext();) {
                ValueHolder currentValue = it.next();
                if (newValue.getName().equals(currentValue.getName())) {
                    if (newValue.getValue() instanceof Mergeable) {
                        Mergeable mergeable = (Mergeable) newValue.getValue();
                        if (mergeable.isMergeEnabled()) {
                            newValue.setValue(mergeable.merge(currentValue.getValue()));
                        }
                    }
                    it.remove();
                }
            }
        }
        this.genericArgumentValues.add(newValue);
    }


    @Nullable
    public ValueHolder getArgumentValue(int index, @Nullable Class<?> requiredType, @Nullable String requiredName, @Nullable Set<ValueHolder> usedValueHolders) {
        Assert.isTrue(index >= 0, "Index must not be negative");
        ValueHolder valueHolder = getIndexedArgumentValue(index, requiredType, requiredName);
        if (valueHolder == null) {
            valueHolder = getGenericArgumentValue(requiredType, requiredName, usedValueHolders);
        }
        return valueHolder;
    }

    @Nullable
    public ValueHolder getIndexedArgumentValue(int index, @Nullable Class<?> requiredType, @Nullable String requiredName) {
        Assert.isTrue(index >= 0, "Index must not be negative");
        ValueHolder valueHolder = this.indexedArgumentValues.get(index);
        if (valueHolder != null &&
                (valueHolder.getType() == null || (requiredType != null &&
                        ClassUtils.matchesTypeName(requiredType, valueHolder.getType()))) &&
                (valueHolder.getName() == null || (requiredName != null &&
                        (requiredName.isEmpty() || requiredName.equals(valueHolder.getName()))))) {
            return valueHolder;
        }
        return null;
    }

    @Nullable
    public ValueHolder getGenericArgumentValue(@Nullable Class<?> requiredType, @Nullable String requiredName,
                                               @Nullable Set<ValueHolder> usedValueHolders) {

        for (ValueHolder valueHolder : this.genericArgumentValues) {
            if (usedValueHolders != null && usedValueHolders.contains(valueHolder)) {
                continue;
            }
            if (valueHolder.getName() != null && (requiredName == null ||
                    (!requiredName.isEmpty() && !requiredName.equals(valueHolder.getName())))) {
                continue;
            }
            if (valueHolder.getType() != null && (requiredType == null ||
                    !ClassUtils.matchesTypeName(requiredType, valueHolder.getType()))) {
                continue;
            }
            if (requiredType != null && valueHolder.getType() == null && valueHolder.getName() == null &&
                    !ClassUtils.isAssignableValue(requiredType, valueHolder.getValue())) {
                continue;
            }
            return valueHolder;
        }
        return null;
    }
    /**
     * Holder for a constructor argument value, with an optional type
     * attribute indicating the target type of the actual constructor argument.
     */
    public static class ValueHolder implements BeanMetadataElement {

        @Nullable
        private Object value;

        @Nullable
        private String type;

        @Nullable
        private String name;

        @Nullable
        private Object source;

        private boolean converted = false;

        @Nullable
        private Object convertedValue;

        /**
         * Create a new ValueHolder for the given value.
         * @param value the argument value
         */
        public ValueHolder(@Nullable Object value) {
            this.value = value;
        }

        /**
         * Create a new ValueHolder for the given value and type.
         * @param value the argument value
         * @param type the type of the constructor argument
         */
        public ValueHolder(@Nullable Object value, @Nullable String type) {
            this.value = value;
            this.type = type;
        }

        /**
         * Create a new ValueHolder for the given value, type and name.
         * @param value the argument value
         * @param type the type of the constructor argument
         * @param name the name of the constructor argument
         */
        public ValueHolder(@Nullable Object value, @Nullable String type, @Nullable String name) {
            this.value = value;
            this.type = type;
            this.name = name;
        }

        /**
         * Set the value for the constructor argument.
         */
        public void setValue(@Nullable Object value) {
            this.value = value;
        }

        /**
         * Return the value for the constructor argument.
         */
        @Nullable
        public Object getValue() {
            return this.value;
        }

        /**
         * Set the type of the constructor argument.
         */
        public void setType(@Nullable String type) {
            this.type = type;
        }

        /**
         * Return the type of the constructor argument.
         */
        @Nullable
        public String getType() {
            return this.type;
        }

        /**
         * Set the name of the constructor argument.
         */
        public void setName(@Nullable String name) {
            this.name = name;
        }

        /**
         * Return the name of the constructor argument.
         */
        @Nullable
        public String getName() {
            return this.name;
        }

        /**
         * Set the configuration source {@code Object} for this metadata element.
         * <p>The exact type of the object will depend on the configuration mechanism used.
         */
        public void setSource(@Nullable Object source) {
            this.source = source;
        }

        @Override
        @Nullable
        public Object getSource() {
            return this.source;
        }

        /**
         * Return whether this holder contains a converted value already ({@code true}),
         * or whether the value still needs to be converted ({@code false}).
         */
        public synchronized boolean isConverted() {
            return this.converted;
        }

        /**
         * Set the converted value of the constructor argument,
         * after processed type conversion.
         */
        public synchronized void setConvertedValue(@Nullable Object value) {
            this.converted = (value != null);
            this.convertedValue = value;
        }

        /**
         * Return the converted value of the constructor argument,
         * after processed type conversion.
         */
        @Nullable
        public synchronized Object getConvertedValue() {
            return this.convertedValue;
        }

        /**
         * Determine whether the content of this ValueHolder is equal
         * to the content of the given other ValueHolder.
         * <p>Note that ValueHolder does not implement {@code equals}
         * directly, to allow for multiple ValueHolder instances with the
         * same content to reside in the same Set.
         */
        private boolean contentEquals(ValueHolder other) {
            return (this == other ||
                    (ObjectUtils.nullSafeEquals(this.value, other.value) && ObjectUtils.nullSafeEquals(this.type, other.type)));
        }

        /**
         * Determine whether the hash code of the content of this ValueHolder.
         * <p>Note that ValueHolder does not implement {@code hashCode}
         * directly, to allow for multiple ValueHolder instances with the
         * same content to reside in the same Set.
         */
        private int contentHashCode() {
            return ObjectUtils.nullSafeHashCode(this.value) * 29 + ObjectUtils.nullSafeHashCode(this.type);
        }

        /**
         * Create a copy of this ValueHolder: that is, an independent
         * ValueHolder instance with the same contents.
         */
        public ValueHolder copy() {
            ValueHolder copy = new ValueHolder(this.value, this.type, this.name);
            copy.setSource(this.source);
            return copy;
        }
    }

}


