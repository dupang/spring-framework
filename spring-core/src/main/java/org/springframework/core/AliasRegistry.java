/*
 * Copyright 2002-2015 the original author or authors.
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

package org.springframework.core;

/**
 * Common interface for managing aliases. Serves as super-interface for
 * {@link org.springframework.beans.factory.support.BeanDefinitionRegistry}.
 *
 * 管理别名的通用接口。作为BeanDefinitionRegistry类的超级接口
 *
 * @author Juergen Hoeller
 * @since 2.5.2
 */
public interface AliasRegistry {

	/**
	 * Given a name, register an alias for it.
	 *
	 * 对于一个给定的名字，给它注册一个别名
	 *
	 * @param name the canonical name
	 * @param alias the alias to be registered
	 * @throws IllegalStateException if the alias is already in use
	 * and may not be overridden
	 *         如果别名已经正在使用并且可能不能被覆盖就会抛出IllegalStateException
	 */
	void registerAlias(String name, String alias);

	/**
	 * Remove the specified alias from this registry.
	 * 从这个注册器中删除指定的别名
	 * @param alias the alias to remove
	 * @throws IllegalStateException if no such alias was found
	 *         如果没有找出这个别名就会抛出IllegalStateException
	 */
	void removeAlias(String alias);

	/**
	 * Determine whether this given name is defines as an alias
	 * (as opposed to the name of an actually registered component).
	 * 判断这个给定的名字是否被定义为别名(而不是一个实际注册组件的名称)
	 * @param name the name to check
	 * @return whether the given name is an alias
	 */
	boolean isAlias(String name);

	/**
	 * Return the aliases for the given name, if defined.
	 * 返回给定名字的别名，如果定义的话。
	 * @param name the name to check for aliases
	 * @return the aliases, or an empty array if none
	 *         别名的数组，或者空数组如果没有。
	 */
	String[] getAliases(String name);

}
