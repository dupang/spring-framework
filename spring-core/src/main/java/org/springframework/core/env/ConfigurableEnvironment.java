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

package org.springframework.core.env;

import java.util.Map;

/**
 * Configuration interface to be implemented by most if not all {@link Environment} types.
 * Provides facilities for setting active and default profiles and manipulating underlying
 * property sources. Allows clients to set and validate required properties, customize the
 * conversion service and more through the {@link ConfigurablePropertyResolver}
 * superinterface.
 *
 * 将被大部分如果不是所有的Environment类型实现的配置接口。提供工具来设置活跃和默认的profile并且维护底层的
 * 属性资源。允许客户端来设置和校验需要的属性，自定义转换服务和更多通过ConfigurablePropertyResolver父接口。
 *
 * <h2>Manipulating property sources</h2>
 * <p>Property sources may be removed, reordered, or replaced; and additional
 * property sources may be added using the {@link MutablePropertySources}
 * instance returned from {@link #getPropertySources()}. The following examples
 * are against the {@link StandardEnvironment} implementation of
 * {@code ConfigurableEnvironment}, but are generally applicable to any implementation,
 * though particular default property sources may differ.
 *
 * 维护属性源
 *
 * 属性可能被删除，重排序，或替换;并且另外的属性源可以使用被getPropertySources()返回的
 * MutablePropertySources实例被增加。
 * 下面的例子对应着ConfigurableEnvironment的StandardEnvironment实现，但是通常也适用于任何实现，
 * 尽管特定的默认属性源可能不同。
 *
 * <h4>Example: adding a new property source with highest search priority</h4>
 * <pre class="code">
 *   ConfigurableEnvironment environment = new StandardEnvironment();
 *   MutablePropertySources propertySources = environment.getPropertySources();
 *   Map<String, String> myMap = new HashMap<String, String>();
 *   myMap.put("xyz", "myValue");
 *   propertySources.addFirst(new MapPropertySource("MY_MAP", myMap));
 * </pre>
 *
 * <h4>Example: removing the default system properties property source</h4>
 * <pre class="code">
 *   MutablePropertySources propertySources = environment.getPropertySources();
 *   propertySources.remove(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME)
 * </pre>
 *
 * <h4>Example: mocking the system environment for testing purposes</h4>
 * <pre class="code">
 *   MutablePropertySources propertySources = environment.getPropertySources();
 *   MockPropertySource mockEnvVars = new MockPropertySource().withProperty("xyz", "myValue");
 *   propertySources.replace(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, mockEnvVars);
 * </pre>
 *
 * When an {@link Environment} is being used by an {@code ApplicationContext}, it is
 * important that any such {@code PropertySource} manipulations be performed
 * <em>before</em> the context's {@link
 * org.springframework.context.support.AbstractApplicationContext#refresh() refresh()}
 * method is called. This ensures that all property sources are available during the
 * container bootstrap process, including use by {@linkplain
 * org.springframework.context.support.PropertySourcesPlaceholderConfigurer property
 * placeholder configurers}.
 *
 * 当一个Environment被ApplicationContext使用，它是重要的任何这样的PropertySource操作被执行
 * 在context的refresh()方法调用之前。它确保所有的属性资源可用在容器的启动过程中，包括被
 * PropertySourcesPlaceholderConfigurer属性占位符配置器的使用。
 *
 * @author Chris Beams
 * @since 3.1
 * @see StandardEnvironment
 * @see org.springframework.context.ConfigurableApplicationContext#getEnvironment
 */
public interface ConfigurableEnvironment extends Environment, ConfigurablePropertyResolver {

	/**
	 * Specify the set of profiles active for this {@code Environment}. Profiles are
	 * evaluated during container bootstrap to determine whether bean definitions
	 * should be registered with the container.
	 * <p>Any existing active profiles will be replaced with the given arguments; call
	 * with zero arguments to clear the current set of active profiles. Use
	 * {@link #addActiveProfile} to add a profile while preserving the existing set.
	 *
	 * 指定这个Environment启用的profile集合。Profiles被求值在容器启动过程中来判断是否bean定义
	 * 是否应该被注册到这个容器上。
	 * 任何存在的启用的profiles将被替换为给定的参数;带着0个参数来调用将清除当前启用的profile.
	 * 使用addActiveProfile来增加一个profile当保存存在的集合时。
	 *
	 * @see #addActiveProfile
	 * @see #setDefaultProfiles
	 * @see org.springframework.context.annotation.Profile
	 * @see AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME
	 * @throws IllegalArgumentException if any profile is null, empty or whitespace-only
	 */
	void setActiveProfiles(String... profiles);

	/**
	 * Add a profile to the current set of active profiles.
	 * @see #setActiveProfiles
	 * @throws IllegalArgumentException if the profile is null, empty or whitespace-only
	 *
	 * 在当前的活跃的profiles中增加一个profile。
	 */
	void addActiveProfile(String profile);

	/**
	 * Specify the set of profiles to be made active by default if no other profiles
	 * are explicitly made active through {@link #setActiveProfiles}.
	 * @see AbstractEnvironment#DEFAULT_PROFILES_PROPERTY_NAME
	 * @throws IllegalArgumentException if any profile is null, empty or whitespace-only
	 *
	 * 指定默认启用的profile集合如果没有其它profiles被显式地通过setActiveProfiles启用。
	 */
	void setDefaultProfiles(String... profiles);

