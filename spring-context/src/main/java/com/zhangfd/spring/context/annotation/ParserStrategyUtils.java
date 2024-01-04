/*
 * Copyright 2002-2019 the original author or authors.
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

import com.zhangfd.spring.BeanUtils;
import com.zhangfd.spring.context.EnvironmentAware;
import com.zhangfd.spring.context.ResourceLoaderAware;
import com.zhangfd.spring.core.env.Environment;
import com.zhangfd.spring.core.io.ResourceLoader;
import com.zhangfd.spring.exception.BeanInstantiationException;
import com.zhangfd.spring.factory.Aware;
import com.zhangfd.spring.factory.BeanClassLoaderAware;
import com.zhangfd.spring.factory.BeanFactory;
import com.zhangfd.spring.factory.BeanFactoryAware;
import com.zhangfd.spring.factory.config.ConfigurableBeanFactory;
import com.zhangfd.spring.factory.support.BeanDefinitionRegistry;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;

import java.lang.reflect.Constructor;



/**
 * Common delegate code for the handling of parser strategies, e.g.
 * {@code TypeFilter}, {@code ImportSelector}, {@code ImportBeanDefinitionRegistrar}
 *
 * @author Juergen Hoeller
 * @author Phillip Webb
 * @since 4.3.3
 */
abstract class ParserStrategyUtils {

	/**
	 * Instantiate a class using an appropriate constructor and return the new
	 * instance as the specified assignable type. The returned instance will
	 * have {@link BeanClassLoaderAware}, {@link BeanFactoryAware},
	 * {@link EnvironmentAware}, and {@link ResourceLoaderAware} contracts
	 * invoked if they are implemented by the given object.
	 * @since 5.2
	 */
	@SuppressWarnings("unchecked")
	static <T> T instantiateClass(Class<?> clazz, Class<T> assignableTo, Environment environment,
								  ResourceLoader resourceLoader, BeanDefinitionRegistry registry) {

		Assert.notNull(clazz, "Class must not be null");
		Assert.isAssignable(assignableTo, clazz);
		if (clazz.isInterface()) {
			throw new BeanInstantiationException(clazz, "Specified class is an interface");
		}
		ClassLoader classLoader = (registry instanceof ConfigurableBeanFactory ?
				((ConfigurableBeanFactory) registry).getBeanClassLoader() : resourceLoader.getClassLoader());
		T instance = (T) createInstance(clazz, environment, resourceLoader, registry, classLoader);
		ParserStrategyUtils.invokeAwareMethods(instance, environment, resourceLoader, registry, classLoader);
		return instance;
	}

	private static Object createInstance(Class<?> clazz, Environment environment,
			ResourceLoader resourceLoader, BeanDefinitionRegistry registry,
			@Nullable ClassLoader classLoader) {

		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		if (constructors.length == 1 && constructors[0].getParameterCount() > 0) {
			try {
				Constructor<?> constructor = constructors[0];
				Object[] args = resolveArgs(constructor.getParameterTypes(),
						environment, resourceLoader, registry, classLoader);
				return BeanUtils.instantiateClass(constructor, args);
			}
			catch (Exception ex) {
				throw new BeanInstantiationException(clazz, "No suitable constructor found", ex);
			}
		}
		return BeanUtils.instantiateClass(clazz);
	}

	private static Object[] resolveArgs(Class<?>[] parameterTypes,
			Environment environment, ResourceLoader resourceLoader,
			BeanDefinitionRegistry registry, @Nullable ClassLoader classLoader) {

			Object[] parameters = new Object[parameterTypes.length];
			for (int i = 0; i < parameterTypes.length; i++) {
				parameters[i] = resolveParameter(parameterTypes[i], environment,
						resourceLoader, registry, classLoader);
			}
			return parameters;
	}

	@Nullable
	private static Object resolveParameter(Class<?> parameterType,
			Environment environment, ResourceLoader resourceLoader,
			BeanDefinitionRegistry registry, @Nullable ClassLoader classLoader) {

		if (parameterType == Environment.class) {
			return environment;
		}
		if (parameterType == ResourceLoader.class) {
			return resourceLoader;
		}
		if (parameterType == BeanFactory.class) {
			return (registry instanceof BeanFactory ? registry : null);
		}
		if (parameterType == ClassLoader.class) {
			return classLoader;
		}
		throw new IllegalStateException("Illegal method parameter type: " + parameterType.getName());
	}

	private static void invokeAwareMethods(Object parserStrategyBean, Environment environment,
			ResourceLoader resourceLoader, BeanDefinitionRegistry registry, @Nullable ClassLoader classLoader) {

		if (parserStrategyBean instanceof Aware) {
			if (parserStrategyBean instanceof BeanClassLoaderAware && classLoader != null) {
				((BeanClassLoaderAware) parserStrategyBean).setBeanClassLoader(classLoader);
			}
			if (parserStrategyBean instanceof BeanFactoryAware && registry instanceof BeanFactory) {
				((BeanFactoryAware) parserStrategyBean).setBeanFactory((BeanFactory) registry);
			}
			if (parserStrategyBean instanceof EnvironmentAware) {
				((EnvironmentAware) parserStrategyBean).setEnvironment(environment);
			}
			if (parserStrategyBean instanceof ResourceLoaderAware) {
				((ResourceLoaderAware) parserStrategyBean).setResourceLoader(resourceLoader);
			}
		}
	}

}
