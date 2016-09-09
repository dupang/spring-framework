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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.util.Assert;

/**
 * {@link Resource} implementation for {@code java.nio.file.Path} handles.
 * Supports resolution as File, and also as URL.
 * Implements the extended {@link WritableResource} interface.
 *
 * java.nio.file.Path句柄的资源实现。
 * 支持作为文件和URL的解析。
 * 实现的扩展的WritableResource接口。
 *
 * @author Philippe Marschall
 * @author Juergen Hoeller
 * @since 4.0
 * @see java.nio.file.Path
 */
public class PathResource extends AbstractResource implements WritableResource {

	private final Path path;


	/**
	 * Create a new PathResource from a Path handle.
	 * <p>Note: Unlike {@link FileSystemResource}, when building relative resources
	 * via {@link #createRelative}, the relative path will be built <i>underneath</i>
	 * the given root:
	 * e.g. Paths.get("C:/dir1/"), relative path "dir2" -> "C:/dir1/dir2"!
	 *
	 * 从一个路径句柄创建一个新的PathResource。
	 * 注意:不像FileSystemResource，当通过createRelative构造相对资源，相对路径将被构造在给定的根路径下面。
	 * 例如，Paths.get("C:/dir1/")，相对的路径"dir2" -> "C:/dir1/dir2"!
	 * @param path a Path handle
	 */
	public PathResource(Path path) {
		Assert.notNull(path, "Path must not be null");
		this.path = path.normalize();
	}

	/**
	 * Create a new PathResource from a Path handle.
	 * <p>Note: Unlike {@link FileSystemResource}, when building relative resources
	 * via {@link #createRelative}, the relative path will be built <i>underneath</i>
	 * the given root:
	 * e.g. Paths.get("C:/dir1/"), relative path "dir2" -> "C:/dir1/dir2"!
	 *
	 * 从一个路径句柄创建一个新的PathResource。
	 * 注意:不像FileSystemResource，当通过createRelative构造相对资源，相对路径将被构造在给定的根路径下面。
	 * 例如，Paths.get("C:/dir1/")，相对的路径"dir2" -> "C:/dir1/dir2"!
	 *
	 * @param path a path
	 * @see java.nio.file.Paths#get(String, String...)
	 */
	public PathResource(String path) {
		Assert.notNull(path, "Path must not be null");
		this.path = Paths.get(path).normalize();
	}

	/**
	 * Create a new PathResource from a Path handle.
	 * <p>Note: Unlike {@link FileSystemResource}, when building relative resources
	 * via {@link #createRelative}, the relative path will be built <i>underneath</i>
	 * the given root:
	 * e.g. Paths.get("C:/dir1/"), relative path "dir2" -> "C:/dir1/dir2"!
	 * @see java.nio.file.Paths#get(URI)
	 *
	 * 从一个路径句柄创建一个新的PathResource。
	 * 注意:不像FileSystemResource，当通过createRelative构造相对资源，相对路径将被构造在给定的根路径下面。
	 * 例如，Paths.get("C:/dir1/")，相对的路径"dir2" -> "C:/dir1/dir2"!
	 *
	 * @param uri a path URI
	 */
	public PathResource(URI uri) {
		Assert.notNull(uri, "URI must not be null");
		this.path = Paths.get(uri).normalize();
	}


	/**
	 * Return the file path for this resource.
	 *
	 * 返回这个资源的文件路径。
	 */
	public final String getPath() {
		return this.path.toString();
	}

	/**
	 * This implementation returns whether the underlying file exists.
	 * @see org.springframework.core.io.PathResource#exists()
	 *
	 * 这个实现返回是否底层的文件存在。
	 */
	@Override
	public boolean exists() {
		return Files.exists(this.path);
	}

	/**
	 * This implementation checks whether the underlying file is marked as readable
	 * (and corresponds to an actual file with content, not to a directory).
	 * @see java.nio.file.Files#isReadable(Path)
	 * @see java.nio.file.Files#isDirectory(Path, java.nio.file.LinkOption...)
	 *
	 * 这个实现检查是否底层的文件被标记为可读的(对就于带着内容的真实文件，而不是一个目录)。
	 */
	@Override
	public boolean isReadable() {
		return (Files.isReadable(this.path) && !Files.isDirectory(this.path));
	}

