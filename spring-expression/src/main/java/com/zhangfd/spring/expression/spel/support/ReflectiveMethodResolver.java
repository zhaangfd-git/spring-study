//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zhangfd.spring.core.BridgeMethodResolver;
import com.zhangfd.spring.core.MethodParameter;
import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.expression.*;
import com.zhangfd.spring.expression.spel.SpelMessage;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.ReflectionUtils;


public class ReflectiveMethodResolver implements MethodResolver {
    private final boolean useDistance;
    @Nullable
    private Map<Class<?>, MethodFilter> filters;

    public ReflectiveMethodResolver() {
        this.useDistance = true;
    }

    public ReflectiveMethodResolver(boolean useDistance) {
        this.useDistance = useDistance;
    }

    public void registerMethodFilter(Class<?> type, @Nullable MethodFilter filter) {
        if (this.filters == null) {
            this.filters = new HashMap();
        }

        if (filter != null) {
            this.filters.put(type, filter);
        } else {
            this.filters.remove(type);
        }

    }

    @Nullable
    public MethodExecutor resolve(EvaluationContext context, Object targetObject, String name, List<TypeDescriptor> argumentTypes) throws AccessException {
        try {
            TypeConverter typeConverter = context.getTypeConverter();
            Class<?> type = targetObject instanceof Class ? (Class)targetObject : targetObject.getClass();
            ArrayList<Method> methods = new ArrayList(this.getMethods(type, targetObject));
            MethodFilter filter = this.filters != null ? (MethodFilter)this.filters.get(type) : null;
            if (filter != null) {
                List<Method> filtered = filter.filter(methods);
                methods = filtered instanceof ArrayList ? (ArrayList)filtered : new ArrayList(filtered);
            }

            if (methods.size() > 1) {
                methods.sort((m1, m2) -> {
                    int m1pl = m1.getParameterCount();
                    int m2pl = m2.getParameterCount();
                    if (m1pl == m2pl) {
                        if (!m1.isVarArgs() && m2.isVarArgs()) {
                            return -1;
                        } else {
                            return m1.isVarArgs() && !m2.isVarArgs() ? 1 : 0;
                        }
                    } else {
                        return Integer.compare(m1pl, m2pl);
                    }
                });
            }

            for(int i = 0; i < methods.size(); ++i) {
                methods.set(i, BridgeMethodResolver.findBridgedMethod((Method)methods.get(i)));
            }

            Set<Method> methodsToIterate = new LinkedHashSet(methods);
            Method closeMatch = null;
            int closeMatchDistance = 2147483647;
            Method matchRequiringConversion = null;
            boolean multipleOptions = false;
            Iterator var14 = methodsToIterate.iterator();

            while(true) {
                Method method;
                int matchDistance;
                label122:
                do {
                    while(true) {
                        while(true) {
                            ArrayList paramDescriptors;
                            ReflectionHelper.ArgumentsMatchInfo matchInfo;
                            do {
                                do {
                                    if (!var14.hasNext()) {
                                        if (closeMatch != null) {
                                            return new ReflectiveMethodExecutor(closeMatch);
                                        }

                                        if (matchRequiringConversion != null) {
                                            if (multipleOptions) {
                                                throw new SpelEvaluationException(SpelMessage.MULTIPLE_POSSIBLE_METHODS, new Object[]{name});
                                            }

                                            return new ReflectiveMethodExecutor(matchRequiringConversion);
                                        }

                                        return null;
                                    }

                                    method = (Method)var14.next();
                                } while(!method.getName().equals(name));

                                int paramCount = method.getParameterCount();
                                paramDescriptors = new ArrayList(paramCount);

                                for(int i = 0; i < paramCount; ++i) {
                                    paramDescriptors.add(new TypeDescriptor(new MethodParameter(method, i)));
                                }

                                matchInfo = null;
                                if (method.isVarArgs() && argumentTypes.size() >= paramCount - 1) {
                                    matchInfo = ReflectionHelper.compareArgumentsVarargs(paramDescriptors, argumentTypes, typeConverter);
                                } else if (paramCount == argumentTypes.size()) {
                                    matchInfo = ReflectionHelper.compareArguments(paramDescriptors, argumentTypes, typeConverter);
                                }
                            } while(matchInfo == null);

                            if (matchInfo.isExactMatch()) {
                                return new ReflectiveMethodExecutor(method);
                            }

                            if (matchInfo.isCloseMatch()) {
                                if (this.useDistance) {
                                    matchDistance = ReflectionHelper.getTypeDifferenceWeight(paramDescriptors, argumentTypes);
                                    continue label122;
                                }

                                if (closeMatch == null) {
                                    closeMatch = method;
                                }
                            } else if (matchInfo.isMatchRequiringConversion()) {
                                if (matchRequiringConversion != null) {
                                    multipleOptions = true;
                                }

                                matchRequiringConversion = method;
                            }
                        }
                    }
                } while(closeMatch != null && matchDistance >= closeMatchDistance);

                closeMatch = method;
                closeMatchDistance = matchDistance;
            }
        } catch (EvaluationException var20) {
            throw new AccessException("Failed to resolve method", var20);
        }
    }

    private Set<Method> getMethods(Class<?> type, Object targetObject) {
        LinkedHashSet result;
        int var6;
        Method[] methods;
        Method[] var14;
        int var15;
        Method method;
        if (targetObject instanceof Class) {
            result = new LinkedHashSet();
            methods = this.getMethods(type);
            var14 = methods;
            var6 = methods.length;

            for(var15 = 0; var15 < var6; ++var15) {
                method = var14[var15];
                if (Modifier.isStatic(method.getModifiers())) {
                    result.add(method);
                }
            }

            Collections.addAll(result, this.getMethods(Class.class));
            return result;
        } else if (!Proxy.isProxyClass(type)) {
            result = new LinkedHashSet();
            methods = this.getMethods(type);
            var14 = methods;
            var6 = methods.length;

            for(var15 = 0; var15 < var6; ++var15) {
                method = var14[var15];
                if (this.isCandidateForInvocation(method, type)) {
                    result.add(method);
                }
            }

            return result;
        } else {
            result = new LinkedHashSet();
            Class[] var4 = type.getInterfaces();
            int var5 = var4.length;

            for(var6 = 0; var6 < var5; ++var6) {
                Class<?> ifc = var4[var6];
                Method[] methods01 = this.getMethods(ifc);
                Method[] var9 = methods01;
                int var10 = methods01.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    Method method01 = var9[var11];
                    if (this.isCandidateForInvocation(method01, type)) {
                        result.add(method01);
                    }
                }
            }

            return result;
        }
    }

    protected Method[] getMethods(Class<?> type) {
        return type.getMethods();
    }

    protected boolean isCandidateForInvocation(Method method, Class<?> targetClass) {
        return true;
    }
}
