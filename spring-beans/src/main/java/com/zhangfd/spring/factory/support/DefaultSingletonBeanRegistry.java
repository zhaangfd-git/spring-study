package com.zhangfd.spring.factory.support;

import com.zhangfd.spring.lang.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry {




    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);


    //保存创建实例时的异常信息
    @Nullable
    private Set<Exception> suppressedExceptions;


    //最大支持的保存异常信息个数
    private static final int SUPPRESSED_EXCEPTIONS_LIMIT = 100;





    protected void onSuppressedException(Exception ex) {
        synchronized (this.singletonObjects) {
            if (this.suppressedExceptions != null && this.suppressedExceptions.size() < SUPPRESSED_EXCEPTIONS_LIMIT) {
                this.suppressedExceptions.add(ex);
            }
        }
    }

    @Nullable
    public Set<Exception> getSuppressedExceptions() {
        return suppressedExceptions;
    }

    public void setSuppressedExceptions(@Nullable Set<Exception> suppressedExceptions) {
        this.suppressedExceptions = suppressedExceptions;
    }
}
