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

package org.springframework.beans.factory.config;

import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;

/**
 * Extension of the {@link org.springframework.beans.factory.BeanFactory}
 * interface to be implemented by bean factories that are capable of
 * autowiring, provided that they want to expose this functionality for
 * existing bean instances.
 *
 * 要被具有自动注入的bean工厂实现的BeanFactory接口的扩展,假如他们想暴露存在的bean实例的
 * 这个功能
 *
 * <p>This subinterface of BeanFactory is not meant to be used in normal
 * application code: stick to {@link org.springframework.beans.factory.BeanFactory}
 * or {@link org.springframework.beans.factory.ListableBeanFactory} for
 * typical use cases.
 *
 * 这个BeanFactory的子接口并不想被用在普通的应用代码中：典型地使用案例是紧跟着BeanFactory或ListableBeanFactory
 *
 * <p>Integration code for other frameworks can leverage this interface to
 * wire and populate existing bean instances that Spring does not control
 * the lifecycle of. This is particularly useful for WebWork Actions and
 * Tapestry Page objects, for example.
 *
 * 和其它框架的整合可以利用这个接口来织入和填充存在的Spring不能控制生命周期的的bean实例。
 * 例如，特别对WebWork Action和Tapestry Page objects很有用。
 *
 *
 *
 * <p>Note that this interface is not implemented by
 * {@link org.springframework.context.ApplicationContext} facades,
 * as it is hardly ever used by application code. That said, it is available
 * from an application context too, accessible through ApplicationContext's
 * {@link org.springframework.context.ApplicationContext#getAutowireCapableBeanFactory()}
 * method.
 *
 * 注意这个接口没有被ApplicationContext接口实现，因为它很少被应用代码使用。即使如此，它也能从一个应用上下文中获取，
 * 通过ApplicationContext的getAutowireCapableBeanFactory()方法访问。
 *
 * <p>You may also implement the {@link org.springframework.beans.factory.BeanFactoryAware}
 * interface, which exposes the internal BeanFactory even when running in an
 * ApplicationContext, to get access to an AutowireCapableBeanFactory:
 * simply cast the passed-in BeanFactory to AutowireCapableBeanFactory.
 *
 * 你也可以实现BeanFactoryAware接口，它暴露了内部的BeanFactory即使当运行在一个ApplicationContext中，
 * 为了访问一个AutowireCapableBeanFactory:简单地转换传入的BeanFactory为AutowireCapableBeanFactory
 *
 * @author Juergen Hoeller
 * @since 04.12.2003
 * @see org.springframework.beans.factory.BeanFactoryAware
 * @see org.springframework.beans.factory.config.ConfigurableListableBeanFactory
 * @see org.springframework.context.ApplicationContext#getAutowireCapableBeanFactory()
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

	/**
	 * Constant that indicates no externally defined autowiring. Note that
	 * BeanFactoryAware etc and annotation-driven injection will still be applied.
	 *
	 * 表示没有外部定义的自动装配的常量。注意BeanFactoryAware等等和注解驱动的注入将仍然被应用。
	 * @see #createBean
	 * @see #autowire
	 * @see #autowireBeanProperties
	 */
	int AUTOWIRE_NO = 0;

	/**
	 * Constant that indicates autowiring bean properties by name
	 * (applying to all bean property setters).
	 * 表示通过名称自动注入bean属性的常量(应用到所有bean属性设置)
	 * @see #createBean
	 * @see #autowire
	 * @see #autowireBeanProperties
	 */
	int AUTOWIRE_BY_NAME = 1;

	/**
	 * Constant that indicates autowiring bean properties by type
	 * (applying to all bean property setters).
	 * 表示通过类型自动注入bean属性的常量(应用到所有bean属性设置)
	 * @see #createBean
	 * @see #autowire
	 * @see #autowireBeanProperties
	 */
	int AUTOWIRE_BY_TYPE = 2;

	/**
	 * Constant that indicates autowiring the greediest constructor that
	 * can be satisfied (involves resolving the appropriate constructor).
	 *
	 * 表示自动注入最满足匹配的构造器的常量。
	 * @see #createBean
	 * @see #autowire
	 */
	int AUTOWIRE_CONSTRUCTOR = 3;

	/**
	 * Constant that indicates determining an appropriate autowire strategy
	 * through introspection of the bean class.
	 * 表示通过bean类的自我反省决定一个最适合的自动注入的策略的常量。
	 * @see #createBean
	 * @see #autowire
	 * @deprecated as of Spring 3.0: If you are using mixed autowiring strategies,
	 * prefer annotation-based autowiring for clearer demarcation of autowiring needs.
	 */
	@Deprecated
	int AUTOWIRE_AUTODETECT = 4;


	//-------------------------------------------------------------------------
	// Typical methods for creating and populating external bean instances
	// 创建和填充外部的bean实例的典型的方法
	//-------------------------------------------------------------------------

	/**
	 * Fully create a new bean instance of the given class.
	 * <p>Performs full initialization of the bean, including all applicable
	 * {@link BeanPostProcessor BeanPostProcessors}.
	 * <p>Note: This is intended for creating a fresh instance, populating annotated
	 * fields and methods as well as applying all standard bean initialization callbacks.
	 * It does <i>not</> imply traditional by-name or by-type autowiring of properties;
	 * use {@link #createBean(Class, int, boolean)} for those purposes.
	 *
	 * 完全创建一个新的给定类的bean实例。
	 * 执行bean的完全初始化，包括所有可用的BeanPostProcessors。
	 * 注意:这个目的是为了创建一个新的实例，填充注解的字段和方法，同时也应用所有标准的bean初始化回调。
	 * 它不意为传统的通过名称或类型属性的自动注入;
	 * 要是以这样的目的可以使用createBean(Class, int, boolean)。
	 * @param beanClass the class of the bean to create
	 * @return the new bean instance
	 * @throws BeansException if instantiation or wiring failed
	 */
	<T> T createBean(Class<T> beanClass) throws BeansException;

	/**
	 * Populate the given bean instance through applying after-instantiation callbacks
	 * and bean property post-processing (e.g. for annotation-driven injection).
	 * <p>Note: This is essentially intended for (re-)populating annotated fields and
	 * methods, either for new instances or for deserialized instances. It does
	 * <i>not</i> imply traditional by-name or by-type autowiring of properties;
	 * use {@link #autowireBeanProperties} for those purposes.
	 * 填充给定的bean实例通过应用after-instantiation回调并且bean属性post-processing(例如，对于注解驱动的注入)。
	 * 注意:这主要是为了填充注解的字段和方法，也为了新的实例或反序列化的实例。
	 *
	 * @param existingBean the existing bean instance
	 *
	 * @throws BeansException if wiring failed
	 */
	void autowireBean(Object existingBean) throws BeansException;

	/**
	 * Configure the given raw bean: autowiring bean properties, applying
	 * bean property values, applying factory callbacks such as {@code setBeanName}
	 * and {@code setBeanFactory}, and also applying all bean post processors
	 * (including ones which might wrap the given raw bean).
	 * <p>This is effectively a superset of what {@link #initializeBean} provides,
	 * fully applying the configuration specified by the corresponding bean definition.
	 * <b>Note: This method requires a bean definition for the given name!</b>
	 * 配置给定的未经加工的bean:自动注入bean属性，应用bean属性值，应用工厂回调例如setBeanName和setBeanFactory。
	 * 并且应用所有bean后处理器(包括可能包装给定的未经加工的bean).
	 * 这是initializeBean提供的有效的超超集，完全应用被相应bean定义指定的配置。
	 * 注意:这个方法要求给定名字的bean定义。
	 * @param existingBean the existing bean instance
	 * @param beanName the name of the bean, to be passed to it if necessary
	 * (a bean definition of that name has to be available)
	 * @return the bean instance to use, either the original or a wrapped one
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
	 * if there is no bean definition with the given name
	 * @throws BeansException if the initialization failed
	 * @see #initializeBean
	 */
	Object configureBean(Object existingBean, String beanName) throws BeansException;

	/**
	 * Resolve the specified dependency against the beans defined in this factory.
	 * 解析这个工厂中的bean定义的指定依赖。
	 * @param descriptor the descriptor for the dependency
	 * @param beanName the name of the bean which declares the present dependency
	 * @return the resolved object, or {@code null} if none found
	 * @throws BeansException if dependency resolution failed
	 */
	Object resolveDependency(DependencyDescriptor descriptor, String beanName) throws BeansException;


	//-------------------------------------------------------------------------
	// Specialized methods for fine-grained control over the bean lifecycle
	// bean生命周期的详细控制的特别的方法。
	//-------------------------------------------------------------------------

	/**
	 * Fully create a new bean instance of the given class with the specified
	 * autowire strategy. All constants defined in this interface are supported here.
	 * <p>Performs full initialization of the bean, including all applicable
	 * {@link BeanPostProcessor BeanPostProcessors}. This is effectively a superset
	 * of what {@link #autowire} provides, adding {@link #initializeBean} behavior.
	 * 用指定的自动注入策略完全创建一个给定类的新的bean实例。所有在这个接口中定义的常量都被支持。
	 * 执行bean完全初始化，包括所有可用的BeanPostProcessors。这是autowire提供的有效的超超集，
	 * 增加initializeBean的行为。
	 * @param beanClass the class of the bean to create
	 * @param autowireMode by name or type, using the constants in this interface
	 * @param dependencyCheck whether to perform a dependency check for objects
	 * (not applicable to autowiring a constructor, thus ignored there)
	 * @return the new bean instance
	 * @throws BeansException if instantiation or wiring failed
	 * @see #AUTOWIRE_NO
	 * @see #AUTOWIRE_BY_NAME
	 * @see #AUTOWIRE_BY_TYPE
	 * @see #AUTOWIRE_CONSTRUCTOR
	 */
	Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;

	/**
	 * Instantiate a new bean instance of the given class with the specified autowire
	 * strategy. All constants defined in this interface are supported here.
	 * Can also be invoked with {@code AUTOWIRE_NO} in order to just apply
	 * before-instantiation callbacks (e.g. for annotation-driven injection).
	 * <p>Does <i>not</i> apply standard {@link BeanPostProcessor BeanPostProcessors}
	 * callbacks or perform any further initialization of the bean. This interface
	 * offers distinct, fine-grained operations for those purposes, for example
	 * {@link #initializeBean}. However, {@link InstantiationAwareBeanPostProcessor}
	 * callbacks are applied, if applicable to the construction of the instance.
	 *
	 * 用指定的自动注入策略完全实例化一个给定类的新的bean实例。所有这个接口中定义的常量都支持。
	 * 也可以被用AUTOWIRE_NO调用只是为了应用实例化前回调(例如为了注解驱动注入)。
	 * 不应用标准的BeanPostProcessor回调或执行任何更进一步的bean的初始化。
	 * 这个接口提供为这些目的不同的，细致的操作，例如，initializeBean。然而，InstantiationAwareBeanPostProcessor
	 * 回调被应用了，如果对这个实例的构造器可用。
	 *
	 * @param beanClass the class of the bean to instantiate
	 * @param autowireMode by name or type, using the constants in this interface
	 * @param dependencyCheck whether to perform a dependency check for object
	 * references in the bean instance (not applicable to autowiring a constructor,
	 * thus ignored there)
	 * @return the new bean instance
	 * @throws BeansException if instantiation or wiring failed
	 * @see #AUTOWIRE_NO
	 * @see #AUTOWIRE_BY_NAME
	 * @see #AUTOWIRE_BY_TYPE
	 * @see #AUTOWIRE_CONSTRUCTOR
	 * @see #AUTOWIRE_AUTODETECT
	 * @see #initializeBean
	 * @see #applyBeanPostProcessorsBeforeInitialization
	 * @see #applyBeanPostProcessorsAfterInitialization
	 */
	Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;

	/**
	 * Autowire the bean properties of the given bean instance by name or type.
	 * Can also be invoked with {@code AUTOWIRE_NO} in order to just apply
	 * after-instantiation callbacks (e.g. for annotation-driven injection).
	 * <p>Does <i>not</i> apply standard {@link BeanPostProcessor BeanPostProcessors}
	 * callbacks or perform any further initialization of the bean. This interface
	 * offers distinct, fine-grained operations for those purposes, for example
	 * {@link #initializeBean}. However, {@link InstantiationAwareBeanPostProcessor}
	 * callbacks are applied, if applicable to the configuration of the instance.
	 * 通过名称或类型自动注入给定bean实例的bean属性。
	 * 也可以被用AUTOWIRE_NO调用只是为了应用实例化前回调(例如为了注解驱动注入)。
	 * 不应用标准的BeanPostProcessor回调或执行任何更进一步的bean的初始化。
	 * 这个接口提供为这些目的不同的，细致的操作，例如，initializeBean。然而，InstantiationAwareBeanPostProcessor
	 * 回调被应用了，如果对这个实例的构造器可用。
	 * @param existingBean the existing bean instance
	 * @param autowireMode by name or type, using the constants in this interface
	 * @param dependencyCheck whether to perform a dependency check for object
	 * references in the bean instance
	 * @throws BeansException if wiring failed
	 * @see #AUTOWIRE_BY_NAME
	 * @see #AUTOWIRE_BY_TYPE
	 * @see #AUTOWIRE_NO
	 */
	void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)
			throws BeansException;

	/**
	 * Apply the property values of the bean definition with the given name to
	 * the given bean instance. The bean definition can either define a fully
	 * self-contained bean, reusing its property values, or just property values
	 * meant to be used for existing bean instances.
	 * <p>This method does <i>not</i> autowire bean properties; it just applies
	 * explicitly defined property values. Use the {@link #autowireBeanProperties}
	 * method to autowire an existing bean instance.
	 * <b>Note: This method requires a bean definition for the given name!</b>
	 * <p>Does <i>not</i> apply standard {@link BeanPostProcessor BeanPostProcessors}
	 * callbacks or perform any further initialization of the bean. This interface
	 * offers distinct, fine-grained operations for those purposes, for example
	 * {@link #initializeBean}. However, {@link InstantiationAwareBeanPostProcessor}
	 * callbacks are applied, if applicable to the configuration of the instance.
	 * 应用给定名称的bean定义的属性值到给定的bean实例。bean定义可能定义一个完全自包含的bean,重用
	 * 它的属性值，或只是被用来为存在的bean实例的属性值。
	 * 这个方法不自动注入bean属性，它只是应用显式的定义的属性值。使用autowireBeanProperties方法
	 * 来自动注入一个存在的bean实例。
	 * 注意:这个方法需要给定名字的一个bean定义！
	 * 不应用标准的BeanPostProcessors回调或执行任何更进一步的bean的初始化。这个接口提供了为这个目的
	 * 不同的，更详细的操作，例如，initializeBean。然而，InstantiationAwareBeanPostProcessor回调
	 * 被调用，如果实例的配置可用。
	 *
	 * @param existingBean the existing bean instance
	 * @param beanName the name of the bean definition in the bean factory
	 * (a bean definition of that name has to be available)
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
	 * if there is no bean definition with the given name
	 * @throws BeansException if applying the property values failed
	 * @see #autowireBeanProperties
	 */
	void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException;

	/**
	 * Initialize the given raw bean, applying factory callbacks
	 * such as {@code setBeanName} and {@code setBeanFactory},
	 * also applying all bean post processors (including ones which
	 * might wrap the given raw bean).
	 * <p>Note that no bean definition of the given name has to exist
	 * in the bean factory. The passed-in bean name will simply be used
	 * for callbacks but not checked against the registered bean definitions.
	 *
	 * 初始化给定的未经处理的bean,应用工厂回调例如setBeanName和setBeanFactory，
	 * 也应用所有bean后处理器(包含可能包装给定未经处理的bean的处理器).
	 * 注意不需要给定名字bean的定义存在于这个bean工厂中。传入的bean名字将简单地被使用到回调，
	 * 但是不检查注册的bean定义。
	 * @param existingBean the existing bean instance
	 * @param beanName the name of the bean, to be passed to it if necessary
	 * (only passed to {@link BeanPostProcessor BeanPostProcessors})
	 * @return the bean instance to use, either the original or a wrapped one
	 * @throws BeansException if the initialization failed
	 */
	Object initializeBean(Object existingBean, String beanName) throws BeansException;

	/**
	 * Apply {@link BeanPostProcessor BeanPostProcessors} to the given existing bean
	 * instance, invoking their {@code postProcessBeforeInitialization} methods.
	 * The returned bean instance may be a wrapper around the original.
	 * 应用BeanPostProcessors到给定的存在bean实例，调用他们的postProcessBeforeInitialization方法。
	 * 返回的bean实例可能是一个围绕原始的封装。
	 * @param existingBean the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one
	 * @throws BeansException if any post-processing failed
	 * @see BeanPostProcessor#postProcessBeforeInitialization
	 */
	Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
			throws BeansException;

	/**
	 * Apply {@link BeanPostProcessor BeanPostProcessors} to the given existing bean
	 * instance, invoking their {@code postProcessAfterInitialization} methods.
	 * The returned bean instance may be a wrapper around the original.
	 * 应用BeanPostProcessors到给定的存在bean实例，调用他们的postProcessAfterInitialization方法。
	 * 返回的bean实例可能是一个围绕原始的封装。
	 * @param existingBean the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one
	 * @throws BeansException if any post-processing failed
	 * @see BeanPostProcessor#postProcessAfterInitialization
	 */
	Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
			throws BeansException;

	/**
	 * Destroy the given bean instance (typically coming from {@link #createBean}),
	 * applying the {@link org.springframework.beans.factory.DisposableBean} contract as well as
	 * registered {@link DestructionAwareBeanPostProcessor DestructionAwareBeanPostProcessors}.
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 * 销毁给定的bean实例(通常来源于createBean),
	 * 应用于DisposableBean协议也注册DestructionAwareBeanPostProcessors。
	 * 任何抛出的异常应该被捕获并且被记录而不是传播到方法的调用者。
	 * @param existingBean the bean instance to destroy
	 */
	void destroyBean(Object existingBean);

	/**
	 * Resolve the specified dependency against the beans defined in this factory.
	 * 解析这个工厂中定义的特定依赖
	 * @param descriptor the descriptor for the dependency
	 * @param beanName the name of the bean which declares the present dependency
	 * @param autowiredBeanNames a Set that all names of autowired beans (used for
	 * resolving the present dependency) are supposed to be added to
	 * @param typeConverter the TypeConverter to use for populating arrays and
	 * collections
	 * @return the resolved object, or {@code null} if none found
	 * @throws BeansException if dependency resolution failed
	 */
	Object resolveDependency(DependencyDescriptor descriptor, String beanName,
			Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException;

}
