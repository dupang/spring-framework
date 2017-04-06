/*
 * Copyright 2002-2016 the original author or authors.
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
import org.springframework.beans.Mergeable;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Tag collection class used to hold managed Set values, which may
 * include runtime bean references (to be resolved into bean objects).
 *
 * 用来持有管理的Set值的标签集合，可能包含运行时bean引用(将被解析为bean对象).
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 21.01.2004
 */
@SuppressWarnings("serial")
public class ManagedSet<E> extends LinkedHashSet<E> implements Mergeable, BeanMetadataElement {

	private Object source;

	private String elementTypeName;

	private boolean mergeEnabled;


	public ManagedSet() {
	}

	public ManagedSet(int initialCapacity) {
		super(initialCapacity);
	}


	/**
	 * Set the configuration source {@code Object} for this metadata element.
	 * <p>The exact type of the object will depend on the configuration mechanism used.
	 *
	 * 设置为这个元数据元素的配置源对象。
	 * 对象的具体类型将依赖使用的配置机制。
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}

	/**
	 * Set the default element type name (class name) to be used for this set.
	 * 设置这个set的被使用的默认元素类型名称。
	 */
	public void setElementTypeName(String elementTypeName) {
		this.elementTypeName = elementTypeName;
	}

	/**
	 * Return the default element type name (class name) to be used for this set.
	 *
	 * 返回这个set的被使用的默认元素类型名称。
	 */
	public String getElementTypeName() {
		return this.elementTypeName;
	}

	/**
	 * Set whether merging should be enabled for this collection,
	 * in case of a 'parent' collection value being present.
	 *
	 * 设置是否合并应该被启用为这个集合，在'父'集合出现的时候。
	 */
	public void setMergeEnabled(boolean mergeEnabled) {
		this.mergeEnabled = mergeEnabled;
	}

	@Override
	public boolean isMergeEnabled() {
		return this.mergeEnabled;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<E> merge(Object parent) {
		if (!this.mergeEnabled) {
			throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
		}
		if (parent == null) {
			return this;
		}
		if (!(parent instanceof Set)) {
			throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
		}
		Set<E> merged = new ManagedSet<>();
		merged.addAll((Set<E>) parent);
		merged.addAll(this);
		return merged;
	}

}
