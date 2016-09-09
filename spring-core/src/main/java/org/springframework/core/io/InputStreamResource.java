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
import java.io.InputStream;

import org.springframework.util.Assert;

/**
 * {@link Resource} implementation for a given {@link InputStream}.
 * <p>Should only be used if no other specific {@code Resource} implementation
 * is applicable. In particular, prefer {@link ByteArrayResource} or any of the
 * file-based {@code Resource} implementations where possible.
 *
 * 给定InputStream的Resource实现，只应该在没有其它指定的可用的Resource实现的时候被使用。
 * 特别地，优先使用ByteArrayResource或任何基于文件的Resource实现。
 *
 * <p>In contrast to other {@code Resource} implementations, this is a descriptor
 * for an <i>already opened</i> resource - therefore returning {@code true} from
 * {@link #isOpen()}. Do not use an {@code InputStreamResource} if you need to
 * keep the resource descriptor somewhere, or if you need to read from a stream
 * multiple times.
 *
 * 相对于其它Resource实现，这个一个为已经打开的资源的描述符 - 因为从isOpen()中返回true。不要使用一个
 * InputStreamResource如果你需要在一些地方保存资源的描述符或如果你需要从一个流中多次读取。
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 28.12.2003
 * @see ByteArrayResource
 * @see ClassPathResource
 * @see FileSystemResource
 * @see UrlResource
 */
public class InputStreamResource extends AbstractResource {

	private final InputStream inputStream;

	private final String description;

	private boolean read = false;


	/**
	 * Create a new InputStreamResource.
	 *
	 * 创建一个新的InputStreamResource。
	 * @param inputStream the InputStream to use
	 */
	public InputStreamResource(InputStream inputStream) {
		this(inputStream, "resource loaded through InputStream");
	}

	/**
	 * Create a new InputStreamResource.
	 *
	 * 创建一个新的InputStreamResource。
	 * @param inputStream the InputStream to use
	 * @param description where the InputStream comes from
	 */
	public InputStreamResource(InputStream inputStream, String description) {
		Assert.notNull(inputStream, "InputStream must not be null");
		this.inputStream = inputStream;
		this.description = (description != null ? description : "");
	}


	/**
	 * This implementation always returns {@code true}.
	 *
	 * 这个实现总是返回true。
	 */
	@Override
	public boolean exists() {
		return true;
	}

	/**
	 * This implementation always returns {@code true}.
	 *
	 * 这个实现总是返回true。
	 */
	@Override
	public boolean isOpen() {
		return true;
	}

	/**
	 * This implementation throws IllegalStateException if attempting to
	 * read the underlying stream multiple times.
	 *
	 * 这个实现抛出IllegalStateException，如果试图多次读取底层的流。
	 */
	@Override
	public InputStream getInputStream() throws IOException, IllegalStateException {
		if (this.read) {
			throw new IllegalStateException("InputStream has already been read - " +
					"do not use InputStreamResource if a stream needs to be read multiple times");
		}
		this.read = true;
		return this.inputStream;
	}

	/**
	 * This implementation returns a description that includes the passed-in
	 * description, if any.
	 *
	 * 这个实现返回一个包含传入的描述的描述。如果有的话。
	 */
	@Override
	public String getDescription() {
		return "InputStream resource [" + this.description + "]";
	}


	/**
	 * This implementation compares the underlying InputStream.
	 *
	 * 这个实现比较底层的输入流。
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj == this ||
			(obj instanceof InputStreamResource && ((InputStreamResource) obj).inputStream.equals(this.inputStream)));
	}

	/**
	 * This implementation returns the hash code of the underlying InputStream.
	 *
	 * 这个实现返回底层输入流的hashCode。
	 */
	@Override
	public int hashCode() {
		return this.inputStream.hashCode();
	}

}
