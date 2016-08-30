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

package org.springframework.core.io.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Holder that combines a {@link Resource} descriptor with a specific encoding
 * or {@code Charset} to be used for reading from the resource.
 *
 * 给合了指定的用于从资源中读取的编码或字符集资源描述符的持有者。
 *
 * <p>Used as an argument for operations that support reading content with
 * a specific encoding, typically via a {@code java.io.Reader}.
 *
 * 作为一个参数被用于支持读取带有选定编码的内容的操作，通常通过一个java.io.Reader。
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 1.2.6
 * @see java.io.Reader
 * @see java.nio.charset.Charset
 */
public class EncodedResource implements InputStreamSource {

	private final Resource resource;

	private final String encoding;

	private final Charset charset;


	/**
	 * Create a new {@code EncodedResource} for the given {@code Resource},
	 * not specifying an explicit encoding or {@code Charset}.
	 * 为给定的资源创建一个新的EncodedResource，不显式指定一个编码或字符集。
	 * @param resource the {@code Resource} to hold (never {@code null})
	 */
	public EncodedResource(Resource resource) {
		this(resource, null, null);
	}

	/**
	 * Create a new {@code EncodedResource} for the given {@code Resource},
	 * using the specified {@code encoding}.
	 * 用指定的编码为给定的资源创建一个新的EncodedResource。
	 * @param resource the {@code Resource} to hold (never {@code null})
	 * @param encoding the encoding to use for reading from the resource
	 */
	public EncodedResource(Resource resource, String encoding) {
		this(resource, encoding, null);
	}

	/**
	 * Create a new {@code EncodedResource} for the given {@code Resource},
	 * using the specified {@code Charset}.
	 * 用指定的字符集为给定的资源创建一个新的EncodedResource。
	 * @param resource the {@code Resource} to hold (never {@code null})
	 * @param charset the {@code Charset} to use for reading from the resource
	 */
	public EncodedResource(Resource resource, Charset charset) {
		this(resource, null, charset);
	}

	private EncodedResource(Resource resource, String encoding, Charset charset) {
		super();
		Assert.notNull(resource, "Resource must not be null");
		this.resource = resource;
		this.encoding = encoding;
		this.charset = charset;
	}


	/**
	 * Return the {@code Resource} held by this {@code EncodedResource}.
	 * 返回被这个 EncodedResource持有的资源。
	 */
	public final Resource getResource() {
		return this.resource;
	}

	/**
	 * Return the encoding to use for reading from the {@linkplain #getResource() resource},
	 * or {@code null} if none specified.
	 * 返回用于从资源读取的编码。或者null如果没有指定。
	 */
	public final String getEncoding() {
		return this.encoding;
	}

	/**
	 * Return the {@code Charset} to use for reading from the {@linkplain #getResource() resource},
	 * or {@code null} if none specified.
	 * 返回用于从资源读取的字符。或者null如果没有指定。
	 */
	public final Charset getCharset() {
		return this.charset;
	}

	/**
	 * Determine whether a {@link Reader} is required as opposed to an {@link InputStream},
	 * i.e. whether an {@linkplain #getEncoding() encoding} or a {@link #getCharset() Charset}
	 * has been specified.
	 * 判断是否需要一个Reader相对于一个InputStream，也就是说。是否指定了一个编码或字符集。
	 * @see #getReader()
	 * @see #getInputStream()
	 */
	public boolean requiresReader() {
		return (this.encoding != null || this.charset != null);
	}

	/**
	 * Open a {@code java.io.Reader} for the specified resource, using the specified
	 * {@link #getCharset() Charset} or {@linkplain #getEncoding() encoding}
	 * (if any).
	 * 为指定的资源打开一个Reader,使用指定的字符集或编码。
	 * @throws IOException if opening the Reader failed
	 * @see #requiresReader()
	 * @see #getInputStream()
	 */
	public Reader getReader() throws IOException {
		if (this.charset != null) {
			return new InputStreamReader(this.resource.getInputStream(), this.charset);
		}
		else if (this.encoding != null) {
			return new InputStreamReader(this.resource.getInputStream(), this.encoding);
		}
		else {
			return new InputStreamReader(this.resource.getInputStream());
		}
	}

	/**
	 * Open a {@code java.io.InputStream} for the specified resource, ignoring any
	 * specified {@link #getCharset() Charset} or {@linkplain #getEncoding() encoding}.
	 * @throws IOException if opening the InputStream failed
	 * @see #requiresReader()
	 * @see #getReader()
	 * 为指定的资源打开一个InputStream，忽略任何指定的编码或字符。抛出IOException如果打开InputStream失败。
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return this.resource.getInputStream();
	}


	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EncodedResource)) {
			return false;
		}
		EncodedResource otherResource = (EncodedResource) other;
		return (this.resource.equals(otherResource.resource) &&
				ObjectUtils.nullSafeEquals(this.charset, otherResource.charset) &&
				ObjectUtils.nullSafeEquals(this.encoding, otherResource.encoding));
	}

	@Override
	public int hashCode() {
		return this.resource.hashCode();
	}

	@Override
	public String toString() {
		return this.resource.toString();
	}

}
