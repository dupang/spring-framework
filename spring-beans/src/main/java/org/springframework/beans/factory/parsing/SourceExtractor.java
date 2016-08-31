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

package org.springframework.beans.factory.parsing;

import org.springframework.core.io.Resource;

/**
 * Simple strategy allowing tools to control how source metadata is attached
 * to the bean definition metadata.
 *
 * 简单的策略允许工具来控制资源的元数据是怎么附加到bean定义的元数据上。
 *
 * <p>Configuration parsers <strong>may</strong> provide the ability to attach
 * source metadata during the parse phase. They will offer this metadata in a
 * generic format which can be further modified by a {@link SourceExtractor}
 * before being attached to the bean definition metadata.
 *
 * 配置的解析器可能在解析的过程中提供附加资源元数据的能力。他们将提供这个元数据以一种普遍的形式，这种形式
 * 可以被SourceExtractor进一步的修改在被附加到bean定义元数据之前。
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see org.springframework.beans.BeanMetadataElement#getSource()
 * @see org.springframework.beans.factory.config.BeanDefinition
 */
@FunctionalInterface
public interface SourceExtractor {

	/**
	 * Extract the source metadata from the candidate object supplied
	 * by the configuration parser.
	 *
	 * 从通过配置的解析器提供的候选的对象中抽取资源的元数据
	 *
	 * @param sourceCandidate the original source metadata (never {@code null})
	 * @param definingResource the resource that defines the given source object
	 * (may be {@code null})
	 * @return the source metadata object to store (may be {@code null})
	 */
	Object extractSource(Object sourceCandidate, Resource definingResource);

}
