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

package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

/**
 * {@link AutowireCandidateResolver} implementation to use when no annotation
 * support is available. This implementation checks the bean definition only.
 *
 * 当没有注解支持可用时使用的AutowireCandidateResolver实现。这个实现只检查bean定义。
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @since 2.5
 * @see BeanDefinition#isAutowireCandidate()
 */
public class SimpleAutowireCandidateResolver implements AutowireCandidateResolver {

	/**
	 * Determine if the provided bean definition is an autowire candidate.
	 * <p>To be considered a candidate the bean's <em>autowire-candidate</em>
	 * attribute must not have been set to 'false'.
	 *
	 * 判断提供的bean定义是否是一个自动注入的候选者。
	 * 要被认为是一个候选者bean的autowire-candidate属性必须不能被设置为'false'。
	 *
	 */
	@Override
	public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		return bdHolder.getBeanDefinition().isAutowireCandidate();
	}

	@Override
	public Object getSuggestedValue(DependencyDescriptor descriptor) {
		return null;
	}

	@Override
	public Object getLazyResolutionProxyIfNecessary(DependencyDescriptor descriptor, String beanName) {
		return null;
	}

}