	/**
	 * This implementation opens a InputStream for the underlying file.
	 * @see java.nio.file.spi.FileSystemProvider#newInputStream(Path, OpenOption...)
	 *
	 * 这个实现打开底层文件的InuptStream。
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		if (!exists()) {
			throw new FileNotFoundException(getPath() + " (no such file or directory)");
		}
		if (Files.isDirectory(this.path)) {
			throw new FileNotFoundException(getPath() + " (is a directory)");
		}
		return Files.newInputStream(this.path);
	}

	/**
	 * This implementation checks whether the underlying file is marked as writable
	 * (and corresponds to an actual file with content, not to a directory).
	 * @see java.nio.file.Files#isWritable(Path)
	 * @see java.nio.file.Files#isDirectory(Path, java.nio.file.LinkOption...)
	 *
	 * 这个实现检查是否底层的文件被标记为可写的(对就于带着内容的真实文件，而不是一个目录)。
	 */
	@Override
	public boolean isWritable() {
		return (Files.isWritable(this.path) && !Files.isDirectory(this.path));
	}

	/**
	 * This implementation opens a OutputStream for the underlying file.
	 * @see java.nio.file.spi.FileSystemProvider#newOutputStream(Path, OpenOption...)
	 *
	 * 这个实现打开底层文件的OutputStream。
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		if (Files.isDirectory(this.path)) {
			throw new FileNotFoundException(getPath() + " (is a directory)");
		}
		return Files.newOutputStream(this.path);
	}

	/**
	 * This implementation returns a URL for the underlying file.
	 * @see java.nio.file.Path#toUri()
	 * @see java.net.URI#toURL()
	 *
	 * 这个实现为底层的文件返回一个URL。
	 */
	@Override
	public URL getURL() throws IOException {
		return this.path.toUri().toURL();
	}

	/**
	 * This implementation returns a URI for the underlying file.
	 * @see java.nio.file.Path#toUri()
	 *
	 * 这个实现为底层的文件返回一个URI。
	 */
	@Override
	public URI getURI() throws IOException {
		return this.path.toUri();
	}

	/**
	 * This implementation always indicates a file.
	 *
	 * 这个实现总是表示一个文件。
	 */
	@Override
	public boolean isFile() {
		return true;
	}

	/**
	 * This implementation returns the underlying File reference.、
	 *
	 * 这个实现返回底层的文件引用。
	 */
	@Override
	public File getFile() throws IOException {
		try {
			return this.path.toFile();
		}
		catch (UnsupportedOperationException ex) {
			// Only paths on the default file system can be converted to a File:
			// Do exception translation for cases where conversion is not possible.
			throw new FileNotFoundException(this.path + " cannot be resolved to " + "absolute file path");
		}
	}

	/**
	 * This implementation returns the underlying File's length.
	 *
	 * 这个实现返回底层文件的长度。
	 */
	@Override
	public long contentLength() throws IOException {
		return Files.size(this.path);
	}

	/**
	 * This implementation returns the underlying File's timestamp.
	 * @see java.nio.file.Files#getLastModifiedTime(Path, java.nio.file.LinkOption...)
	 *
	 * 这个实现返回底层文件的时间戳。
	 */
	@Override
	public long lastModified() throws IOException {
		// We can not use the superclass method since it uses conversion to a File and
		// only a Path on the default file system can be converted to a File...
		return Files.getLastModifiedTime(path).toMillis();
	}

	/**
	 * This implementation creates a FileResource, applying the given path
	 * relative to the path of the underlying file of this resource descriptor.
	 * @see java.nio.file.Path#resolve(String)
	 *
	 * 这个实现创建一个FileResource,应用相对于这个资源描述符的底层文件的路径的路径。
	 */
	@Override
	public Resource createRelative(String relativePath) throws IOException {
		return new PathResource(this.path.resolve(relativePath));
	}

	/**
	 * This implementation returns the name of the file.
	 * @see java.nio.file.Path#getFileName()
	 *
	 * 这个实现返回文件的名字。
	 */
	@Override
	public String getFilename() {
		return this.path.getFileName().toString();
	}

	@Override
	public String getDescription() {
		return "path [" + this.path.toAbsolutePath() + "]";
	}


	/**
	 * This implementation compares the underlying Path references.
	 *
	 * 这个实现比较底层的路径引用。
	 */
	@Override
	public boolean equals(Object obj) {
		return (this == obj ||
			(obj instanceof PathResource && this.path.equals(((PathResource) obj).path)));
	}

	/**
	 * This implementation returns the hash code of the underlying Path reference.
	 *
	 * 这个实现返回底层路径引用的hashCode。
	 */
	@Override
	public int hashCode() {
		return this.path.hashCode();
	}

}
