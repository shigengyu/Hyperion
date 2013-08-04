/*******************************************************************************
 * Copyright 2013 Gengyu Shi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.shigengyu.hyperion.common;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.google.common.collect.Lists;

public abstract class SpringBeanSet<T> implements BeanPostProcessor {

	private final List<T> beans;

	private final Class<? extends T> clazz;

	protected SpringBeanSet(Class<? extends T> clazz) {
		this.clazz = clazz;
		beans = Lists.newArrayList();
	}

	public List<T> getBeans() {
		return beans;
	}

	@Override
	public final Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (clazz.isAssignableFrom(bean.getClass())) {
			beans.add((T) bean);
		}
		return bean;
	}
}
