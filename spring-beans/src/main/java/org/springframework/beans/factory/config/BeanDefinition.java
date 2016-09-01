/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;

/**
 * A BeanDefinition describes a bean instance, which has property values,
 * constructor argument values, and further information supplied by
 * concrete implementations.
 *
 * 一个BeanDefinition描述了一个bean实例，它具有属性值，构造参数值，和被具体的实现提供的更多的信息。
 *
 * <p>This is just a minimal interface: The main intention is to allow a
 * {@link BeanFactoryPostProcessor} such as {@link PropertyPlaceholderConfigurer}
 * to introspect and modify property values and other bean metadata.
 *
 * 这只是一个最小的接口:最主要的目的是允许一个BeanFactoryPostProcessor，例如PropertyPlaceholderConfigurer
 * ，来自我反思和修改属性值和其它bean元数据。
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 19.03.2004
 * @see ConfigurableListableBeanFactory#getBeanDefinition
 * @see org.springframework.beans.factory.support.RootBeanDefinition
 * @see org.springframework.beans.factory.support.ChildBeanDefinition
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

	/**
	 * Scope identifier for the standard singleton scope: "singleton".
	 * <p>Note that extended bean factories might support further scopes.
	 * @see #setScope
	 *
	 * 标准单例scope:"singleton"的Scope标识符。
	 * 注意扩展的工厂可以支持更多的范围。
	 */
	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

	/**
	 * Scope identifier for the standard prototype scope: "prototype".
	 * <p>Note that extended bean factories might support further scopes.
	 * @see #setScope
	 *
	 * 标准单例scope:"prototype"的Scope标识符。
	 * 注意扩展的工厂可以支持更多的范围。
	 */
	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;


	/**
	 * Role hint indicating that a {@code BeanDefinition} is a major part
	 * of the application. Typically corresponds to a user-defined bean.
	 *
	 * 作用提示表示一个BeanDefinition是应用的主要部分。通常对应一个用户定义的bean。
	 */
	int ROLE_APPLICATION = 0;

	/**
	 * Role hint indicating that a {@code BeanDefinition} is a supporting
	 * part of some larger configuration, typically an outer
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 * {@code SUPPORT} beans are considered important enough to be aware
	 * of when looking more closely at a particular
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition},
	 * but not when looking at the overall configuration of an application.
	 *
	 * 角色提示表示BeanDefinition是一些大的配置中的支持部分，通常是一个外部的ComponentDefinition。
	 * SUPPORT beans 被认为足够重要在仔细查看一个特定的ComponentDefinition时应该被觉察。
	 * 但不是在查看整个应用配置的时候。
	 */
	int ROLE_SUPPORT = 1;

	/**
	 * Role hint indicating that a {@code BeanDefinition} is providing an
	 * entirely background role and has no relevance to the end-user. This hint is
	 * used when registering beans that are completely part of the internal workings
	 * of a {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 * 角色提示表示BeanDefinition正提供一个整个背景作为并且和终端用户没有关联。这个提示被使用当注册bean
	 * 是ComponentDefinition完整部分的时候。
	 *
	 */
	int ROLE_INFRASTRUCTURE = 2;


	/**
	 * Return the name of the parent definition of this bean definition, if any.
	 * 返回这个bean定义的父定义名称，如果有的话。
	 */
	String getParentName();

	/**
	 * Set the name of the parent definition of this bean definition, if any.
	 * 设置这个bean定义的父bean定义的名称，如果有的话。
	 */
	void setParentName(String parentName);

	/**
	 * Return the current bean class name of this bean definition.
	 * <p>Note that this does not have to be the actual class name used at runtime, in
	 * case of a child definition overriding/inheriting the class name from its parent.
	 * Hence, do <i>not</i> consider this to be the definitive bean type at runtime but
	 * rather only use it for parsing purposes at the individual bean definition level.
	 * 返回这个bean定义的当前bean类。
	 * 注意在运行时这不必是真实使用的类名，但是只用它用于解析目的在每一个单独的bean定义层次。
	 */
	String getBeanClassName();

	/**
	 * Override the bean class name of this bean definition.
	 * <p>The class name can be modified during bean factory post-processing,
	 * typically replacing the original class name with a parsed variant of it.
	 * 覆盖这个bean定义的bean类名。
	 * 这个类名可以被修改在bean工厂后处理的过程中，通常用一个它的解析的变量替换原始的类名。
	 */
	void setBeanClassName(String beanClassName);

	/**
	 * Return the factory bean name, if any.
	 * 返回工厂bean名称，如果有的话。
	 */
	String getFactoryBeanName();

	/**
	 * Specify the factory bean to use, if any.
	 * 设置要用的工厂的bean，如果有的话。
	 */
	void setFactoryBeanName(String factoryBeanName);

	/**
	 * Return a factory method, if any.
	 * 返回一个工厂方法，如果有的话。
	 */
	String getFactoryMethodName();

	/**
	 * Specify a factory method, if any. This method will be invoked with
	 * constructor arguments, or with no arguments if none are specified.
	 * The method will be invoked on the specified factory bean, if any,
	 * or otherwise as a static method on the local bean class.
	 * 指定一个工厂方法，如果有的话。这个方法将被带着构造参数调用，或者没有参数如果没有指定任务东西。
	 * 这个方法将被在一个指定的工厂bean上被调用，如果有的话，或者否则作为本地bean类的静态方法。
	 * @param factoryMethodName static factory method name,
	 * or {@code null} if normal constructor creation should be used
	 * @see #getBeanClassName()
	 */
	void setFactoryMethodName(String factoryMethodName);

	/**
	 * Return the name of the current target scope for this bean,
	 * or {@code null} if not known yet.
	 * 返回这个bean的当前目的范围的名称。
	 */
	String getScope();

	/**
	 * Override the target scope of this bean, specifying a new scope name.
	 * @see #SCOPE_SINGLETON
	 * @see #SCOPE_PROTOTYPE
	 * 覆盖这个bean的目标范围，指定一个新的范围名称。
	 */
	void setScope(String scope);

	/**
	 * Return whether this bean should be lazily initialized, i.e. not
	 * eagerly instantiated on startup. Only applicable to a singleton bean.
	 *
	 * 返回是否这个bean应试被懒初始化，也就是说。在启动的时候不急切地加载。
	 * 只可应用到单例bean。
	 */
	boolean isLazyInit();

	/**
	 * Set whether this bean should be lazily initialized.
	 * <p>If {@code false}, the bean will get instantiated on startup by bean
	 * factories that perform eager initialization of singletons.
	 *
	 * 设置是否这个bean应该被懒初始化。
	 * 如果false,bean将被bean工厂初始化在启动的时候，
	 */
	void setLazyInit(boolean lazyInit);

	/**
	 * Return the bean names that this bean depends on.
	 * 返回这个bean依赖的bean。
	 */
	String[] getDependsOn();

	/**
	 * Set the names of the beans that this bean depends on being initialized.
	 * The bean factory will guarantee that these beans get initialized first.
	 *
	 * 设置这个bean依赖的bean的名称。bean工厂将保证这些bean首先被初始化。
	 */
	void setDependsOn(String... dependsOn);

	/**
	 * Return whether this bean is a candidate for getting autowired into some other bean.
	 *
	 * 返回是否这个bean是织入到其它bean的候选者。
	 */
	boolean isAutowireCandidate();

	/**
	 * Set whether this bean is a candidate for getting autowired into some other bean.
	 *
	 * 设置是否这个bean是织入到其它bean的候选者。
	 */
	void setAutowireCandidate(boolean autowireCandidate);

	/**
	 * Return whether this bean is a primary autowire candidate.
	 * If this value is true for exactly one bean among multiple
	 * matching candidates, it will serve as a tie-breaker.
	 * 返回是否这个bean是一个主要的自动织入的候选者。
	 * 如果这个值是true。在多个匹配的候选者中的一个bean，它将作为决胜者。
	 */
	boolean isPrimary();

	/**
	 * Set whether this bean is a primary autowire candidate.
	 * <p>If this value is true for exactly one bean among multiple
	 * matching candidates, it will serve as a tie-breaker.
	 *
	 * 设置这个bean是否是一个主要的自动注入的候选者。
	 * 如果对于多个匹配候选者中的一个这个值是true，它将作为决胜者。
	 */
	void setPrimary(boolean primary);


	/**
	 * Return the constructor argument values for this bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * 返回这个bean的构造参数。返回的实例可以被修改在bean工厂后处理过程中。
	 * @return the ConstructorArgumentValues object (never {@code null})
	 */
	ConstructorArgumentValues getConstructorArgumentValues();

	/**
	 * Return the property values to be applied to a new instance of the bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the MutablePropertyValues object (never {@code null})
	 *
	 * 返回应用到这个bean的新的实现上的属性值。
	 * 返回的实例可以被修改在bean工厂后处理过程中。
	 */
	MutablePropertyValues getPropertyValues();


	/**
	 * Return whether this a <b>Singleton</b>, with a single, shared instance
	 * returned on all calls.
	 * @see #SCOPE_SINGLETON
	 *
	 * 返回是否这是一个单例
	 */
	boolean isSingleton();

	/**
	 * Return whether this a <b>Prototype</b>, with an independent instance
	 * returned for each call.
	 * @see #SCOPE_PROTOTYPE
	 *
	 * 返回是否这是一个Prototype，每一次调用返回一个独立的实例。
	 */
	boolean isPrototype();

	/**
	 * Return whether this bean is "abstract", that is, not meant to be instantiated.
	 * 返回是否这个bean是"abstract"，也就是说，不想被实例化。
	 */
	boolean isAbstract();

	/**
	 * Get the role hint for this {@code BeanDefinition}. The role hint
	 * provides the frameworks as well as tools with an indication of
	 * the role and importance of a particular {@code BeanDefinition}.
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 *
	 * 获取这个BeanDefinition的角色提示。角色提示提供给框架和工具一个角色和特定
	 * BeanDefinition的重要性的提示。
	 */
	int getRole();

	/**
	 * Return a human-readable description of this bean definition.
	 * 返回这个bean定义的人类可读的描述。
	 */
	String getDescription();

	/**
	 * Return a description of the resource that this bean definition
	 * came from (for the purpose of showing context in case of errors).
	 * 返回这个bean定义从那些资源来的描述(为了在出错的时候显示上下文)。
	 */
	String getResourceDescription();

	/**
	 * Return the originating BeanDefinition, or {@code null} if none.
	 * Allows for retrieving the decorated bean definition, if any.
	 * <p>Note that this method returns the immediate originator. Iterate through the
	 * originator chain to find the original BeanDefinition as defined by the user.
	 *
	 * 返回原始的BeanDefinition，或者null如果没有。
	 * 允许检索装饰过的bean 定义，如果有的话。
	 * 注意这个方法返回直接的鼻祖。遍历整个鼻祖链来找到被用户定义的原始的BeanDefinition。
	 */
	BeanDefinition getOriginatingBeanDefinition();

}
