/*******************************************************************************
 * Copyright 2013-2014 Gengyu Shi
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
package com.shigengyu.hyperion.cache;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheLoader;
import com.shigengyu.hyperion.core.TransitionCondition;
import com.shigengyu.hyperion.core.TransitionDefinitionException;

@Service
public class WorkflowTransitionConditionCacheLoader extends
		CacheLoader<Class<? extends TransitionCondition>, TransitionCondition> implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public TransitionCondition load(Class<? extends TransitionCondition> transitionClass) throws Exception {
		if (transitionClass.isInterface() || Modifier.isAbstract(transitionClass.getModifiers())) {
			throw new TransitionDefinitionException("Transition cannot be interface or abstract class");
		}

		Constructor<? extends TransitionCondition> constructor = transitionClass.getConstructor();
		if (constructor == null) {
			throw new TransitionDefinitionException(
					"Cannot get no argument constructor for transition condition class [" + transitionClass.getName()
							+ "]");
		}

		TransitionCondition transitionCondition = constructor.newInstance();

		applicationContext.getAutowireCapableBeanFactory().autowireBean(transitionCondition);

		return transitionCondition;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
