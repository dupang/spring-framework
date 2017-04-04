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

/**
 * Simple interface for objects that are sources for an {@link InputStream}.
 *
 * 数据源是InputStream的对象的简单接口。
 *
 * <p>This is the base interface for Spring's more extensive {@link Resource} interface.
 *
 * 这是Spring的更具可扩展性的Resource接口的基础接口。
 *
 * <p>For single-use streams, {@link InputStreamResource} can be used for any
 * given {@code InputStream}. Spring's {@link ByteArrayResource} or any
 * file-based {@code Resource} implementation can be used as a concrete
 * instance, allowing one to read the underlying content stream multiple times.
 * This makes this interface useful as an abstract content source for mail
 * attachments, for example.
 *
 * 对于一次性使用的流，InputStreamResource可以被任何给定的InputStream使用。Spring的ByteArrayResource
 * 或任何基于文件的Resource实现可以被用来作为一个具体的实例，允许你来多次读底层的内容流。
 * 例如，这使这个接口作为一个邮件附件的抽象内容来源很有用。
 * @author Juergen Hoeller
 * @since 20.01.2004
 * @see java.io.InputStream
 * @see Resource
 * @see InputStreamResource
 * @see ByteArrayResource
 */
public interface InputStreamSource {

	/**
	 * Return an {@link InputStream}.
	 * <p>It is expected that each call creates a <i>fresh</i> stream.
	 * <p>This requirement is particularly important when you consider an API such
	 * as JavaMail, which needs to be able to read the stream multiple times when
	 * creating mail attachments. For such a use case, it is <i>required</i>
	 * that each {@code getInputStream()} call returns a fresh stream.
	 * 返回一个InputStream。
	 * 期望每一个调用创建一个新的流。
	 * 当你考虑一个像JavaMail的API的时候这个要求很重要，当创建邮件附件的时候它需要可以多次读取流。
	 * 对于这样的使用例子，它要求每一次getInputStream调用返回一个新的流。
	 *
	 * @return the input stream for the underlying resource (must not be {@code null})
	 * @throws IOException if the stream could not be opened
	 */
	InputStream getInputStream() throws IOException;

}
