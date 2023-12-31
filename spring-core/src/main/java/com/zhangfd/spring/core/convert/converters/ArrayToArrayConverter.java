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

package com.zhangfd.spring.core.convert.converters;

import com.zhangfd.spring.core.convert.ConversionService;
import com.zhangfd.spring.core.convert.GenericConversionService;
import com.zhangfd.spring.core.convert.TypeDescriptor;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;



/**
 * Converts an array to another array. First adapts the source array to a List,
 * then delegates to {@link CollectionToArrayConverter} to perform the target
 * array conversion.
 *
 * @author Keith Donald
 * @author Phillip Webb
 * @since 3.0
 */
public final class ArrayToArrayConverter implements ConditionalGenericConverter {

	private final CollectionToArrayConverter helperConverter;

	private final ConversionService conversionService;


	public ArrayToArrayConverter(ConversionService conversionService) {
		this.helperConverter = new CollectionToArrayConverter(conversionService);
		this.conversionService = conversionService;
	}


	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Object[].class, Object[].class));
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return this.helperConverter.matches(sourceType, targetType);
	}

	@Override
	@Nullable
	public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (this.conversionService instanceof GenericConversionService) {
			TypeDescriptor targetElement = targetType.getElementTypeDescriptor();
			if (targetElement != null &&
					((GenericConversionService) this.conversionService).canBypassConvert(
							sourceType.getElementTypeDescriptor(), targetElement)) {
				return source;
			}
		}
		List<Object> sourceList = Arrays.asList(ObjectUtils.toObjectArray(source));
		return this.helperConverter.convert(sourceList, sourceType, targetType);
	}

}
