/*
 * Copyright 2002-2017 the original author or authors.
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


import com.zhangfd.spring.core.env.Environment;
import com.zhangfd.spring.expression.AccessException;
import com.zhangfd.spring.expression.EvaluationContext;
import com.zhangfd.spring.expression.PropertyAccessor;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;

/**
 * Read-only EL property accessor that knows how to retrieve keys
 * of a Spring {@link Environment} instance.
 *
 * @author Chris Beams
 * @since 3.1
 */
public class EnvironmentAccessor implements PropertyAccessor {

	@Override
	public Class<?>[] getSpecificTargetClasses() {
		return new Class<?>[] {Environment.class};
	}

	/**
	 * Can read any {@link Environment}, thus always returns true.
	 * @return true
	 */
	@Override
	public boolean canRead(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
		return true;
	}

	/**
	 * Access the given target object by resolving the given property name against the given target
	 * environment.
	 */
	@Override
	public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
		Assert.state(target instanceof Environment, "Target must be of type Environment");
		return new TypedValue(((Environment) target).getProperty(name));
	}

	/**
	 * Read-only: returns {@code false}.
	 */
	@Override
	public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
		return false;
	}

	/**
	 * Read-only: no-op.
	 */
	@Override
	public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue)
			throws AccessException {
	}

}
