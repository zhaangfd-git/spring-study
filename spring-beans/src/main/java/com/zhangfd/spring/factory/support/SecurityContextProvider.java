package com.zhangfd.spring.factory.support;

import java.security.AccessControlContext;

public interface SecurityContextProvider {

	/**
	 * Provides a security access control context relevant to a bean factory.
	 * @return bean factory security control context
	 */
	AccessControlContext getAccessControlContext();

}