/*
 * Copyright 2002-2012 the original author or authors.
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
 * Simple {@link SourceExtractor} implementation that just passes
 * the candidate source metadata object through for attachment.
 *
 * 简单的SourceExtractor实现只是传递候选的资源元数据对象用于附加。
 *
 * <p>Using this implementation means that tools will get raw access to the
 * underlying configuration source metadata provided by the tool.
 *
 * 使用这个实现意为着工具将得到通过这个工具提供的原始未经加工的配置资源元数据，
 *
 * <p>This implementation <strong>should not</strong> be used in a production
 * application since it is likely to keep too much metadata in memory
 * (unnecessarily).
 *
 * 这个实现不应该用在生产应用当中，因为它可能持有太多的元数据在内在中(不必要地)。
 *
 * @author Rob Harrop
 * @since 2.0
 */
public class PassThroughSourceExtractor implements SourceExtractor {

	/**
	 * Simply returns the supplied {@code sourceCandidate} as-is.
	 * 简单地返回提供的sourceCandidate。
	 * @param sourceCandidate the source metadata
	 * @return the supplied {@code sourceCandidate}
	 */
	@Override
	public Object extractSource(Object sourceCandidate, Resource definingResource) {
		return sourceCandidate;
	}

}
