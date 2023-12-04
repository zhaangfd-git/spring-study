//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.support;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zhangfd.spring.core.MethodParameter;
import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.expression.*;
import com.zhangfd.spring.lang.Nullable;


public class ReflectiveConstructorResolver implements ConstructorResolver {
    public ReflectiveConstructorResolver() {
    }

    @Nullable
    public ConstructorExecutor resolve(EvaluationContext context, String typeName, List<TypeDescriptor> argumentTypes) throws AccessException {
        try {
            TypeConverter typeConverter = context.getTypeConverter();
            Class<?> type = context.getTypeLocator().findType(typeName);
            Constructor<?>[] ctors = type.getConstructors();
            Arrays.sort(ctors, (c1, c2) -> {
                int c1pl = c1.getParameterCount();
                int c2pl = c2.getParameterCount();
                return Integer.compare(c1pl, c2pl);
            });
            Constructor<?> closeMatch = null;
            Constructor<?> matchRequiringConversion = null;
            Constructor[] var9 = ctors;
            int var10 = ctors.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                Constructor<?> ctor = var9[var11];
                int paramCount = ctor.getParameterCount();
                List<TypeDescriptor> paramDescriptors = new ArrayList(paramCount);

                for(int i = 0; i < paramCount; ++i) {
                    paramDescriptors.add(new TypeDescriptor(new MethodParameter(ctor, i)));
                }

                ReflectionHelper.ArgumentsMatchInfo matchInfo = null;
                if (ctor.isVarArgs() && argumentTypes.size() >= paramCount - 1) {
                    matchInfo = ReflectionHelper.compareArgumentsVarargs(paramDescriptors, argumentTypes, typeConverter);
                } else if (paramCount == argumentTypes.size()) {
                    matchInfo = ReflectionHelper.compareArguments(paramDescriptors, argumentTypes, typeConverter);
                }

                if (matchInfo != null) {
                    if (matchInfo.isExactMatch()) {
                        return new ReflectiveConstructorExecutor(ctor);
                    }

                    if (matchInfo.isCloseMatch()) {
                        closeMatch = ctor;
                    } else if (matchInfo.isMatchRequiringConversion()) {
                        matchRequiringConversion = ctor;
                    }
                }
            }

            if (closeMatch != null) {
                return new ReflectiveConstructorExecutor(closeMatch);
            } else if (matchRequiringConversion != null) {
                return new ReflectiveConstructorExecutor(matchRequiringConversion);
            } else {
                return null;
            }
        } catch (EvaluationException var16) {
            throw new AccessException("Failed to resolve constructor", var16);
        }
    }
}
