//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.support;

import java.lang.reflect.Constructor;

import com.zhangfd.spring.expression.AccessException;
import com.zhangfd.spring.expression.ConstructorExecutor;
import com.zhangfd.spring.expression.EvaluationContext;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.ReflectionUtils;


public class ReflectiveConstructorExecutor implements ConstructorExecutor {
    private final Constructor<?> ctor;
    @Nullable
    private final Integer varargsPosition;

    public ReflectiveConstructorExecutor(Constructor<?> ctor) {
        this.ctor = ctor;
        if (ctor.isVarArgs()) {
            this.varargsPosition = ctor.getParameterCount() - 1;
        } else {
            this.varargsPosition = null;
        }

    }

    public TypedValue execute(EvaluationContext context, Object... arguments) throws AccessException {
        try {
            ReflectionHelper.convertArguments(context.getTypeConverter(), arguments, this.ctor, this.varargsPosition);
            if (this.ctor.isVarArgs()) {
                arguments = ReflectionHelper.setupArgumentsForVarargsInvocation(this.ctor.getParameterTypes(), arguments);
            }

            ReflectionUtils.makeAccessible(this.ctor);
            return new TypedValue(this.ctor.newInstance(arguments));
        } catch (Exception var4) {
            throw new AccessException("Problem invoking constructor: " + this.ctor, var4);
        }
    }

    public Constructor<?> getConstructor() {
        return this.ctor;
    }
}
