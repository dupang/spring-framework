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

package org.springframework.beans.factory.parsing;

import java.util.Stack;

/**
 * Simple {@link Stack}-based structure for tracking the logical position during
 * a parsing process. {@link Entry entries} are added to the stack at
 * each point during the parse phase in a reader-specific manner.
 *
 * 简单的基于栈的结构，用来在解析的过程当中跟踪逻辑位置。条目以读的方式被加入到栈中。
 *
 * <p>Calling {@link #toString()} will render a tree-style view of the current logical
 * position in the parse phase. This representation is intended for use in
 * error messages.
 * 调用toString()将提供一个解析阶段的当前逻辑位置的树状的视图。这个表示主要用来错误信息。
 *
 * @author Rob Harrop
 * @since 2.0
 */
public final class ParseState {

	/**
	 * Tab character used when rendering the tree-style representation.
	 *
	 * 当渲染树状表示的时候用到的Tab字符。
	 */
	private static final char TAB = '\t';

	/**
	 * Internal {@link Stack} storage.
	 * 内部的Stack存储。
	 */
	private final Stack<Entry> state;


	/**
	 * Create a new {@code ParseState} with an empty {@link Stack}.
	 *
	 * 创建一个带着空的栈的新ParseState
	 */
	public ParseState() {
		this.state = new Stack<>();
	}

	/**
	 * Create a new {@code ParseState} whose {@link Stack} is a {@link Object#clone clone}
	 * of that of the passed in {@code ParseState}.
	 *
	 * 创建一个新的ParseState。它的Stack是传入的ParseState的一个克隆。
	 */
	@SuppressWarnings("unchecked")
	private ParseState(ParseState other) {
		this.state = (Stack<Entry>) other.state.clone();
	}


	/**
	 * Add a new {@link Entry} to the {@link Stack}.
	 *
	 * 往Stack里加入一个新的条目
	 */
	public void push(Entry entry) {
		this.state.push(entry);
	}

	/**
	 * Remove an {@link Entry} from the {@link Stack}.
	 *
	 * 从Stack中删除一个条目。
	 */
	public void pop() {
		this.state.pop();
	}

	/**
	 * Return the {@link Entry} currently at the top of the {@link Stack} or
	 * {@code null} if the {@link Stack} is empty.
	 *
	 * 返回当前栈顶的元素。或者如果Stack是空的话就返回null。
	 */
	public Entry peek() {
		return this.state.empty() ? null : this.state.peek();
	}

	/**
	 * Create a new instance of {@link ParseState} which is an independent snapshot
	 * of this instance.
	 *
	 * 创建一个新的ParseState实例，它是这个实例的独立的快照。
	 */
	public ParseState snapshot() {
		return new ParseState(this);
	}


	/**
	 * Returns a tree-style representation of the current {@code ParseState}.
	 *
	 * 返回一个当前ParseState的树状表示。
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < this.state.size(); x++) {
			if (x > 0) {
				sb.append('\n');
				for (int y = 0; y < x; y++) {
					sb.append(TAB);
				}
				sb.append("-> ");
			}
			sb.append(this.state.get(x));
		}
		return sb.toString();
	}


	/**
	 * Marker interface for entries into the {@link ParseState}.
	 *
	 * 进入ParseState的条目的标记接口
	 */
	public interface Entry {

	}

}
