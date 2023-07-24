//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.jcl;

public interface Log {
    boolean isFatalEnabled();

    boolean isErrorEnabled();

    boolean isWarnEnabled();

    boolean isInfoEnabled();

    boolean isDebugEnabled();

    boolean isTraceEnabled();

    void fatal(Object var1);

    void fatal(Object var1, Throwable var2);

    void error(Object var1);

    void error(Object var1, Throwable var2);

    void warn(Object var1);

    void warn(Object var1, Throwable var2);

    void info(Object var1);

    void info(Object var1, Throwable var2);

    void debug(Object var1);

    void debug(Object var1, Throwable var2);

    void trace(Object var1);

    void trace(Object var1, Throwable var2);
}
