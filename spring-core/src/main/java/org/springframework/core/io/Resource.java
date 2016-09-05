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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * Interface for a resource descriptor that abstracts from the actual
 * type of underlying resource, such as a file or class path resource.
 *
 * 抽象真实的底层的资源的资源描述符的接口，例如一个文件或类路径资源。
 *
 * <p>An InputStream can be opened for every resource if it exists in
 * physical form, but a URL or File handle can just be returned for
 * certain resources. The actual behavior is implementation-specific.
 *
 * 一个InputStream可以被打开为每一个资源，如果它以物理形式存在，但是一个URL或文件句柄
 * 仅仅可以作为特定的资源返回。真实的行为是实现相关的。
 *
 * @author Juergen Hoeller
 * @since 28.12.2003
 * @see #getInputStream()
 * @see #getURL()
 * @see #getURI()
 * @see #getFile()
 * @see WritableResource
 * @see ContextResource
 * @see UrlResource
 * @see ClassPathResource
 * @see FileSystemResource
 * @see PathResource
 * @see ByteArrayResource
 * @see InputStreamResource
 */
public interface Resource extends InputStreamSource {

	/**
	 * Determine whether this resource actually exists in physical form.
	 * <p>This method performs a definitive existence check, whereas the
	 * existence of a {@code Resource} handle only guarantees a valid
	 * descriptor handle.
	 *
	 * 判断是否这个资源真正地以物理形式存在。
	 * 这个方法执行一个明确的存在检查，而Resource句柄的存在性只保证一个有效的描述符句柄。
	 */
	boolean exists();

	/**
	 * Indicate whether the contents of this resource can be read via
	 * {@link #getInputStream()}.
	 * <p>Will be {@code true} for typical resource descriptors;
	 * note that actual content reading may still fail when attempted.
	 * However, a value of {@code false} is a definitive indication
	 * that the resource content cannot be read.
	 * @see #getInputStream()
	 *
	 * 表示是否这个资源可以通过getInputStream读取。
	 * 对于普通的资源描述符将会是true;
	 * 注意真正的内容读可能仍然失败当试图读的时候。
	 * 然而，一个false的值是一个明确的表示资源内容不能被读。
	 */
	default boolean isReadable() {
		return true;
	}

	/**
	 * Indicate whether this resource represents a handle with an open stream.
	 * If {@code true}, the InputStream cannot be read multiple times,
	 * and must be read and closed to avoid resource leaks.
	 * <p>Will be {@code false} for typical resource descriptors.
	 * 表示是否这个资源表示一个带有打开流的句柄。
	 * 如果true,InputStreamg不能多次读取，
	 * 并且必须读和关闭来避免资源泄漏。
	 * 对于典型的资源描述符将是false。
	 */
	default boolean isOpen() {
		return false;
	}

	/**
	 * Determine whether this resource represents a file in a file system.
	 * A value of {@code true} strongly suggests (but does not guarantee)
	 * that a {@link #getFile()} call will succeed.
	 * <p>This is conservatively {@code false} by default.
	 * 判断是否这个资源表示一个文件系统中的文件。
	 * 一个true的值强烈建议(但是不保证)getFile()调用将成功。
	 * 默认情况下这总是false。
	 * @since 5.0
	 * @see #getFile()
	 */
	default boolean isFile() {
		return false;
	}

	/**
	 * Return a URL handle for this resource.
	 * 返回这个资源的URL句柄。
	 * @throws IOException if the resource cannot be resolved as URL,
	 * i.e. if the resource is not available as descriptor
	 */
	URL getURL() throws IOException;

	/**
	 * Return a URI handle for this resource.
	 * 返回这个资源的URI句柄。
	 * @throws IOException if the resource cannot be resolved as URI,
	 * i.e. if the resource is not available as descriptor
	 * @since 2.5
	 */
	URI getURI() throws IOException;

	/**
	 * Return a File handle for this resource.
	 * 返回这个资源的文件句柄。
	 * @throws IOException if the resource cannot be resolved as absolute
	 * file path, i.e. if the resource is not available in a file system
	 */
	File getFile() throws IOException;

	/**
	 * Determine the content length for this resource.
	 * 确定这个资源的内容长度。
	 * @throws IOException if the resource cannot be resolved
	 * (in the file system or as some other known physical resource type)
	 */
	long contentLength() throws IOException;

	/**
	 * Determine the last-modified timestamp for this resource.
	 * 确定这个资源的最后修改时间戳。
	 * @throws IOException if the resource cannot be resolved
	 * (in the file system or as some other known physical resource type)
	 */
	long lastModified() throws IOException;

	/**
	 * Create a resource relative to this resource.
	 * 创建一个相对于这个资源的资源。
	 * @param relativePath the relative path (relative to this resource)
	 * @return the resource handle for the relative resource
	 * @throws IOException if the relative resource cannot be determined
	 */
	Resource createRelative(String relativePath) throws IOException;

	/**
	 * Determine a filename for this resource, i.e. typically the last
	 * part of the path: for example, "myfile.txt".
	 * <p>Returns {@code null} if this type of resource does not
	 * have a filename.
	 * 确定这个资源的文件名，也就是说,通常是路径的最后一部分:例如，"myfile.txt"。
	 * 如果这个资源的类型没有一个文件名，就返回null。
	 */
	String getFilename();

	/**
	 * Return a description for this resource,
	 * to be used for error output when working with the resource.
	 * <p>Implementations are also encouraged to return this value
	 * from their {@code toString} method.
	 * @see Object#toString()
	 *
	 * 返回这个资源的描述符，被用于错误输出当使用资源的时候。
	 * 实现也被鼓励从toString()方法返回这个值。
	 *
	 */
	String getDescription();

}
