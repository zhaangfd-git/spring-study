package com.zhangfd.spring.factory.config;

public interface ConfigurableBeanFactory {

    /**
     * Scope identifier for the standard singleton scope: {@value}.
     * <p>Custom scopes can be added via {@code registerScope}.
     * @see #registerScope
     */
    String SCOPE_SINGLETON = "singleton";

    /**
     * Scope identifier for the standard prototype scope: {@value}.
     * <p>Custom scopes can be added via {@code registerScope}.
     * @see #registerScope
     */
    String SCOPE_PROTOTYPE = "prototype";


}
