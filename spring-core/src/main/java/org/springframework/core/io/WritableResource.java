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

package org.springframework.core.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Extended interface for a resource that supports writing to it.
 * Provides an {@link #getOutputStream() OutputStream accessor}.
 *
 * 支持在资源进行写操作的扩展接口。
 * 提供一个getOutputStream() 访问器。
 *
 * @author Juergen Hoeller
 * @since 3.1
 * @see java.io.OutputStream
 */
public interface WritableResource extends Resource {

	/**
	 * Indicate whether the contents of this resource can be written
	 * via {@link #getOutputStream()}.
	 * <p>Will be {@code true} for typical resource descriptors;
	 * note that actual content writing may still fail when attempted.
	 * However, a value of {@code false} is a definitive indication
	 * that the resource content cannot be modified.
	 * @see #getOutputStream()
	 * @see #isReadable()
	 *
	 * 表示这个资源的内容是否可以通过getOutputStream()被写。
	 * 对典型的资源描述符将是true;
	 *
	 * 注意当试图真正的内容写的时候可能仍然失败。
	 * 然而，false的值是一个确定的表示这个资源不能被修改。
	 *
	 */
	default boolean isWritable() {
		return true;
	}

	/**
	 * Return an {@link OutputStream} for the underlying resource,
	 * allowing to (over-)write its content.
	 * @throws IOException if the stream could not be opened
	 * @see #getInputStream()
	 *
	 * 为底层的资源返回一个OutputStream，
	 * 允许覆盖它的内容。
	 */
	OutputStream getOutputStream() throws IOException;

}