	/**
	 * Return the {@link PropertySources} for this {@code Environment} in mutable form,
	 * allowing for manipulation of the set of {@link PropertySource} objects that should
	 * be searched when resolving properties against this {@code Environment} object.
	 * The various {@link MutablePropertySources} methods such as
	 * {@link MutablePropertySources#addFirst addFirst},
	 * {@link MutablePropertySources#addFirst addLast},
	 * {@link MutablePropertySources#addFirst addBefore} and
	 * {@link MutablePropertySources#addFirst addAfter} allow for fine-grained control
	 * over property source ordering. This is useful, for example, in ensuring that
	 * certain user-defined property sources have search precedence over default property
	 * sources such as the set of system properties or the set of system environment
	 * variables.
	 * @see AbstractEnvironment#customizePropertySources
	 *
	 * 以可变的形式返回这个Environment的PropertySources，允许操作当根据这个Environment
	 * 对象解析属性时应该被搜索的PropertySource对象的集合
	 *
	 * 不同的MutablePropertySources方法例如：
	 * ...
	 * 允许的对属性源顺序进行详细的控制。这是很有用的，例如，为了确保一些用户定义的属性源比默认的属性源有优先搜索顺序。
	 */
	MutablePropertySources getPropertySources();

	/**
	 * Return the value of {@link System#getenv()} if allowed by the current
	 * {@link SecurityManager}, otherwise return a map implementation that will attempt
	 * to access individual keys using calls to {@link System#getenv(String)}.
	 * <p>Note that most {@link Environment} implementations will include this system
	 * environment map as a default {@link PropertySource} to be searched. Therefore, it
	 * is recommended that this method not be used directly unless bypassing other
	 * property sources is expressly intended.
	 * <p>Calls to {@link Map#get(Object)} on the Map returned will never throw
	 * {@link IllegalAccessException}; in cases where the SecurityManager forbids access
	 * to a property, {@code null} will be returned and an INFO-level log message will be
	 * issued noting the exception.
	 *
	 * 返回System#getenv()的值，如果被当前的SecurityManager允许。否则返回一个map实现，这个map将试图
	 * 访问单独的keys使用System#getenv(String)调用。
	 * 注意大部分Environment实现将包括这个系统环境map作为一个将被搜索的默认的PropertySource。因此，
	 * 建议这个方法不被直接使用除非绕过其它属性源是明确的目的。
	 *
	 * 在返回的Map上调用Map#get(Object)将永远不会抛出IllegalAccessException;在SecurityManager
	 * 禁止访问一个Property的情况下，null将被返回并且一个INFO-level的日志消息将被抛出而不是异常。
	 */
	Map<String, Object> getSystemEnvironment();

	/**
	 * Return the value of {@link System#getProperties()} if allowed by the current
	 * {@link SecurityManager}, otherwise return a map implementation that will attempt
	 * to access individual keys using calls to {@link System#getProperty(String)}.
	 * <p>Note that most {@code Environment} implementations will include this system
	 * properties map as a default {@link PropertySource} to be searched. Therefore, it is
	 * recommended that this method not be used directly unless bypassing other property
	 * sources is expressly intended.
	 * <p>Calls to {@link Map#get(Object)} on the Map returned will never throw
	 * {@link IllegalAccessException}; in cases where the SecurityManager forbids access
	 * to a property, {@code null} will be returned and an INFO-level log message will be
	 * issued noting the exception.
	 *
	 *
	 */
	Map<String, Object> getSystemProperties();

	/**
	 * Append the given parent environment's active profiles, default profiles and
	 * property sources to this (child) environment's respective collections of each.
	 * <p>For any identically-named {@code PropertySource} instance existing in both
	 * parent and child, the child instance is to be preserved and the parent instance
	 * discarded. This has the effect of allowing overriding of property sources by the
	 * child as well as avoiding redundant searches through common property source types,
	 * e.g. system environment and system properties.
	 * <p>Active and default profile names are also filtered for duplicates, to avoid
	 * confusion and redundant storage.
	 * <p>The parent environment remains unmodified in any case. Note that any changes to
	 * the parent environment occurring after the call to {@code merge} will not be
	 * reflected in the child. Therefore, care should be taken to configure parent
	 * property sources and profile information prior to calling {@code merge}.
	 *
	 * 附加给定的父环境的启用的profile,默认的profiles和property源到这个环境的相应的集合中。
	 * 对任何存在于父和孩子中的同名的PropertySource实例，孩子实例将被保留并且父实例丢弃。这有允许被孩子
	 * 的属性源的覆盖的作用也有避免重复的搜索通过通过属性源类型，例如系统环境和系统属性。
	 *
	 * 启用的和默认的profile名称也被过滤为了重复，为了避免混淆和过多的存储。
	 *
	 * 父环境仍然不可修改在任何情况下。注意在调用merge之后对父环境的任何改变将不会反映到孩子中。
	 * 因此应该小心来配置父属性源和profile信息在调用merge之前。
	 *
	 * @param parent the environment to merge with
	 * @since 3.1.2
	 * @see org.springframework.context.support.AbstractApplicationContext#setParent
	 */
	void merge(ConfigurableEnvironment parent);

}
