//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression;

@FunctionalInterface
public interface TypeLocator {
    Class<?> findType(String var1) throws EvaluationException;
}
