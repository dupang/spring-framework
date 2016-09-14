/*
 * Copyright 2002-2013 the original author or authors.
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

/**
 * Interface representing the environment in which the current application is running.
 * Models two key aspects of the application environment: <em>profiles</em> and
 * <em>properties</em>. Methods related to property access are exposed via the
 * {@link PropertyResolver} superinterface.
 *
 * 代表当前应用正在运行的环球的接口。
 * 典型两个关键的应用环境:profiles和properties。属性相关的方法通过PropertyResolver父接口暴露。
 *
 * <p>A <em>profile</em> is a named, logical group of bean definitions to be registered
 * with the container only if the given profile is <em>active</em>. Beans may be assigned
 * to a profile whether defined in XML or via annotations; see the spring-beans 3.1 schema
 * or the {@link org.springframework.context.annotation.Profile @Profile} annotation for
 * syntax details. The role of the {@code Environment} object with relation to profiles is
 * in determining which profiles (if any) are currently {@linkplain #getActiveProfiles
 * active}, and which profiles (if any) should be {@linkplain #getDefaultProfiles active
 * by default}.
 *
 * 一个profile是一个命名的，逻辑上将被注册到容器上的是一组bean定义，只要给定的profile是活跃的。
 * beans可以被分配给一个profile不管以XML定义或通过注解;参考spring-beans 3.1 schema或
 * org.springframework.context.annotation.Profile @Profile注解关于语法的详细信息。
 *
 * 和profiles相关的Environment对象的角色是决定那一个profile正在启动，和那一个profile应该被
 * 默认的启用。
 *
 * <p><em>Properties</em> play an important role in almost all applications, and may
 * originate from a variety of sources: properties files, JVM system properties, system
 * environment variables, JNDI, servlet context parameters, ad-hoc Properties objects,
 * Maps, and so on. The role of the environment object with relation to properties is to
 * provide the user with a convenient service interface for configuring property sources
 * and resolving properties from them.
 *
 * Properties扮演着一个重要的角色在大部分的应用中，并且可能源于各种资源:属性文件，JVM系统属性，
 * 系统环境变量，JNDI,servlet上下文属性，ad-hoc属性对象，Maps等等。和属性相关的environment对象
 * 的角色是提供给用户一个方便的服务接口用于配置属性资源和解析他们的属性。
 *
 * <p>Beans managed within an {@code ApplicationContext} may register to be {@link
 * org.springframework.context.EnvironmentAware EnvironmentAware} or {@code @Inject} the
 * {@code Environment} in order to query profile state or resolve properties directly.
 *
 * ApplicationContext中管理的bean可以注册为EnvironmentAware或注入Environment为了查询profile状态或
 * 解析直接解析属性。
 *
 * <p>In most cases, however, application-level beans should not need to interact with the
 * {@code Environment} directly but instead may have to have {@code ${...}} property
 * values replaced by a property placeholder configurer such as
 * {@link org.springframework.context.support.PropertySourcesPlaceholderConfigurer
 * PropertySourcesPlaceholderConfigurer}, which itself is {@code EnvironmentAware} and
 * as of Spring 3.1 is registered by default when using
 * {@code <context:property-placeholder/>}.
 *
 * 大部分情况下，然而，应用级别的bean不应该需要和Environment直接联系但是相反地可能不得不有${...}属性值
 * 被属性占位符配置器替换，例如PropertySourcesPlaceholderConfigurer，它本身是一个EnvironmentAware
 * 和从Spring 3.1开始当使用<context:property-placeholder/>时默认地被注册。
 *
 * <p>Configuration of the environment object must be done through the
 * {@code ConfigurableEnvironment} interface, returned from all
 * {@code AbstractApplicationContext} subclass {@code getEnvironment()} methods. See
 * {@link ConfigurableEnvironment} Javadoc for usage examples demonstrating manipulation
 * of property sources prior to application context {@code refresh()}.
 *
 * environment对象的配置必须通过ConfigurableEnvironment接口完成，从所有的AbstractApplicationContext的子类的
 * getEnvironment()方法返回。
 * 参考ConfigurableEnvironment的Javadoc关于使用案例。展示了管理属性资源应用上下文refresh()之前。
 *
 * @author Chris Beams
 * @since 3.1
 * @see PropertyResolver
 * @see EnvironmentCapable
 * @see ConfigurableEnvironment
 * @see AbstractEnvironment
 * @see StandardEnvironment
 * @see org.springframework.context.EnvironmentAware
 * @see org.springframework.context.ConfigurableApplicationContext#getEnvironment
 * @see org.springframework.context.ConfigurableApplicationContext#setEnvironment
 * @see org.springframework.context.support.AbstractApplicationContext#createEnvironment
 */
public interface Environment extends PropertyResolver {

	/**
	 * Return the set of profiles explicitly made active for this environment. Profiles
	 * are used for creating logical groupings of bean definitions to be registered
	 * conditionally, for example based on deployment environment.  Profiles can be
	 * activated by setting {@linkplain AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME
	 * "spring.profiles.active"} as a system property or by calling
	 * {@link ConfigurableEnvironment#setActiveProfiles(String...)}.
	 * <p>If no profiles have explicitly been specified as active, then any {@linkplain
	 * #getDefaultProfiles() default profiles} will automatically be activated.
	 *
	 * 返回这个环境下的显式启用的profiles集合。Profiles被用于创建将被有条件注册的bean定义的逻辑组，例如，
	 * 基于开发环境。Profiles可以被启用通过设置AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME
	 * 作为一个系统属性或通过调用ConfigurableEnvironment#setActiveProfiles(String...)。
	 * 如果没有profiles显示地被指定为启用，那么任何getDefaultProfiles()将自动地被启用。
	 *
	 *
	 * @see #getDefaultProfiles
	 * @see ConfigurableEnvironment#setActiveProfiles
	 * @see AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME
	 */
	String[] getActiveProfiles();

	/**
	 * Return the set of profiles to be active by default when no active profiles have
	 * been set explicitly.
	 * 返回当没有显示地启用profiles时默认启用的profiles集合。
	 * @see #getActiveProfiles
	 * @see ConfigurableEnvironment#setDefaultProfiles
	 * @see AbstractEnvironment#DEFAULT_PROFILES_PROPERTY_NAME
	 */
	String[] getDefaultProfiles();

	/**
	 * Return whether one or more of the given profiles is active or, in the case of no
	 * explicit active profiles, whether one or more of the given profiles is included in
	 * the set of default profiles. If a profile begins with '!' the logic is inverted,
	 * i.e. the method will return true if the given profile is <em>not</em> active.
	 * For example, <pre class="code">env.acceptsProfiles("p1", "!p2")</pre> will
	 * return {@code true} if profile 'p1' is active or 'p2' is not active.
	 *
	 * 返回是否一个或多个给定的profile启用或在没有显式的启用profile的情况下，是否一个或多个profile被包含在
	 * 默认的profile集合中。如果一个proifle以！开头，逻辑是相反的，
	 * 也就是说，方法将返回true如果给定的proifle没有启用。
	 * 例如，
	 * @throws IllegalArgumentException if called with zero arguments
	 * or if any profile is {@code null}, empty or whitespace-only
	 * @see #getActiveProfiles
	 * @see #getDefaultProfiles
	 */
	boolean acceptsProfiles(String... profiles);

}
