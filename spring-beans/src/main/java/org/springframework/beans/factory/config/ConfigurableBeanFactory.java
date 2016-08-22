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

import java.beans.PropertyEditor;
import java.security.AccessControlContext;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringValueResolver;

/**
 * Configuration interface to be implemented by most bean factories. Provides
 * facilities to configure a bean factory, in addition to the bean factory
 * client methods in the {@link org.springframework.beans.factory.BeanFactory}
 * interface.
 *
 * 将被大部分bean工厂实现的配置接口。提供了设施来配置bean工厂，除了BeanFactory接口中的
 * bean工厂的客户端方法
 *
 * <p>This bean factory interface is not meant to be used in normal application
 * code: Stick to {@link org.springframework.beans.factory.BeanFactory} or
 * {@link org.springframework.beans.factory.ListableBeanFactory} for typical
 * needs. This extended interface is just meant to allow for framework-internal
 * plug'n'play and for special access to bean factory configuration methods.
 *
 * 这个bean工厂接口不是想被用在普通的应用代码中:紧跟着BeanFactory或ListableBeanFactory为了典型的需求。
 * 这个扩展的接口仅仅想允许框架内部使用，并且为了对bean工厂配置方法的特殊访问。
 *
 * @author Juergen Hoeller
 * @since 03.11.2003
 * @see org.springframework.beans.factory.BeanFactory
 * @see org.springframework.beans.factory.ListableBeanFactory
 * @see ConfigurableListableBeanFactory
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

	/**
	 * Scope identifier for the standard singleton scope: "singleton".
	 * Custom scopes can be added via {@code registerScope}.
	 * 标准的单例范围的范围标识符:"singleton"。
	 * 自定义的范围可以通过registerScope添加。
	 * @see #registerScope
	 */
	String SCOPE_SINGLETON = "singleton";

	/**
	 * Scope identifier for the standard prototype scope: "prototype".
	 * Custom scopes can be added via {@code registerScope}.
	 * 标准的原型范围的范围标识符:"singleton"。
	 * 自定义的范围可以通过registerScope添加。
	 * @see #registerScope
	 */
	String SCOPE_PROTOTYPE = "prototype";


	/**
	 * Set the parent of this bean factory.
	 * <p>Note that the parent cannot be changed: It should only be set outside
	 * a constructor if it isn't available at the time of factory instantiation.
	 * 设置这个bean工厂的父亲。
	 * 注意父亲不能被改变：它应该只能在构造函数之外被设置，如果它在工厂初始化的时候不可用。
	 * @param parentBeanFactory the parent BeanFactory
	 * @throws IllegalStateException if this factory is already associated with
	 * a parent BeanFactory
	 * @see #getParentBeanFactory()
	 */
	void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException;

	/**
	 * Set the class loader to use for loading bean classes.
	 * Default is the thread context class loader.
	 * <p>Note that this class loader will only apply to bean definitions
	 * that do not carry a resolved bean class yet. This is the case as of
	 * Spring 2.0 by default: Bean definitions only carry bean class names,
	 * to be resolved once the factory processes the bean definition.
	 * 设置用来加载bean类的类加载器。默认是线程上下文的类加载器。
	 * 注意这个类加载器将只应用到不携带一个解析的bean类的bean定义。这个Spring 2.0的默认情况:
	 * bean定义只携带bean类名，一旦工厂处理bean定义的时候被解析。
	 * @param beanClassLoader the class loader to use,
	 * or {@code null} to suggest the default class loader
	 */
	void setBeanClassLoader(ClassLoader beanClassLoader);

	/**
	 * Return this factory's class loader for loading bean classes.
	 * 返回用于加载bean类的工厂的类加载器
	 */
	ClassLoader getBeanClassLoader();

	/**
	 * Specify a temporary ClassLoader to use for type matching purposes.
	 * Default is none, simply using the standard bean ClassLoader.
	 * <p>A temporary ClassLoader is usually just specified if
	 * <i>load-time weaving</i> is involved, to make sure that actual bean
	 * classes are loaded as lazily as possible. The temporary loader is
	 * then removed once the BeanFactory completes its bootstrap phase.
	 *
	 * 指定一个暂时的用来类型匹配目的的类加载器。默认是没有的，简单地使用标准的bean类加载器。
	 * 一个暂时的类加载器通常被指定如果涉及到加载时织入。
	 * 来确真实的bean类被尽可能的延迟加载。暂时的加载器然后被删除一旦BeanFactory完成了它的启动阶段。
	 * @since 2.5
	 */
	void setTempClassLoader(ClassLoader tempClassLoader);

	/**
	 * Return the temporary ClassLoader to use for type matching purposes,
	 * 返回用于类型匹配目的的暂时类加载器。
	 * if any.
	 * @since 2.5
	 */
	ClassLoader getTempClassLoader();

	/**
	 * Set whether to cache bean metadata such as given bean definitions
	 * (in merged fashion) and resolved bean classes. Default is on.
	 * <p>Turn this flag off to enable hot-refreshing of bean definition objects
	 * and in particular bean classes. If this flag is off, any creation of a bean
	 * instance will re-query the bean class loader for newly resolved classes.
	 * 设置是否缓存例如给定bean定义的bean元数据(以合并的形式)并且解析bean类。默认是打开的。
	 * 关闭这个标记来打开bean定义对象的热恢复并且特别是bean类。如果这个标记是关闭的，任何bean实例的
	 * 创建将重新要求新的解析的类的bean类加载器。
	 *
	 */
	void setCacheBeanMetadata(boolean cacheBeanMetadata);

	/**
	 * Return whether to cache bean metadata such as given bean definitions
	 * (in merged fashion) and resolved bean classes.
	 * 返回是否缓存例如给定bean定义的bean元数据(以合并的形式)和解析bean类。
	 */
	boolean isCacheBeanMetadata();

	/**
	 * Specify the resolution strategy for expressions in bean definition values.
	 * <p>There is no expression support active in a BeanFactory by default.
	 * An ApplicationContext will typically set a standard expression strategy
	 * here, supporting "#{...}" expressions in a Unified EL compatible style.
	 * 指定表示bean定义值的解析策略。默认的没有表达式支持在BeanFactory中。
	 * 一个 ApplicationContext将通常设置一个标准的表达式策略，支持"#{...}"表达式
	 * 用一种统一的EL兼容风格。
	 * @since 3.0
	 */
	void setBeanExpressionResolver(BeanExpressionResolver resolver);

	/**
	 * Return the resolution strategy for expressions in bean definition values.
	 * 返回表达bean定义值的解析策略
	 * @since 3.0
	 */
	BeanExpressionResolver getBeanExpressionResolver();

	/**
	 * Specify a Spring 3.0 ConversionService to use for converting
	 * property values, as an alternative to JavaBeans PropertyEditors.
	 * 指定一个Spring3.0的ConversionService用来转换属性值，作为JavaBeans的
	 * PropertyEditors替换方式。
	 * @since 3.0
	 */
	void setConversionService(ConversionService conversionService);

	/**
	 * Return the associated ConversionService, if any.
	 * 返回相关的ConversionService，如果有的话。
	 * @since 3.0
	 */
	ConversionService getConversionService();

	/**
	 * Add a PropertyEditorRegistrar to be applied to all bean creation processes.
	 * <p>Such a registrar creates new PropertyEditor instances and registers them
	 * on the given registry, fresh for each bean creation attempt. This avoids
	 * the need for synchronization on custom editors; hence, it is generally
	 * preferable to use this method instead of {@link #registerCustomEditor}.
	 * 增加一个应用到所有bean创建过程的PropertyEditorRegistrar。
	 * 这样的注册器创建一个新的PropertyEditor实例并且在给定的注册器上注册它们。
	 * 对每一个bean创建企图都是新鲜的。这避免了在自定义编辑器上的同步。因此，通常使用这个方法
	 * 来代替registerCustomEditor。
	 *
	 * @param registrar the PropertyEditorRegistrar to register
	 */
	void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);

	/**
	 * Register the given custom property editor for all properties of the
	 * given type. To be invoked during factory configuration.
	 * <p>Note that this method will register a shared custom editor instance;
	 * access to that instance will be synchronized for thread-safety. It is
	 * generally preferable to use {@link #addPropertyEditorRegistrar} instead
	 * of this method, to avoid for the need for synchronization on custom editors.
	 * 为所有给定类型的属性注册给定的自定义属性编辑器。在工厂配置的过程中被调用。
	 * 注意这个方法将注册一个共享的自定义编辑器实例;访问这个实例为了线程安全将被同步。通常使用addPropertyEditorRegistrar
	 * 来代替这个方法，来避免在自定义编辑器上的同步需要。
	 * @param requiredType type of the property
	 * @param propertyEditorClass the {@link PropertyEditor} class to register
	 */
	void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass);

	/**
	 * Initialize the given PropertyEditorRegistry with the custom editors
	 * that have been registered with this BeanFactory.
	 * 用已经注册到这个BeanFactory的自定义编辑器初始化给定的PropertyEditorRegistry
	 * @param registry the PropertyEditorRegistry to initialize
	 */
	void copyRegisteredEditorsTo(PropertyEditorRegistry registry);

	/**
	 * Set a custom type converter that this BeanFactory should use for converting
	 * bean property values, constructor argument values, etc.
	 * <p>This will override the default PropertyEditor mechanism and hence make
	 * any custom editors or custom editor registrars irrelevant.
	 * 设置一个自定义的类型转换器，这个BeanFactory使用它来转换bean属性值，构造参数值，等等。
	 * 这将覆盖默认的PropertyEditor机制并且因此使任何自定义编辑器或自定义编辑器不适当地注册。
	 * @see #addPropertyEditorRegistrar
	 * @see #registerCustomEditor
	 * @since 2.5
	 */
	void setTypeConverter(TypeConverter typeConverter);

	/**
	 * Obtain a type converter as used by this BeanFactory. This may be a fresh
	 * instance for each call, since TypeConverters are usually <i>not</i> thread-safe.
	 * <p>If the default PropertyEditor mechanism is active, the returned
	 * TypeConverter will be aware of all custom editors that have been registered.
	 * 获取一个被这个BeanFactory使用的类型转换器。这可能是一个新的实例对每一次调用，因为TypeConverters
	 * 通常不是线程安全的。
	 * @since 2.5
	 */
	TypeConverter getTypeConverter();

	/**
	 * Add a String resolver for embedded values such as annotation attributes.
	 * 为例如注解属性这样的内置的值增加一个String解析器。
	 * @param valueResolver the String resolver to apply to embedded values
	 * @since 3.0
	 */
	void addEmbeddedValueResolver(StringValueResolver valueResolver);

	/**
	 * Determine whether an embedded value resolver has been registered with this
	 * bean factory, to be applied through {@link #resolveEmbeddedValue(String)}.
	 * 判断是否一个内置的值解析器已经被注册到这个bean工厂上，通过resolveEmbeddedValue(String)
	 * 来应用。
	 * @since 4.3
	 */
	boolean hasEmbeddedValueResolver();

	/**
	 * Resolve the given embedded value, e.g. an annotation attribute.
	 * 解析给定的内置值，例如注解属性。
	 * @param value the value to resolve
	 * @return the resolved value (may be the original value as-is)
	 * @since 3.0
	 */
	String resolveEmbeddedValue(String value);

	/**
	 * Add a new BeanPostProcessor that will get applied to beans created
	 * by this factory. To be invoked during factory configuration.
	 * <p>Note: Post-processors submitted here will be applied in the order of
	 * registration; any ordering semantics expressed through implementing the
	 * {@link org.springframework.core.Ordered} interface will be ignored. Note
	 * that autodetected post-processors (e.g. as beans in an ApplicationContext)
	 * will always be applied after programmatically registered ones.
	 * 增加一个新的将被这个工厂应用到bean创建的新的BeanPostProcessor。在工厂配置的过程中被调用。
	 * 注意：这里提交的后处理器将被以注册时的顺序被应用;任何通过实现org.springframework.core.Ordered
	 * 接口的顺序语义表达式将被忽略。注意自动检测的后处理器将总是被应用在编程地注册一个之后。
	 * @param beanPostProcessor the post-processor to register
	 */
	void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

	/**
	 * Return the current number of registered BeanPostProcessors, if any.
	 * 返回注册的BeanPostProcessors的数量，如果有的话。
	 */
	int getBeanPostProcessorCount();

	/**
	 * Register the given scope, backed by the given Scope implementation.
	 * 注册给定的范围，依靠给定的范围实现。
	 * @param scopeName the scope identifier
	 * @param scope the backing Scope implementation
	 */
	void registerScope(String scopeName, Scope scope);

	/**
	 * Return the names of all currently registered scopes.
	 * <p>This will only return the names of explicitly registered scopes.
	 * Built-in scopes such as "singleton" and "prototype" won't be exposed.
	 * 返回当前所有注册的范围的名字。
	 * 这将只返回显示注册的范围的名字。
	 * 内置的范围例如singleton和prototype将不会暴露。
	 * @return the array of scope names, or an empty array if none
	 * @see #registerScope
	 */
	String[] getRegisteredScopeNames();

	/**
	 * Return the Scope implementation for the given scope name, if any.
	 * <p>This will only return explicitly registered scopes.
	 * Built-in scopes such as "singleton" and "prototype" won't be exposed.
	 * 返回给定的范围的实现，如果有的话。
	 * 这将只返回显示注册的范围的名字。
	 * 内置的范围例如singleton和prototype将不会暴露。
	 * @param scopeName the name of the scope
	 * @return the registered Scope implementation, or {@code null} if none
	 * @see #registerScope
	 */
	Scope getRegisteredScope(String scopeName);

	/**
	 * Provides a security access control context relevant to this factory.
	 * 提供一个和这个工厂相关的安全访问控制上下文。
	 * @return the applicable AccessControlContext (never {@code null})
	 * @since 3.0
	 */
	AccessControlContext getAccessControlContext();

	/**
	 * Copy all relevant configuration from the given other factory.
	 * <p>Should include all standard configuration settings as well as
	 * BeanPostProcessors, Scopes, and factory-specific internal settings.
	 * Should not include any metadata of actual bean definitions,
	 * such as BeanDefinition objects and bean name aliases.
	 * 从给定的其它工厂拷贝所有相关的配置。
	 * 应该包含所有标准的配置设置和BeanPostProcessors,范围和工厂指定的内部设置。
	 * 不应该包含真实bean定义的任何元数据，例如BeanDefinition对象和bean名字别名。
	 * @param otherFactory the other BeanFactory to copy from
	 */
	void copyConfigurationFrom(ConfigurableBeanFactory otherFactory);

	/**
	 * Given a bean name, create an alias. We typically use this method to
	 * support names that are illegal within XML ids (used for bean names).
	 * <p>Typically invoked during factory configuration, but can also be
	 * used for runtime registration of aliases. Therefore, a factory
	 * implementation should synchronize alias access.
	 * 给定一个bean名字，创建一个别名。我们通常使用这个方法来支持在XML的id不合法的名字。
	 * 通常在工厂配置的过程中被调用，但是也能被运行时别名注册来使用。因此，一个工厂实现应该
	 * 同步别名访问。
	 * @param beanName the canonical name of the target bean
	 * @param alias the alias to be registered for the bean
	 * @throws BeanDefinitionStoreException if the alias is already in use
	 */
	void registerAlias(String beanName, String alias) throws BeanDefinitionStoreException;

	/**
	 * Resolve all alias target names and aliases registered in this
	 * factory, applying the given StringValueResolver to them.
	 * <p>The value resolver may for example resolve placeholders
	 * in target bean names and even in alias names.
	 * 解析这个工厂中的所有别名的目标名字和注册的别名，应用给定的StringValueResolver到他们。
	 * 值解析器可能解析目标bean名甚至是别名的点位符。
	 * @param valueResolver the StringValueResolver to apply
	 * @since 2.5
	 */
	void resolveAliases(StringValueResolver valueResolver);

	/**
	 * Return a merged BeanDefinition for the given bean name,
	 * merging a child bean definition with its parent if necessary.
	 * Considers bean definitions in ancestor factories as well.
	 * 返回给定bean名字的合并的BeanDefinition，如果有必要合并bean定义的父。
	 * 也考虑祖先工厂的bean定义。
	 * @param beanName the name of the bean to retrieve the merged definition for
	 * @return a (potentially merged) BeanDefinition for the given bean
	 * @throws NoSuchBeanDefinitionException if there is no bean definition with the given name
	 * @since 2.5
	 */
	BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	/**
	 * Determine whether the bean with the given name is a FactoryBean.
	 * 判断给定名字的bean是否是一个FactoryBean。
	 * @param name the name of the bean to check
	 * @return whether the bean is a FactoryBean
	 * ({@code false} means the bean exists but is not a FactoryBean)
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.5
	 */
	boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Explicitly control the current in-creation status of the specified bean.
	 * For container-internal use only.
	 * 显示地控制指定bean的当前的创建状态。
	 * 只用于容器内部使用。
	 * @param beanName the name of the bean
	 * @param inCreation whether the bean is currently in creation
	 * @since 3.1
	 */
	void setCurrentlyInCreation(String beanName, boolean inCreation);

	/**
	 * Determine whether the specified bean is currently in creation.
	 * 判断指定的bean是否正在被创建。
	 * @param beanName the name of the bean
	 * @return whether the bean is currently in creation
	 * @since 2.5
	 */
	boolean isCurrentlyInCreation(String beanName);

	/**
	 * Register a dependent bean for the given bean,
	 * to be destroyed before the given bean is destroyed.
	 * 为给定的bean注册一个依赖的bean,给定的bean被销毁的时候被销毁。
	 * @param beanName the name of the bean
	 * @param dependentBeanName the name of the dependent bean
	 * @since 2.5
	 */
	void registerDependentBean(String beanName, String dependentBeanName);

	/**
	 * Return the names of all beans which depend on the specified bean, if any.
	 * 返回依赖指定的bean的所有bean的名字，如果有的话。
	 * @param beanName the name of the bean
	 * @return the array of dependent bean names, or an empty array if none
	 * @since 2.5
	 */
	String[] getDependentBeans(String beanName);

	/**
	 * Return the names of all beans that the specified bean depends on, if any.
	 * 返回指定bean依赖的所有bean的名字，如果有的话。
	 * @param beanName the name of the bean
	 * @return the array of names of beans which the bean depends on,
	 * or an empty array if none
	 * @since 2.5
	 */
	String[] getDependenciesForBean(String beanName);

	/**
	 * Destroy the given bean instance (usually a prototype instance
	 * obtained from this factory) according to its bean definition.
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 * 根据它的bean定义销毁给定bean实例(通常是从这个工厂获取的原型实例)。
	 * 在销毁的过程中任何抛出的异常应该被捕获，并且被记录到日志，而不是传递给这个方法的调用者。
	 *
	 * @param beanName the name of the bean definition
	 * @param beanInstance the bean instance to destroy
	 */
	void destroyBean(String beanName, Object beanInstance);

	/**
	 * Destroy the specified scoped bean in the current target scope, if any.
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 * 销毁在当前目标范围中的指定的范围bean,如果有的话。
	 * 在销毁的过程中任何抛出的异常应该被捕获，并且被记录到日志，而不是传递给这个方法的调用者。
	 * @param beanName the name of the scoped bean
	 */
	void destroyScopedBean(String beanName);

	/**
	 * Destroy all singleton beans in this factory, including inner beans that have
	 * been registered as disposable. To be called on shutdown of a factory.
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 * 销毁这个工厂中的所有的单例bean,包括被作为一次性的被注册的内部bean。在工厂关闭的时候被调用。
	 * 在销毁的过程中任何抛出的异常应该被捕获，并且被记录到日志，而不是传递给这个方法的调用者。
	 */
	void destroySingletons();

}
