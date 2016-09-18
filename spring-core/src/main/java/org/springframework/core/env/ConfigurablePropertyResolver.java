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

package org.springframework.core.env;

import org.springframework.core.convert.support.ConfigurableConversionService;

/**
 * Configuration interface to be implemented by most if not all {@link PropertyResolver}
 * types. Provides facilities for accessing and customizing the
 * {@link org.springframework.core.convert.ConversionService ConversionService}
 * used when converting property values from one type to another.
 *
 * 如果不是所有的PropertyResolver类型被大部分实现的配置接口。提供工具用于访问和自定义ConversionService
 * 当转换属性值从一种类型到另一种类型时被使用。
 *
 * @author Chris Beams
 * @since 3.1
 */
public interface ConfigurablePropertyResolver extends PropertyResolver {

	/**
	 * Return the {@link ConfigurableConversionService} used when performing type
	 * conversions on properties.
	 * <p>The configurable nature of the returned conversion service allows for
	 * the convenient addition and removal of individual {@code Converter} instances:
	 * <pre class="code">
	 * ConfigurableConversionService cs = env.getConversionService();
	 * cs.addConverter(new FooConverter());
	 * </pre>
	 * @see PropertyResolver#getProperty(String, Class)
	 * @see org.springframework.core.convert.converter.ConverterRegistry#addConverter
	 *
	 * 返回当在属性值上执行类型转换时使用的ConfigurableConversionService。
	 * 返回的转换服务的可配置属性允许方便的添加和删除单独的Converter实例:
	 */
	ConfigurableConversionService getConversionService();

	/**
	 * Set the {@link ConfigurableConversionService} to be used when performing type
	 * conversions on properties.
	 * <p><strong>Note:</strong> as an alternative to fully replacing the
	 * {@code ConversionService}, consider adding or removing individual
	 * {@code Converter} instances by drilling into {@link #getConversionService()}
	 * and calling methods such as {@code #addConverter}.
	 * @see PropertyResolver#getProperty(String, Class)
	 * @see #getConversionService()
	 * @see org.springframework.core.convert.converter.ConverterRegistry#addConverter
	 *
	 * 设置当在属性上执行类型转换时被使用的ConfigurableConversionService。
	 * 注意:作为一个完全替代ConversionService的选择，考虑增加或删除单个的Converter实例通过
	 * 进入到getConversionService()并且调用例如addConverter的方法。
	 */
	void setConversionService(ConfigurableConversionService conversionService);

	/**
	 * Set the prefix that placeholders replaced by this resolver must begin with.
	 *
	 * 设置被这个解析器替换的占位符必须开头的前缀。
	 */
	void setPlaceholderPrefix(String placeholderPrefix);

	/**
	 * Set the suffix that placeholders replaced by this resolver must end with.
	 *
	 * 设置被这个解析器替换的占位符必须开头的后缀。
	 */
	void setPlaceholderSuffix(String placeholderSuffix);

	/**
	 * Specify the separating character between the placeholders replaced by this
	 * resolver and their associated default value, or {@code null} if no such
	 * special character should be processed as a value separator.
	 *
	 * 指定被这个解析者替换的占位符和它们相关的默认值之间分隔符，或null如果没有这样特殊的字符应该被作为
	 * 值分隔符处理。
	 */
	void setValueSeparator(String valueSeparator);

	/**
	 * Set whether to throw an exception when encountering an unresolvable placeholder
	 * nested within the value of a given property. A {@code false} value indicates strict
	 * resolution, i.e. that an exception will be thrown. A {@code true} value indicates
	 * that unresolvable nested placeholders should be passed through in their unresolved
	 * ${...} form.
	 * <p>Implementations of {@link #getProperty(String)} and its variants must inspect
	 * the value set here to determine correct behavior when property values contain
	 * unresolvable placeholders.
	 * @since 3.2
	 *
	 * 设置是否抛出一个异常当遇到一个不可解析的占位符嵌套在给定的属性值中。
	 * false值表示严格的解析，也就是说，将会抛出异常。true表示不可解析的嵌套的占位符应该被通过以他们的不可解析的
	 * ${...}形式。
	 *
	 * getProperty(String)的实现和它的变种必须检查这里设置的值来确定正确的行为，当属性值包含不可解析的占位符的时候。
	 *
	 */
	void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders);

	/**
	 * Specify which properties must be present, to be verified by
	 * {@link #validateRequiredProperties()}.
	 *
	 * 设置那一个属性必须出现，被validateRequiredProperties()检查。
	 */
	void setRequiredProperties(String... requiredProperties);

	/**
	 * Validate that each of the properties specified by
	 * {@link #setRequiredProperties} is present and resolves to a
	 * non-{@code null} value.
	 * @throws MissingRequiredPropertiesException if any of the required
	 * properties are not resolvable.
	 *
	 * 检查每一个被setRequiredProperties指定的属性是存在的并且解析为一个非null的值。
	 * 抛出MissingRequiredPropertiesException如果任何要求的属性是不可解析的。
	 */
	void validateRequiredProperties() throws MissingRequiredPropertiesException;

}
