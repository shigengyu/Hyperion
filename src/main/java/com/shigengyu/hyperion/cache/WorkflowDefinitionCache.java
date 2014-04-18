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

import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.shigengyu.common.StringMessage;
import com.shigengyu.hyperion.core.Workflow;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowDefinitionException;
import com.shigengyu.hyperion.core.WorkflowStateException;
import com.shigengyu.hyperion.dao.WorkflowDefinitionDao;
import com.shigengyu.hyperion.utils.ReflectionsHelper;

@Service
@Lazy(false)
public class WorkflowDefinitionCache {

	private static Logger LOGGER = LoggerFactory.getLogger(WorkflowDefinitionCache.class);

	private final LoadingCache<Class<? extends WorkflowDefinition>, WorkflowDefinition> cache;

	@Resource
	private WorkflowDefinitionDao workflowDefinitionDao;

	@Autowired
	private WorkflowDefinitionCache(final WorkflowDefinitionCacheLoader workflowDefinitionCacheLoader) {
		cache = CacheBuilder.newBuilder().build(workflowDefinitionCacheLoader);
	}

	public synchronized <T extends WorkflowDefinition> WorkflowDefinition get(final Class<T> workflowDefinitionClass) {
		try {
			return cache.get(workflowDefinitionClass);
		}
		catch (final ExecutionException e) {
			throw new WorkflowStateException("Failed to get workflow definition by type [{}]", workflowDefinitionClass,
					e);
		}
	}

	public synchronized <T extends WorkflowDefinition> WorkflowDefinition get(final String workflowDefinitionId) {
		for (WorkflowDefinition workflowDefinition : getAll()) {
			if (workflowDefinition.getWorkflowDefinitionId().equals(workflowDefinitionId)) {
				return workflowDefinition;
			}
		}
		return null;
	}

	public ImmutableList<WorkflowDefinition> getAll() {
		return ImmutableList.copyOf(cache.asMap().values());
	}

	public WorkflowDefinitionCache scanPackages(final String... packageNames) {
		final Reflections reflections = ReflectionsHelper.createReflections(packageNames);
		for (final Class<?> clazz : reflections.getTypesAnnotatedWith(Workflow.class)) {
			if (!WorkflowDefinition.class.isAssignableFrom(clazz)) {
				final String message = StringMessage.with(
						"Class [{}] with @Workflow annotation does not extend [{}] class", clazz.getName(),
						WorkflowDefinition.class.getName());
				throw new WorkflowDefinitionException(message);
			}

			if (!WorkflowDefinition.class.isAssignableFrom(clazz)) {
				throw new WorkflowDefinitionException("Workflow definition class [" + clazz.getName()
						+ "] must extend " + WorkflowDefinition.class.getName() + " class");
			}

			@SuppressWarnings("unchecked")
			final Class<WorkflowDefinition> workflowDefinitionClass = (Class<WorkflowDefinition>) clazz;

			// Touch the workflow definition to cache it
			WorkflowDefinition definition = this.get(workflowDefinitionClass);

			// Save or update the workflow definition in database
			workflowDefinitionDao.saveOrUpdate(definition.toEntity());

			LOGGER.info("Workflow definition [{}] loaded into cache", definition);
		}

		return this;
	}
}
