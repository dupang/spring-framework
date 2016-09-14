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

/**
 * SPI interface allowing tools and other external processes to handle errors
 * and warnings reported during bean definition parsing.
 *
 * 允许工具和其它外部的进程来处理在bean定义解析的过程当中的错误和警告的SPI接口。
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see Problem
 */
public interface ProblemReporter {

	/**
	 * Called when a fatal error is encountered during the parsing process.
	 * <p>Implementations must treat the given problem as fatal,
	 * i.e. they have to eventually raise an exception.
	 * 在解析的过程当中遇到一个致命的错误时调用。
	 * 实现方必须对待给定的问题作为致命的，也就是说他们最终不得不抛出一个异常。
	 * @param problem the source of the error (never {@code null})
	 */
	void fatal(Problem problem);

	/**
	 * Called when an error is encountered during the parsing process.
	 * <p>Implementations may choose to treat errors as fatal.
	 *
	 * 在解析的过程当中遇到一个错误时调用。
	 * 实现方可以选择作为致命的来对待这个错误。
	 * @param problem the source of the error (never {@code null})
	 */
	void error(Problem problem);

	/**
	 * Called when a warning is raised during the parsing process.
	 * <p>Warnings are <strong>never</strong> considered to be fatal.
	 *
	 * 在解析的过程当中遇到一个警告时调用。
	 * 警告永远不会被识为严重的。
	 * @param problem the source of the warning (never {@code null})
	 */
	void warning(Problem problem);

}
