/*
 * Copyright 2002-2020 the original author or authors.
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

package com.zhangfd.spring.core;

import java.lang.annotation.Annotation;

import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.ClassUtils;


/**
 * A common delegate for detecting Kotlin's presence and for identifying Kotlin types.
 *
 * @author Juergen Hoeller
 * @author Sebastien Deleuze
 * @since 5.0
 */
@SuppressWarnings("unchecked")
public abstract class KotlinDetector {

	@Nullable
	private static final Class<? extends Annotation> kotlinMetadata;

	private static final boolean kotlinReflectPresent;

	static {
		Class<?> metadata;
		ClassLoader classLoader = KotlinDetector.class.getClassLoader();
		try {
			metadata = ClassUtils.forName("kotlin.Metadata", classLoader);
		}
		catch (ClassNotFoundException ex) {
			// Kotlin API not available - no Kotlin support
			metadata = null;
		}
		kotlinMetadata = (Class<? extends Annotation>) metadata;
		kotlinReflectPresent = ClassUtils.isPresent("kotlin.reflect.full.KClasses", classLoader);
	}


	/**
	 * Determine whether Kotlin is present in general.
	 */
	public static boolean isKotlinPresent() {
		return (kotlinMetadata != null);
	}

	/**
	 * Determine whether Kotlin reflection is present.
	 * @since 5.1
	 */
	public static boolean isKotlinReflectPresent() {
		return kotlinReflectPresent;
	}

	/**
	 * Determine whether the given {@code Class} is a Kotlin type
	 * (with Kotlin metadata present on it).
	 */
	public static boolean isKotlinType(Class<?> clazz) {
		return (kotlinMetadata != null && clazz.getDeclaredAnnotation(kotlinMetadata) != null);
	}

}
