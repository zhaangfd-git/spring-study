//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.support;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.zhangfd.spring.asm.MethodVisitor;
import com.zhangfd.spring.core.MethodParameter;
import com.zhangfd.spring.core.convert.Property;
import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.expression.*;
import com.zhangfd.spring.expression.spel.CodeFlow;
import com.zhangfd.spring.expression.spel.CompilablePropertyAccessor;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;
import com.zhangfd.spring.util.ClassUtils;
import com.zhangfd.spring.util.ReflectionUtils;
import com.zhangfd.spring.util.StringUtils;


public class ReflectivePropertyAccessor implements PropertyAccessor {
    private static final Set<Class<?>> ANY_TYPES = Collections.emptySet();
    private static final Set<Class<?>> BOOLEAN_TYPES;
    private final boolean allowWrite;
    private final Map<ReflectivePropertyAccessor.PropertyCacheKey, ReflectivePropertyAccessor.InvokerPair> readerCache = new ConcurrentHashMap(64);
    private final Map<ReflectivePropertyAccessor.PropertyCacheKey, Member> writerCache = new ConcurrentHashMap(64);
    private final Map<ReflectivePropertyAccessor.PropertyCacheKey, TypeDescriptor> typeDescriptorCache = new ConcurrentHashMap(64);
    private final Map<Class<?>, Method[]> sortedMethodsCache = new ConcurrentHashMap(64);
    @Nullable
    private volatile ReflectivePropertyAccessor.InvokerPair lastReadInvokerPair;

    public ReflectivePropertyAccessor() {
        this.allowWrite = true;
    }

    public ReflectivePropertyAccessor(boolean allowWrite) {
        this.allowWrite = allowWrite;
    }

    @Nullable
    public Class<?>[] getSpecificTargetClasses() {
        return null;
    }

