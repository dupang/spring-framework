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

package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;

/**
 * The root interface for accessing a Spring bean container.
 * This is the basic client view of a bean container;
 * further interfaces such as {@link ListableBeanFactory} and
 * {@link org.springframework.beans.factory.config.ConfigurableBeanFactory}
 * are available for specific purposes.
 *
 * 访问一个Spring bean容器的根接口。
 * 这个一个bean容器的基本客户端视图。
 * 更进一步的接口，例如ListableBeanFactory和ConfigurableBeanFactory可以用于特定目的。
 *
 *
 * <p>This interface is implemented by objects that hold a number of bean definitions,
 * each uniquely identified by a String name. Depending on the bean definition,
 * the factory will return either an independent instance of a contained object
 * (the Prototype design pattern), or a single shared instance (a superior
 * alternative to the Singleton design pattern, in which the instance is a
 * singleton in the scope of the factory). Which type of instance will be returned
 * depends on the bean factory configuration: the API is the same. Since Spring
 * 2.0, further scopes are available depending on the concrete application
 * context (e.g. "request" and "session" scopes in a web environment).
 *
 * 这个接口被持有一些bean定义的对象实现，每一个bean定义被一个唯一的String名字标记。根据bean定义，
 * 工厂将返回一个独立的包含对象的实例(原型设计模式),或者一个单独共享的实例(单例设计模式的替代者，在工厂的范围
 * 中实例是一个单例的)。实例的类型将被返回根据bean工厂的配置:API是相同的。从Spring2.0开始，
 * 根据创建应用上下文更多的范围可用。(例如，在web环境中的"request"和"session"范围)
 *
 * <p>The point of this approach is that the BeanFactory is a central registry
 * of application components, and centralizes configuration of application
 * components (no more do individual objects need to read properties files,
 * for example). See chapters 4 and 11 of "Expert One-on-One J2EE Design and
 * Development" for a discussion of the benefits of this approach.
 *
 * 这种方法的关键是BeanFactory是一个应用程序组件的中央登记处，并且集中配置应用组件(不需要独立地对象
 * 来读取属性文件)。参考"Expert One-on-One J2EE Design and Development"的第四章和11章关于
 * 这种方法的好处的讨论。
 *
 * <p>Note that it is generally better to rely on Dependency Injection
 * ("push" configuration) to configure application objects through setters
 * or constructors, rather than use any form of "pull" configuration like a
 * BeanFactory lookup. Spring's Dependency Injection functionality is
 * implemented using this BeanFactory interface and its subinterfaces.
 *
 * 注意通常依靠依赖注入通过setter或构造器来配置应用对象，比使用任何形式的"pull"配置像beanfactory搜索。
 * Spring的依赖注入功能被实现使用这个BeanFactotyr接口和它的字接口。
 *
 * <p>Normally a BeanFactory will load bean definitions stored in a configuration
 * source (such as an XML document), and use the {@code org.springframework.beans}
 * package to configure the beans. However, an implementation could simply return
 * Java objects it creates as necessary directly in Java code. There are no
 * constraints on how the definitions could be stored: LDAP, RDBMS, XML,
 * properties file, etc. Implementations are encouraged to support references
 * amongst beans (Dependency Injection).
 *
 * 通常一个BeanFactory将加载存储在一个配置源的bean定义(例如一个XML文档)，然后使用beans包来
 * 配置bean。然而一个实现可能简单地返回它创建的Java对象如果有必要。关于怎么存储定义没有限制:LDAP，RDBMS,
 * XML，属性文件，等等。实现被鼓励来支持bean之间的引用(依赖注入)。
 *
 * <p>In contrast to the methods in {@link ListableBeanFactory}, all of the
 * operations in this interface will also check parent factories if this is a
 * {@link HierarchicalBeanFactory}. If a bean is not found in this factory instance,
 * the immediate parent factory will be asked. Beans in this factory instance
 * are supposed to override beans of the same name in any parent factory.
 *
 * 和ListableBeanFactory中的方法相比，所有这个接口中的方法也将检查父工厂如果这是一个HierarchicalBeanFactory。
 * 如果在这个工厂实例中没有发现bean，当前的父工厂将被访问。这个工厂实例中的bean可以覆盖在父工厂中任何相同名字的bean。
 *
 * <p>Bean factory implementations should support the standard bean lifecycle interfaces
 * as far as possible. The full set of initialization methods and their standard order is:<br>
 * 1. BeanNameAware's {@code setBeanName}<br>
 * 2. BeanClassLoaderAware's {@code setBeanClassLoader}<br>
 * 3. BeanFactoryAware's {@code setBeanFactory}<br>
 * 4. ResourceLoaderAware's {@code setResourceLoader}
 * (only applicable when running in an application context)<br>
 * 5. ApplicationEventPublisherAware's {@code setApplicationEventPublisher}
 * (only applicable when running in an application context)<br>
 * 6. MessageSourceAware's {@code setMessageSource}
 * (only applicable when running in an application context)<br>
 * 7. ApplicationContextAware's {@code setApplicationContext}
 * (only applicable when running in an application context)<br>
 * 8. ServletContextAware's {@code setServletContext}
 * (only applicable when running in a web application context)<br>
 * 9. {@code postProcessBeforeInitialization} methods of BeanPostProcessors<br>
 * 10. InitializingBean's {@code afterPropertiesSet}<br>
 * 11. a custom init-method definition<br>
 * 12. {@code postProcessAfterInitialization} methods of BeanPostProcessors
 * Bean工厂的实现应该尽可能地支持标准的bean生命周期接口。完整的初始化方法和他们的标准顺序是：
 *
 * <p>On shutdown of a bean factory, the following lifecycle methods apply:<br>
 * 1. DisposableBean's {@code destroy}<br>
 * 2. a custom destroy-method definition
 *
 * 当关闭一个bean工厂，下面的生命周期方法被应用:
 * 1. 一次性bean的destroy方法。
 * 2. 一个自定义销毁方法定义
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 13 April 2001
 * @see BeanNameAware#setBeanName
 * @see BeanClassLoaderAware#setBeanClassLoader
 * @see BeanFactoryAware#setBeanFactory
 * @see org.springframework.context.ResourceLoaderAware#setResourceLoader
 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher
 * @see org.springframework.context.MessageSourceAware#setMessageSource
 * @see org.springframework.context.ApplicationContextAware#setApplicationContext
 * @see org.springframework.web.context.ServletContextAware#setServletContext
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization
 * @see InitializingBean#afterPropertiesSet
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getInitMethodName
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization
 * @see DisposableBean#destroy
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getDestroyMethodName
 */
