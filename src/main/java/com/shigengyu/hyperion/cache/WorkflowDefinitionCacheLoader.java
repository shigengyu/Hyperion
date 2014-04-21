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

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheLoader;
import com.shigengyu.common.StringMessage;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowDefinitionException;
import com.shigengyu.hyperion.dao.WorkflowDefinitionDao;

@Service
public class WorkflowDefinitionCacheLoader extends CacheLoader<Class<? extends WorkflowDefinition>, WorkflowDefinition>
		implements ApplicationContextAware {

	@Resource
	private WorkflowDefinitionDao workflowDefinitionDao;

	private ApplicationContext applicationContext;

	@Override
	public WorkflowDefinition load(final Class<? extends WorkflowDefinition> key) {

		WorkflowDefinition workflowDefinition = null;
		try {
			workflowDefinition = key.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e) {
			throw new WorkflowDefinitionException(StringMessage.with(
					"Failed to create workflow definition instance of type [{}]", key.getName()), e);
		}

		workflowDefinitionDao.saveOrUpdate(workflowDefinition.toEntity());

		// Autowire workflow definition instance
		applicationContext.getAutowireCapableBeanFactory().autowireBean(workflowDefinition);

		// Load all transitions in this definition to cache.
		WorkflowTransitionCache.getInstance().get(workflowDefinition);

		return workflowDefinition;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
