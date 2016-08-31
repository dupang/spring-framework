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
 * Simple implementation of {@link SourceExtractor} that returns {@code null}
 * as the source metadata.
 * SourceExtractor的简单实现，它总是返回null作为资源的元数据。
 *
 * <p>This is the default implementation and prevents too much metadata from being
 * held in memory during normal (non-tooled) runtime usage.
 *
 * 这是默认的实现和防止太多的元数据被持有在内存中在普通的运行时使用。
 *
 * @author Rob Harrop
 * @since 2.0
 */
public class NullSourceExtractor implements SourceExtractor {

	/**
	 * This implementation simply returns {@code null} for any input.
	 * 这个实现简单地返回null为任何输入。
	 */
	@Override
	public Object extractSource(Object sourceCandidate, Resource definitionResource) {
		return null;
	}

}