    public boolean canRead(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
        if (target == null) {
            return false;
        } else {
            Class<?> type = target instanceof Class ? (Class)target : target.getClass();
            if (type.isArray() && name.equals("length")) {
                return true;
            } else {
                ReflectivePropertyAccessor.PropertyCacheKey cacheKey = new ReflectivePropertyAccessor.PropertyCacheKey(type, name, target instanceof Class);
                if (this.readerCache.containsKey(cacheKey)) {
                    return true;
                } else {
                    Method method = this.findGetterForProperty(name, type, target);
                    TypeDescriptor typeDescriptor;
                    if (method != null) {
                        Property property = new Property(type, method, (Method)null);
                        typeDescriptor = new TypeDescriptor(property);
                        method = ClassUtils.getInterfaceMethodIfPossible(method);
                        this.readerCache.put(cacheKey, new ReflectivePropertyAccessor.InvokerPair(method, typeDescriptor));
                        this.typeDescriptorCache.put(cacheKey, typeDescriptor);
                        return true;
                    } else {
                        Field field = this.findField(name, type, target);
                        if (field != null) {
                            typeDescriptor = new TypeDescriptor(field);
                            this.readerCache.put(cacheKey, new ReflectivePropertyAccessor.InvokerPair(field, typeDescriptor));
                            this.typeDescriptorCache.put(cacheKey, typeDescriptor);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
    }

    public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
        Assert.state(target != null, "Target must not be null");
        Class<?> type = target instanceof Class ? (Class)target : target.getClass();
        if (type.isArray() && name.equals("length")) {
            if (target instanceof Class) {
                throw new AccessException("Cannot access length on array class itself");
            } else {
                return new TypedValue(Array.getLength(target));
            }
        } else {
            ReflectivePropertyAccessor.PropertyCacheKey cacheKey = new ReflectivePropertyAccessor.PropertyCacheKey(type, name, target instanceof Class);
            ReflectivePropertyAccessor.InvokerPair invoker = (ReflectivePropertyAccessor.InvokerPair)this.readerCache.get(cacheKey);
            this.lastReadInvokerPair = invoker;
            Object value;
            if (invoker == null || invoker.member instanceof Method) {
                Method method = (Method)((Method)(invoker != null ? invoker.member : null));
                if (method == null) {
                    method = this.findGetterForProperty(name, type, target);
                    if (method != null) {
                        Property property = new Property(type, method, (Method)null);
                        TypeDescriptor typeDescriptor = new TypeDescriptor(property);
                        method = ClassUtils.getInterfaceMethodIfPossible(method);
                        invoker = new ReflectivePropertyAccessor.InvokerPair(method, typeDescriptor);
                        this.lastReadInvokerPair = invoker;
                        this.readerCache.put(cacheKey, invoker);
                    }
                }

                if (method != null) {
                    try {
                        ReflectionUtils.makeAccessible(method);
                        value = method.invoke(target);
                        return new TypedValue(value, invoker.typeDescriptor.narrow(value));
                    } catch (Exception var10) {
                        throw new AccessException("Unable to access property '" + name + "' through getter method", var10);
                    }
                }
            }

            if (invoker == null || invoker.member instanceof Field) {
                Field field = (Field)((Field)(invoker == null ? null : invoker.member));
                if (field == null) {
                    field = this.findField(name, type, target);
                    if (field != null) {
                        invoker = new ReflectivePropertyAccessor.InvokerPair(field, new TypeDescriptor(field));
                        this.lastReadInvokerPair = invoker;
                        this.readerCache.put(cacheKey, invoker);
                    }
                }

                if (field != null) {
                    try {
                        ReflectionUtils.makeAccessible(field);
                        value = field.get(target);
                        return new TypedValue(value, invoker.typeDescriptor.narrow(value));
                    } catch (Exception var11) {
                        throw new AccessException("Unable to access field '" + name + "'", var11);
                    }
                }
            }

            throw new AccessException("Neither getter method nor field found for property '" + name + "'");
        }
    }

    public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
        if (this.allowWrite && target != null) {
            Class<?> type = target instanceof Class ? (Class)target : target.getClass();
            ReflectivePropertyAccessor.PropertyCacheKey cacheKey = new ReflectivePropertyAccessor.PropertyCacheKey(type, name, target instanceof Class);
            if (this.writerCache.containsKey(cacheKey)) {
                return true;
            } else {
                Method method = this.findSetterForProperty(name, type, target);
                if (method != null) {
                    Property property = new Property(type, (Method)null, method);
                    TypeDescriptor typeDescriptor = new TypeDescriptor(property);
                    method = ClassUtils.getInterfaceMethodIfPossible(method);
                    this.writerCache.put(cacheKey, method);
                    this.typeDescriptorCache.put(cacheKey, typeDescriptor);
                    return true;
                } else {
                    Field field = this.findField(name, type, target);
                    if (field != null) {
                        this.writerCache.put(cacheKey, field);
                        this.typeDescriptorCache.put(cacheKey, new TypeDescriptor(field));
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
    }

    public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) throws AccessException {
        if (!this.allowWrite) {
            throw new AccessException("PropertyAccessor for property '" + name + "' on target [" + target + "] does not allow write operations");
        } else {
            Assert.state(target != null, "Target must not be null");
            Class<?> type = target instanceof Class ? (Class)target : target.getClass();
            Object possiblyConvertedNewValue = newValue;
            TypeDescriptor typeDescriptor = this.getTypeDescriptor(context, target, name);
            if (typeDescriptor != null) {
                try {
                    possiblyConvertedNewValue = context.getTypeConverter().convertValue(newValue, TypeDescriptor.forObject(newValue), typeDescriptor);
                } catch (EvaluationException var14) {
                    throw new AccessException("Type conversion failure", var14);
                }
            }

            ReflectivePropertyAccessor.PropertyCacheKey cacheKey = new ReflectivePropertyAccessor.PropertyCacheKey(type, name, target instanceof Class);
            Member cachedMember = (Member)this.writerCache.get(cacheKey);
            if (cachedMember == null || cachedMember instanceof Method) {
                Method method = (Method)cachedMember;
                if (method == null) {
                    method = this.findSetterForProperty(name, type, target);
                    if (method != null) {
                        method = ClassUtils.getInterfaceMethodIfPossible(method);
                        cachedMember = method;
                        this.writerCache.put(cacheKey, method);
                    }
                }

                if (method != null) {
                    try {
                        ReflectionUtils.makeAccessible(method);
                        method.invoke(target, possiblyConvertedNewValue);
                        return;
                    } catch (Exception var12) {
                        throw new AccessException("Unable to access property '" + name + "' through setter method", var12);
                    }
                }
            }

            if (cachedMember == null || cachedMember instanceof Field) {
                Field field = (Field)cachedMember;
                if (field == null) {
                    field = this.findField(name, type, target);
                    if (field != null) {
                        this.writerCache.put(cacheKey, field);
                    }
                }

                if (field != null) {
                    try {
                        ReflectionUtils.makeAccessible(field);
                        field.set(target, possiblyConvertedNewValue);
                        return;
                    } catch (Exception var13) {
                        throw new AccessException("Unable to access field '" + name + "'", var13);
                    }
                }
            }

            throw new AccessException("Neither setter method nor field found for property '" + name + "'");
        }
    }

    /** @deprecated */
    @Deprecated
    @Nullable
    public Member getLastReadInvokerPair() {
        ReflectivePropertyAccessor.InvokerPair lastReadInvoker = this.lastReadInvokerPair;
        return lastReadInvoker != null ? lastReadInvoker.member : null;
    }

    @Nullable
    private TypeDescriptor getTypeDescriptor(EvaluationContext context, Object target, String name) {
        Class<?> type = target instanceof Class ? (Class)target : target.getClass();
        if (type.isArray() && name.equals("length")) {
            return TypeDescriptor.valueOf(Integer.TYPE);
        } else {
            ReflectivePropertyAccessor.PropertyCacheKey cacheKey = new ReflectivePropertyAccessor.PropertyCacheKey(type, name, target instanceof Class);
            TypeDescriptor typeDescriptor = (TypeDescriptor)this.typeDescriptorCache.get(cacheKey);
            if (typeDescriptor == null) {
                try {
                    if (this.canRead(context, target, name) || this.canWrite(context, target, name)) {
                        typeDescriptor = (TypeDescriptor)this.typeDescriptorCache.get(cacheKey);
                    }
                } catch (AccessException var8) {
                }
            }

            return typeDescriptor;
        }
    }

    @Nullable
    private Method findGetterForProperty(String propertyName, Class<?> clazz, Object target) {
        Method method = this.findGetterForProperty(propertyName, clazz, target instanceof Class);
        if (method == null && target instanceof Class) {
            method = this.findGetterForProperty(propertyName, target.getClass(), false);
        }

        return method;
    }

    @Nullable
    private Method findSetterForProperty(String propertyName, Class<?> clazz, Object target) {
        Method method = this.findSetterForProperty(propertyName, clazz, target instanceof Class);
        if (method == null && target instanceof Class) {
            method = this.findSetterForProperty(propertyName, target.getClass(), false);
        }

        return method;
    }

    @Nullable
    protected Method findGetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic) {
        Method method = this.findMethodForProperty(this.getPropertyMethodSuffixes(propertyName), "get", clazz, mustBeStatic, 0, ANY_TYPES);
        if (method == null) {
            method = this.findMethodForProperty(this.getPropertyMethodSuffixes(propertyName), "is", clazz, mustBeStatic, 0, BOOLEAN_TYPES);
        }

        return method;
    }

    @Nullable
    protected Method findSetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic) {
        return this.findMethodForProperty(this.getPropertyMethodSuffixes(propertyName), "set", clazz, mustBeStatic, 1, ANY_TYPES);
    }

    @Nullable
    private Method findMethodForProperty(String[] methodSuffixes, String prefix, Class<?> clazz, boolean mustBeStatic, int numberOfParams, Set<Class<?>> requiredReturnTypes) {
        Method[] methods = this.getSortedMethods(clazz);
        String[] var8 = methodSuffixes;
        int var9 = methodSuffixes.length;

        for(int var10 = 0; var10 < var9; ++var10) {
            String methodSuffix = var8[var10];
            Method[] var12 = methods;
            int var13 = methods.length;

            for(int var14 = 0; var14 < var13; ++var14) {
                Method method = var12[var14];
                if (this.isCandidateForProperty(method, clazz) && method.getName().equals(prefix + methodSuffix) && method.getParameterCount() == numberOfParams && (!mustBeStatic || Modifier.isStatic(method.getModifiers())) && (requiredReturnTypes.isEmpty() || requiredReturnTypes.contains(method.getReturnType()))) {
                    return method;
                }
            }
        }

        return null;
    }

    private Method[] getSortedMethods(Class<?> clazz) {
        return (Method[])this.sortedMethodsCache.computeIfAbsent(clazz, (key) -> {
            Method[] methods = key.getMethods();
            Arrays.sort(methods, (o1, o2) -> {
                return o1.isBridge() == o2.isBridge() ? 0 : (o1.isBridge() ? 1 : -1);
            });
            return methods;
        });
    }

    protected boolean isCandidateForProperty(Method method, Class<?> targetClass) {
        return true;
    }

    protected String[] getPropertyMethodSuffixes(String propertyName) {
        String suffix = this.getPropertyMethodSuffix(propertyName);
        return suffix.length() > 0 && Character.isUpperCase(suffix.charAt(0)) ? new String[]{suffix} : new String[]{suffix, StringUtils.capitalize(suffix)};
    }

    protected String getPropertyMethodSuffix(String propertyName) {
        return propertyName.length() > 1 && Character.isUpperCase(propertyName.charAt(1)) ? propertyName : StringUtils.capitalize(propertyName);
    }

    @Nullable
    private Field findField(String name, Class<?> clazz, Object target) {
        Field field = this.findField(name, clazz, target instanceof Class);
        if (field == null && target instanceof Class) {
            field = this.findField(name, target.getClass(), false);
        }

        return field;
    }

    @Nullable
    protected Field findField(String name, Class<?> clazz, boolean mustBeStatic) {
        Field[] fields = clazz.getFields();
        Field[] var5 = fields;
        int var6 = fields.length;

        int var7;
        for(var7 = 0; var7 < var6; ++var7) {
            Field field = var5[var7];
            if (field.getName().equals(name) && (!mustBeStatic || Modifier.isStatic(field.getModifiers()))) {
                return field;
            }
        }

        if (clazz.getSuperclass() != null) {
            Field field = this.findField(name, clazz.getSuperclass(), mustBeStatic);
            if (field != null) {
                return field;
            }
        }

        Class[] var11 = clazz.getInterfaces();
        var6 = var11.length;

        for(var7 = 0; var7 < var6; ++var7) {
            Class<?> implementedInterface = var11[var7];
            Field field = this.findField(name, implementedInterface, mustBeStatic);
            if (field != null) {
                return field;
            }
        }

        return null;
    }

    public PropertyAccessor createOptimalAccessor(EvaluationContext context, @Nullable Object target, String name) {
        if (target == null) {
            return this;
        } else {
            Class<?> clazz = target instanceof Class ? (Class)target : target.getClass();
            if (clazz.isArray()) {
                return this;
            } else {
                ReflectivePropertyAccessor.PropertyCacheKey cacheKey = new ReflectivePropertyAccessor.PropertyCacheKey(clazz, name, target instanceof Class);
                ReflectivePropertyAccessor.InvokerPair invocationTarget = (ReflectivePropertyAccessor.InvokerPair)this.readerCache.get(cacheKey);
                if (invocationTarget == null || invocationTarget.member instanceof Method) {
                    Method method = (Method)((Method)(invocationTarget != null ? invocationTarget.member : null));
                    if (method == null) {
                        method = this.findGetterForProperty(name, clazz, target);
                        if (method != null) {
                            TypeDescriptor typeDescriptor = new TypeDescriptor(new MethodParameter(method, -1));
                            method = ClassUtils.getInterfaceMethodIfPossible(method);
                            invocationTarget = new ReflectivePropertyAccessor.InvokerPair(method, typeDescriptor);
                            ReflectionUtils.makeAccessible(method);
                            this.readerCache.put(cacheKey, invocationTarget);
                        }
                    }

                    if (method != null) {
                        return new ReflectivePropertyAccessor.OptimalPropertyAccessor(invocationTarget);
                    }
                }

                if (invocationTarget == null || invocationTarget.member instanceof Field) {
                    Field field = invocationTarget != null ? (Field)invocationTarget.member : null;
                    if (field == null) {
                        field = this.findField(name, clazz, target instanceof Class);
                        if (field != null) {
                            invocationTarget = new ReflectivePropertyAccessor.InvokerPair(field, new TypeDescriptor(field));
                            ReflectionUtils.makeAccessible(field);
                            this.readerCache.put(cacheKey, invocationTarget);
                        }
                    }

                    if (field != null) {
                        return new ReflectivePropertyAccessor.OptimalPropertyAccessor(invocationTarget);
                    }
                }

                return this;
            }
        }
    }

    static {
        Set<Class<?>> booleanTypes = new HashSet(4);
        booleanTypes.add(Boolean.class);
        booleanTypes.add(Boolean.TYPE);
        BOOLEAN_TYPES = Collections.unmodifiableSet(booleanTypes);
    }

    public static class OptimalPropertyAccessor implements CompilablePropertyAccessor {
        public final Member member;
        private final TypeDescriptor typeDescriptor;

        OptimalPropertyAccessor(ReflectivePropertyAccessor.InvokerPair target) {
            this.member = target.member;
            this.typeDescriptor = target.typeDescriptor;
        }

        @Nullable
        public Class<?>[] getSpecificTargetClasses() {
            throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
        }

        public boolean canRead(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
            if (target == null) {
                return false;
            } else {
                Class<?> type = target instanceof Class ? (Class)target : target.getClass();
                if (type.isArray()) {
                    return false;
                } else if (this.member instanceof Method) {
                    Method method = (Method)this.member;
                    String getterName = "get" + StringUtils.capitalize(name);
                    if (getterName.equals(method.getName())) {
                        return true;
                    } else {
                        getterName = "is" + StringUtils.capitalize(name);
                        return getterName.equals(method.getName());
                    }
                } else {
                    Field field = (Field)this.member;
                    return field.getName().equals(name);
                }
            }
        }

        public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
            Object value;
            if (this.member instanceof Method) {
                Method method = (Method)this.member;

                try {
                    ReflectionUtils.makeAccessible(method);
                    value = method.invoke(target);
                    return new TypedValue(value, this.typeDescriptor.narrow(value));
                } catch (Exception var6) {
                    throw new AccessException("Unable to access property '" + name + "' through getter method", var6);
                }
            } else {
                Field field = (Field)this.member;

                try {
                    ReflectionUtils.makeAccessible(field);
                    value = field.get(target);
                    return new TypedValue(value, this.typeDescriptor.narrow(value));
                } catch (Exception var7) {
                    throw new AccessException("Unable to access field '" + name + "'", var7);
                }
            }
        }

        public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) {
            throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
        }

        public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) {
            throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
        }

        public boolean isCompilable() {
            return Modifier.isPublic(this.member.getModifiers()) && Modifier.isPublic(this.member.getDeclaringClass().getModifiers());
        }

        public Class<?> getPropertyType() {
            return this.member instanceof Method ? ((Method)this.member).getReturnType() : ((Field)this.member).getType();
        }

        public void generateCode(String propertyName, MethodVisitor mv, CodeFlow cf) {
            boolean isStatic = Modifier.isStatic(this.member.getModifiers());
            String descriptor = cf.lastDescriptor();
            String classDesc = this.member.getDeclaringClass().getName().replace('.', '/');
            if (!isStatic) {
                if (descriptor == null) {
                    cf.loadTarget(mv);
                }

                if (descriptor == null || !classDesc.equals(descriptor.substring(1))) {
                    mv.visitTypeInsn(192, classDesc);
                }
            } else if (descriptor != null) {
                mv.visitInsn(87);
            }

            if (this.member instanceof Method) {
                Method method = (Method)this.member;
                boolean isInterface = method.getDeclaringClass().isInterface();
                int opcode = isStatic ? 184 : (isInterface ? 185 : 182);
                mv.visitMethodInsn(opcode, classDesc, method.getName(), CodeFlow.createSignatureDescriptor(method), isInterface);
            } else {
                mv.visitFieldInsn(isStatic ? 178 : 180, classDesc, this.member.getName(), CodeFlow.toJvmDescriptor(((Field)this.member).getType()));
            }

        }
    }

    private static final class PropertyCacheKey implements Comparable<ReflectivePropertyAccessor.PropertyCacheKey> {
        private final Class<?> clazz;
        private final String property;
        private boolean targetIsClass;

        public PropertyCacheKey(Class<?> clazz, String name, boolean targetIsClass) {
            this.clazz = clazz;
            this.property = name;
            this.targetIsClass = targetIsClass;
        }

        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            } else if (!(other instanceof ReflectivePropertyAccessor.PropertyCacheKey)) {
                return false;
            } else {
                ReflectivePropertyAccessor.PropertyCacheKey otherKey = (ReflectivePropertyAccessor.PropertyCacheKey)other;
                return this.clazz == otherKey.clazz && this.property.equals(otherKey.property) && this.targetIsClass == otherKey.targetIsClass;
            }
        }

        public int hashCode() {
            return this.clazz.hashCode() * 29 + this.property.hashCode();
        }

        public String toString() {
            return "PropertyCacheKey [clazz=" + this.clazz.getName() + ", property=" + this.property + ", targetIsClass=" + this.targetIsClass + "]";
        }

        public int compareTo(ReflectivePropertyAccessor.PropertyCacheKey other) {
            int result = this.clazz.getName().compareTo(other.clazz.getName());
            if (result == 0) {
                result = this.property.compareTo(other.property);
            }

            return result;
        }
    }

    private static class InvokerPair {
        final Member member;
        final TypeDescriptor typeDescriptor;

        public InvokerPair(Member member, TypeDescriptor typeDescriptor) {
            this.member = member;
            this.typeDescriptor = typeDescriptor;
        }
    }
}
