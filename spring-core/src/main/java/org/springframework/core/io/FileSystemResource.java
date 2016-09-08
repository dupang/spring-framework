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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * {@link Resource} implementation for {@code java.io.File} handles.
 * Supports resolution as a {@code File} and also as a {@code URL}.
 * Implements the extended {@link WritableResource} interface.
 *
 * 为java.io.File句柄的Resource实现。
 * 支持作为文件和URL的解析。
 * 实现的扩展的WritableResource接口。
 *
 * @author Juergen Hoeller
 * @since 28.12.2003
 * @see java.io.File
 */
public class FileSystemResource extends AbstractResource implements WritableResource {

	private final File file;

	private final String path;


	/**
	 * Create a new {@code FileSystemResource} from a {@link File} handle.
	 * <p>Note: When building relative resources via {@link #createRelative},
	 * the relative path will apply <i>at the same directory level</i>:
	 * e.g. new File("C:/dir1"), relative path "dir2" -> "C:/dir2"!
	 * If you prefer to have relative paths built underneath the given root
	 * directory, use the {@link #FileSystemResource(String) constructor with a file path}
	 * to append a trailing slash to the root path: "C:/dir1/", which
	 * indicates this directory as root for all relative paths.
	 *
	 * 从一个文件句柄创建一个新的FileSystemResource。
	 * 注意：当通过createRelative构造相对的资源时，相对路径将应用到相同的目录层次:例如，new File("C:/dir1"),
	 * 相对路径"dir2" -> "C:/dir2"!
	 * 如果你喜欢用相对路径构造底层的给定根目录，使用FileSystemResource(String)来增加一个斜线到根路径:"C:/dir1/",
	 * 它表示这个目录作为所有相对路径的根。
	 *
	 * @param file a File handle
	 */
	public FileSystemResource(File file) {
		Assert.notNull(file, "File must not be null");
		this.file = file;
		this.path = StringUtils.cleanPath(file.getPath());
	}

	/**
	 * Create a new {@code FileSystemResource} from a file path.
	 * <p>Note: When building relative resources via {@link #createRelative},
	 * it makes a difference whether the specified resource base path here
	 * ends with a slash or not. In the case of "C:/dir1/", relative paths
	 * will be built underneath that root: e.g. relative path "dir2" ->
	 * "C:/dir1/dir2". In the case of "C:/dir1", relative paths will apply
	 * at the same directory level: relative path "dir2" -> "C:/dir2".
	 *
	 * 从一个文件路径创建一个新的FileSystemResource。
	 * 注意:当通过createRelative创建相对的资源，这里指定的根路径是否以斜线结尾是不同的。
	 * 当是"C:/dir1/"的情况下，相对路径将在根目录下被构建:例如相对路径"dir2" -> "C:/dir1/dir2"。
	 * 当是"C:/dir1"的情况下，相对路径将被应用到相同的目录层级:相对路径"dir2" -> "C:/dir2"。
	 * @param path a file path
	 */
	public FileSystemResource(String path) {
		Assert.notNull(path, "Path must not be null");
		this.file = new File(path);
		this.path = StringUtils.cleanPath(path);
	}


	/**
	 * Return the file path for this resource.
	 *
	 * 返回这个资源的文件路径。
	 */
	public final String getPath() {
		return this.path;
	}

	/**
	 * This implementation returns whether the underlying file exists.
	 * @see java.io.File#exists()
	 *
	 * 这个实现返回是否底层的文件存在。
	 *
	 */
	@Override
	public boolean exists() {
		return this.file.exists();
	}

	/**
	 * This implementation checks whether the underlying file is marked as readable
	 * (and corresponds to an actual file with content, not to a directory).
	 * @see java.io.File#canRead()
	 * @see java.io.File#isDirectory()
	 *
	 * 这个实现检查是否底层的文件被标记为可读的(对应于一个真实的有内容的文件，不是一个目录)。
	 */
	@Override
	public boolean isReadable() {
		return (this.file.canRead() && !this.file.isDirectory());
	}

	/**
	 * This implementation opens a FileInputStream for the underlying file.
	 * @see java.io.FileInputStream
	 *
	 * 这个实现为底层的文件打开一个FileInputStream。
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(this.file);
	}

	/**
	 * This implementation checks whether the underlying file is marked as writable
	 * (and corresponds to an actual file with content, not to a directory).
	 * @see java.io.File#canWrite()
	 * @see java.io.File#isDirectory()
	 *
	 * 这个实现检查是否底层的文件被标记为可写的(对应于一个真实的有内容的文件，不是一个目录)。
	 */
	@Override
	public boolean isWritable() {
		return (this.file.canWrite() && !this.file.isDirectory());
	}

	/**
	 * This implementation opens a FileOutputStream for the underlying file.
	 * @see java.io.FileOutputStream
	 *
	 * 这个实现为底层的文件打开一个FileOutputStream。
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		return new FileOutputStream(this.file);
	}

	/**
	 * This implementation returns a URL for the underlying file.
	 * @see java.io.File#toURI()
	 *
	 * 这个实现为底层的文件返回一个URL。
	 */
	@Override
	public URL getURL() throws IOException {
		return this.file.toURI().toURL();
	}

	/**
	 * This implementation returns a URI for the underlying file.
	 * @see java.io.File#toURI()
	 *
	 * 这个实现为底层的文件返回一个URI。
	 */
	@Override
	public URI getURI() throws IOException {
		return this.file.toURI();
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
	 * This implementation returns the underlying File reference.
	 *
	 * 这个实现返回底层的文件引用。
	 */
	@Override
	public File getFile() {
		return this.file;
	}

	/**
	 * This implementation returns the underlying File's length.
	 *
	 * 这个实现返回底层文件的长度。
	 */
	@Override
	public long contentLength() throws IOException {
		return this.file.length();
	}

	/**
	 * This implementation creates a FileSystemResource, applying the given path
	 * relative to the path of the underlying file of this resource descriptor.
	 * @see org.springframework.util.StringUtils#applyRelativePath(String, String)
	 *
	 * 这个实现创建一个FileSystemResource，应用到相对于这个资源描述符的底层文件的路径的路径。
	 *
	 */
	@Override
	public Resource createRelative(String relativePath) {
		String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
		return new FileSystemResource(pathToUse);
	}

	/**
	 * This implementation returns the name of the file.
	 * @see java.io.File#getName()
	 *
	 * 这个实现返回文件的名字。
	 */
	@Override
	public String getFilename() {
		return this.file.getName();
	}

	/**
	 * This implementation returns a description that includes the absolute
	 * path of the file.
	 * @see java.io.File#getAbsolutePath()
	 *
	 * 这个实现返回包含这个文件绝对路径的描述符。
	 */
	@Override
	public String getDescription() {
		return "file [" + this.file.getAbsolutePath() + "]";
	}


	/**
	 * This implementation compares the underlying File references.
	 *
	 * 这个实现比较底层文件的引用。
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj == this ||
			(obj instanceof FileSystemResource && this.path.equals(((FileSystemResource) obj).path)));
	}

	/**
	 * This implementation returns the hash code of the underlying File reference.
	 *
	 * 这个实现舞台底层文件引用的hashCode。
	 */
	@Override
	public int hashCode() {
		return this.path.hashCode();
	}

}
