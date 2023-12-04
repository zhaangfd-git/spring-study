/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhangfd.spring.context.expression;


import com.zhangfd.spring.BeansException;
import com.zhangfd.spring.expression.AccessException;
import com.zhangfd.spring.expression.BeanResolver;
import com.zhangfd.spring.expression.EvaluationContext;
import com.zhangfd.spring.factory.BeanFactory;
import com.zhangfd.spring.util.Assert;

/**
 * EL bean resolver that operates against a Spring
 * {@link org.springframework.beans.factory.BeanFactory}.
 *
 * @author Juergen Hoeller
 * @since 3.0.4
 */
public class BeanFactoryResolver implements BeanResolver {

	private final BeanFactory beanFactory;


	/**
	 * Create a new {@link BeanFactoryResolver} for the given factory.
	 * @param beanFactory the {@link BeanFactory} to resolve bean names against
	 */
	public BeanFactoryResolver(BeanFactory beanFactory) {
		Assert.notNull(beanFactory, "BeanFactory must not be null");
		this.beanFactory = beanFactory;
	}


	@Override
	public Object resolve(EvaluationContext context, String beanName) throws AccessException {
		try {
			return this.beanFactory.getBean(beanName);
		}
		catch (BeansException ex) {
			throw new AccessException("Could not resolve bean reference against BeanFactory", ex);
		}
	}

}
