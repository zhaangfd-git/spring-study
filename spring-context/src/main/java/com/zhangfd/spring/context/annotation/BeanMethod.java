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

package com.zhangfd.spring.context.annotation;


import com.zhangfd.spring.core.type.MethodMetadata;
import com.zhangfd.spring.factory.parsing.Problem;
import com.zhangfd.spring.factory.parsing.ProblemReporter;

/**
 * Represents a {@link Configuration @Configuration} class method marked with the
 * {@link Bean @Bean} annotation.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.0
 * @see ConfigurationClass
 * @see ConfigurationClassParser
 * @see ConfigurationClassBeanDefinitionReader
 */
final class BeanMethod extends ConfigurationMethod {

	public BeanMethod(MethodMetadata metadata, ConfigurationClass configurationClass) {
		super(metadata, configurationClass);
	}

	@Override
	public void validate(ProblemReporter problemReporter) {
		if (getMetadata().isStatic()) {
			// static @Bean methods have no constraints to validate -> return immediately
			return;
		}

		if (this.configurationClass.getMetadata().isAnnotated(Configuration.class.getName())) {
			if (!getMetadata().isOverridable()) {
				// instance @Bean methods within @Configuration classes must be overridable to accommodate CGLIB
				problemReporter.error(new NonOverridableMethodError());
			}
		}
	}


	private class NonOverridableMethodError extends Problem {

		public NonOverridableMethodError() {
			super(String.format("@Bean method '%s' must not be private or final; change the method's modifiers to continue",
					getMetadata().getMethodName()), getResourceLocation());
		}
	}
}
