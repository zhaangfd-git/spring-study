//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.jcl;

public abstract class LogFactory {
    public LogFactory() {
    }

    public static Log getLog(Class<?> clazz) {
        return getLog(clazz.getName());
    }

    public static Log getLog(String name) {
        return LogAdapter.createLog(name);
    }

    /** @deprecated */
    @Deprecated
    public static LogFactory getFactory() {
        return new LogFactory() {
        };
    }

    /** @deprecated */
    @Deprecated
    public Log getInstance(Class<?> clazz) {
        return getLog(clazz);
    }

    /** @deprecated */
    @Deprecated
    public Log getInstance(String name) {
        return getLog(name);
    }
}