public interface BeanFactory {

	/**
	 * Used to dereference a {@link FactoryBean} instance and distinguish it from
	 * beans <i>created</i> by the FactoryBean. For example, if the bean named
	 * {@code myJndiObject} is a FactoryBean, getting {@code &myJndiObject}
	 * will return the factory, not the instance returned by the factory.
	 * 用来引用一个FactoryBean的实例并且把它和被FactoryBean创建的bean区分开。例如，
	 * 如果命名为myJndiObject的bean是FactoryBean，获取&myJndiObject将返回工厂，而不是
	 * 被这个工厂返回的实例。
	 */
	String FACTORY_BEAN_PREFIX = "&";


	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>This method allows a Spring BeanFactory to be used as a replacement for the
	 * Singleton or Prototype design pattern. Callers may retain references to
	 * returned objects in the case of Singleton beans.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 *
	 * 返回一个实例，它可能是共享或独立的于指定的bean。
	 * 这个方法允许一个Spring的BeanFactory被用来作为单例或原型设计模式的替代品。
	 * 调用者可以持有返回对象引用在单例bean的时候。
	 * 转换别名为相应的规范的bean名字。
	 * 将访问父工厂如果bean不能在这个工厂实例中找到。
	 *
	 * @param name the name of the bean to retrieve
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no bean definition
	 * with the specified name
	 * @throws BeansException if the bean could not be obtained
	 */
	Object getBean(String name) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Behaves the same as {@link #getBean(String)}, but provides a measure of type
	 * safety by throwing a BeanNotOfRequiredTypeException if the bean is not of the
	 * required type. This means that ClassCastException can't be thrown on casting
	 * the result correctly, as can happen with {@link #getBean(String)}.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * 返回一个实例，它可能是共享或独立的于指定的bean。
	 * 和getBean(String)的行为一样，但是提供一个类型安全的方式通过抛出一个BeanNotOfRequiredTypeException
	 * 如果bean不是要求的类型。这意为着ClassCastException不能被抛出当正确地转换结果的时候，这可能在getBean(String)
	 * 的时候发生。
	 * 转换别名为相应的规范的bean名字。
	 * 将访问父工厂如果bean不能在这个工厂实例中找到。
	 * @param name the name of the bean to retrieve
	 * @param requiredType type the bean must match. Can be an interface or superclass
	 * of the actual class, or {@code null} for any match. For example, if the value
	 * is {@code Object.class}, this method will succeed whatever the class of the
	 * returned instance.
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanNotOfRequiredTypeException if the bean is not of the required type
	 * @throws BeansException if the bean could not be created
	 */
	<T> T getBean(String name, Class<T> requiredType) throws BeansException;

	/**
	 * Return the bean instance that uniquely matches the given object type, if any.
	 * 返回唯一匹配给定对象类型的bean实例，如果存在。
	 * @param requiredType type the bean must match; can be an interface or superclass.
	 * {@code null} is disallowed.
	 * <p>This method goes into {@link ListableBeanFactory} by-type lookup territory
	 * but may also be translated into a conventional by-name lookup based on the name
	 * of the given type. For more extensive retrieval operations across sets of beans,
	 * use {@link ListableBeanFactory} and/or {@link BeanFactoryUtils}.
	 * @return an instance of the single bean matching the required type
	 * @throws NoSuchBeanDefinitionException if no bean of the given type was found
	 * @throws NoUniqueBeanDefinitionException if more than one bean of the given type was found
	 * @throws BeansException if the bean could not be created
	 * @since 3.0
	 * @see ListableBeanFactory
	 */
	<T> T getBean(Class<T> requiredType) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Allows for specifying explicit constructor arguments / factory method arguments,
	 * overriding the specified default arguments (if any) in the bean definition.
	 *
	 * 返回一个实例，它可能是共享或独立的于指定的bean。
	 * 允许指定的显式的构造参数/工厂方法参数，
	 * 覆盖指定的默认的bean定义中的参数。
	 *
	 * @param name the name of the bean to retrieve
	 * @param args arguments to use when creating a bean instance using explicit arguments
	 * (only applied when creating a new instance as opposed to retrieving an existing one)
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanDefinitionStoreException if arguments have been given but
	 * the affected bean isn't a prototype
	 * @throws BeansException if the bean could not be created
	 * @since 2.5
	 */
	Object getBean(String name, Object... args) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Allows for specifying explicit constructor arguments / factory method arguments,
	 * overriding the specified default arguments (if any) in the bean definition.
	 * <p>This method goes into {@link ListableBeanFactory} by-type lookup territory
	 * but may also be translated into a conventional by-name lookup based on the name
	 * of the given type. For more extensive retrieval operations across sets of beans,
	 * use {@link ListableBeanFactory} and/or {@link BeanFactoryUtils}.
	 *
	 * 返回一个实例，它可能是共享或独立的于指定的bean。
	 * 允许指定的显式的构造参数/工厂方法参数，
	 * 覆盖指定的默认的bean定义中的参数。
	 * 这个方法进入ListableBeanFactory通过类型查找逻辑，但是也可能被转换到一个方便的通过名字查找基于给定类型的
	 * 名字。对于更扩展的检索操作。使用ListableBeanFactory或BeanFactoryUtils。
	 *
	 * @param requiredType type the bean must match; can be an interface or superclass.
	 * {@code null} is disallowed.
	 * @param args arguments to use when creating a bean instance using explicit arguments
	 * (only applied when creating a new instance as opposed to retrieving an existing one)
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanDefinitionStoreException if arguments have been given but
	 * the affected bean isn't a prototype
	 * @throws BeansException if the bean could not be created
	 * @since 4.1
	 */
	<T> T getBean(Class<T> requiredType, Object... args) throws BeansException;


	/**
	 * Does this bean factory contain a bean definition or externally registered singleton
	 * instance with the given name?
	 * <p>If the given name is an alias, it will be translated back to the corresponding
	 * canonical bean name.
	 * <p>If this factory is hierarchical, will ask any parent factory if the bean cannot
	 * be found in this factory instance.
	 * <p>If a bean definition or singleton instance matching the given name is found,
	 * this method will return {@code true} whether the named bean definition is concrete
	 * or abstract, lazy or eager, in scope or not. Therefore, note that a {@code true}
	 * return value from this method does not necessarily indicate that {@link #getBean}
	 * will be able to obtain an instance for the same name.
	 * 是否这个bean工厂包含一个bean定义或外部的注册的给定名字的单例。
	 * 如果给定的名字是一个别名，它将被转换成相应的标准bean名字。
	 * 如果这个工厂是一个层次的，将访问任何父工厂如果bean不能在这个工厂实例中被找到。
	 * 如果匹配给定名字的bean定义或单例被找到，这个方法将会返回true,不管命名的bean定义是具体的或抽象的。
	 * 懒怠或急切的，在不在范围中。因此注意这个方法的true的返回值不一定表示getBean将可以获取一个实例。
	 *
	 * @param name the name of the bean to query
	 * @return whether a bean with the given name is present
	 */
	boolean containsBean(String name);

	/**
	 * Is this bean a shared singleton? That is, will {@link #getBean} always
	 * return the same instance?
	 * <p>Note: This method returning {@code false} does not clearly indicate
	 * independent instances. It indicates non-singleton instances, which may correspond
	 * to a scoped bean as well. Use the {@link #isPrototype} operation to explicitly
	 * check for independent instances.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * 这个bean是否是一个共享的单例，也就是说，getBean()是否总是返回相同的实例？
	 * 注意:这个方法返回fase不能完全表示独立的实例。它表示非单例实例，可能对应一个范围的bean。使用isPrototype
	 * 操作来显式地对独立的实例做检查。
	 *
	 * @param name the name of the bean to query
	 * @return whether this bean corresponds to a singleton instance
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @see #getBean
	 * @see #isPrototype
	 */
	boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Is this bean a prototype? That is, will {@link #getBean} always return
	 * independent instances?
	 * <p>Note: This method returning {@code false} does not clearly indicate
	 * a singleton object. It indicates non-independent instances, which may correspond
	 * to a scoped bean as well. Use the {@link #isSingleton} operation to explicitly
	 * check for a shared singleton instance.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * 是否这个bean是一个原型？也就是说，是否getBean总是返回独立的实例？
	 * 注意:这个方法返回fase不完全表示是一个单例对象。它表示非独立的实例，它可能对应一个范围的bean。使用
	 * isSingleton操作来显式地检查一个单例的实例。
	 *
	 * @param name the name of the bean to query
	 * @return whether this bean will always deliver independent instances
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.0.3
	 * @see #getBean
	 * @see #isSingleton
	 */
	boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Check whether the bean with the given name matches the specified type.
	 * More specifically, check whether a {@link #getBean} call for the given name
	 * would return an object that is assignable to the specified target type.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * 检查给定名字的bean是否匹配指定的类型。更具体地，检查是否给定名字的getBean调用将返回一个对象，这个对象可以
	 * 赋值给指定的目标类型。
	 * 转换别名为相应的规范的bean名字。
	 * 将访问父工厂如果bean不能在这个工厂实例中找到。
	 *
	 * @param name the name of the bean to query
	 * @param typeToMatch the type to match against (as a {@code ResolvableType})
	 * @return {@code true} if the bean type matches,
	 * {@code false} if it doesn't match or cannot be determined yet
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 4.2
	 * @see #getBean
	 * @see #getType
	 */
	boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException;

	/**
	 * Check whether the bean with the given name matches the specified type.
	 * More specifically, check whether a {@link #getBean} call for the given name
	 * would return an object that is assignable to the specified target type.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * 检查是否给定名字的bean匹配指定的类型。更具体地，检查一个对于给定名字的getBean调用将返回一个对象，
	 * 这个对象可能赋值给指定的目标类型。
	 * 转换别名为相应的规范的bean名字。
	 * 将访问父工厂如果bean不能在这个工厂实例中找到。
	 * @param name the name of the bean to query
	 * @param typeToMatch the type to match against (as a {@code Class})
	 * @return {@code true} if the bean type matches,
	 * {@code false} if it doesn't match or cannot be determined yet
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.0.1
	 * @see #getBean
	 * @see #getType
	 */
	boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException;

	/**
	 * Determine the type of the bean with the given name. More specifically,
	 * determine the type of object that {@link #getBean} would return for the given name.
	 * <p>For a {@link FactoryBean}, return the type of object that the FactoryBean creates,
	 * as exposed by {@link FactoryBean#getObjectType()}.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * 确定给定名字的bean的类型。更具体地说，确定给定name的getBean返回的对象的类型。
	 * 对于一个FactoryBean，返回FactoryBean创建一对象的类型，正如FactoryBean.getObjectType()暴露的那样。
	 * 转换别名为相应的规范的bean名字。
	 * 将访问父工厂如果bean不能在这个工厂实例中找到。
	 * @param name the name of the bean to query
	 * @return the type of the bean, or {@code null} if not determinable
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 1.1.2
	 * @see #getBean
	 * @see #isTypeMatch
	 */
	Class<?> getType(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Return the aliases for the given bean name, if any.
	 * All of those aliases point to the same bean when used in a {@link #getBean} call.
	 * <p>If the given name is an alias, the corresponding original bean name
	 * and other aliases (if any) will be returned, with the original bean name
	 * being the first element in the array.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 * 返回给定bean名字的别名，如果有的话。
	 * 所有的这些别名指向相同的bean当在getBean调用中使用。
	 * 如果给定的名字是一个别名，相应的原始bean名字和其它别名将被返回，原始bean名字将是数组中的第一个元素。
	 * 将访问父工厂如果bean不能在这个工厂实例中找到。
	 * @param name the bean name to check for aliases
	 * @return the aliases, or an empty array if none
	 * @see #getBean
	 */
	String[] getAliases(String name);

}
