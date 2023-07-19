package com.zhangfd.spring;

import com.zhangfd.spring.factory.InjectionPoint;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.StringUtils;

public class UnsatisfiedDependencyException extends BeanCreationException {

	@Nullable
	private final InjectionPoint injectionPoint;


	/**
	 * Create a new UnsatisfiedDependencyException.
	 * @param resourceDescription description of the resource that the bean definition came from
	 * @param beanName the name of the bean requested
	 * @param propertyName the name of the bean property that couldn't be satisfied
	 * @param msg the detail message
	 */
	public UnsatisfiedDependencyException(
			@Nullable String resourceDescription, @Nullable String beanName, String propertyName, String msg) {

		super(resourceDescription, beanName,
				"Unsatisfied dependency expressed through bean property '" + propertyName + "'" +
				(StringUtils.hasLength(msg) ? ": " + msg : ""));
		this.injectionPoint = null;
	}

	/**
	 * Create a new UnsatisfiedDependencyException.
	 * @param resourceDescription description of the resource that the bean definition came from
	 * @param beanName the name of the bean requested
	 * @param propertyName the name of the bean property that couldn't be satisfied
	 * @param ex the bean creation exception that indicated the unsatisfied dependency
	 */
	public UnsatisfiedDependencyException(
			@Nullable String resourceDescription, @Nullable String beanName, String propertyName, BeansException ex) {

		this(resourceDescription, beanName, propertyName, "");
		initCause(ex);
	}

	/**
	 * Create a new UnsatisfiedDependencyException.
	 * @param resourceDescription description of the resource that the bean definition came from
	 * @param beanName the name of the bean requested
	 * @param injectionPoint the injection point (field or method/constructor parameter)
	 * @param msg the detail message
	 * @since 4.3
	 */
	public UnsatisfiedDependencyException(
			@Nullable String resourceDescription, @Nullable String beanName, @Nullable InjectionPoint injectionPoint, String msg) {

		super(resourceDescription, beanName,
				"Unsatisfied dependency expressed through " + injectionPoint +
				(StringUtils.hasLength(msg) ? ": " + msg : ""));
		this.injectionPoint = injectionPoint;
	}

	/**
	 * Create a new UnsatisfiedDependencyException.
	 * @param resourceDescription description of the resource that the bean definition came from
	 * @param beanName the name of the bean requested
	 * @param injectionPoint the injection point (field or method/constructor parameter)
	 * @param ex the bean creation exception that indicated the unsatisfied dependency
	 * @since 4.3
	 */
	public UnsatisfiedDependencyException(
			@Nullable String resourceDescription, @Nullable String beanName, @Nullable InjectionPoint injectionPoint, BeansException ex) {

		this(resourceDescription, beanName, injectionPoint, "");
		initCause(ex);
	}


	/**
	 * Return the injection point (field or method/constructor parameter), if known.
	 * @since 4.3
	 */
	@Nullable
	public InjectionPoint getInjectionPoint() {
		return this.injectionPoint;
	}

}
