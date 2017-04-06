/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.support;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

/**
 * Object representing the override of a method on a managed object by the IoC
 * container.
 *
 * 表示被Ioc容器管理的对象方法覆盖的对象。
 *
 * <p>Note that the override mechanism is <em>not</em> intended as a generic
 * means of inserting crosscutting code: use AOP for that.
 *
 * 注意覆盖机制不想作为一个插入横切代码的通常的方法。
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 1.1
 */
public abstract class MethodOverride implements BeanMetadataElement {

	private final String methodName;

	private boolean overloaded = true;

	private Object source;


	/**
	 * Construct a new override for the given method.
	 *
	 * 为指定的方法构建一个新的MethodOverride。
	 * @param methodName the name of the method to override
	 */
	protected MethodOverride(String methodName) {
		Assert.notNull(methodName, "Method name must not be null");
		this.methodName = methodName;
	}


	/**
	 * Return the name of the method to be overridden.
	 *
	 * 返回将被覆盖的方法的名称
	 */
	public String getMethodName() {
		return this.methodName;
	}

	/**
	 * Set whether the overridden method is <em>overloaded</em> (i.e., whether argument
	 * type matching needs to occur to disambiguate methods of the same name).
	 * <p>Default is {@code true}; can be switched to {@code false} to optimize
	 * runtime performance.
	 *
	 * 设置被覆盖的方法是否被重载了.(也就是说，是否参数类型匹配需要来辨别相同名称的方法)。
	 * 默认是true;可心被切换为false来优化运行时性能。
	 */
	protected void setOverloaded(boolean overloaded) {
		this.overloaded = overloaded;
	}

	/**
	 * Return whether the overridden method is <em>overloaded</em> (i.e., whether argument
	 * type matching needs to occur to disambiguate methods of the same name).
	 */
	protected boolean isOverloaded() {
		return this.overloaded;
	}

	/**
	 * Set the configuration source {@code Object} for this metadata element.
	 * <p>The exact type of the object will depend on the configuration mechanism used.
	 *
	 * 为这个元数据元素设置配置源对象。这个对象的具体类型将依赖使用的配置机制。
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}

	/**
	 * Subclasses must override this to indicate whether they <em>match</em> the
	 * given method. This allows for argument list checking as well as method
	 * name checking.
	 *
	 * 字类必需要重载这个来表示是否他们匹配给定的方法。这允许参数列表检查和方法名检查。
	 * @param method the method to check
	 * @return whether this override matches the given method
	 */
	public abstract boolean matches(Method method);


	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MethodOverride)) {
			return false;
		}
		MethodOverride that = (MethodOverride) other;
		return (ObjectUtils.nullSafeEquals(this.methodName, that.methodName) &&
				ObjectUtils.nullSafeEquals(this.source, that.source));
	}

	@Override
	public int hashCode() {
		int hashCode = ObjectUtils.nullSafeHashCode(this.methodName);
		hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.source);
		return hashCode;
	}

}
