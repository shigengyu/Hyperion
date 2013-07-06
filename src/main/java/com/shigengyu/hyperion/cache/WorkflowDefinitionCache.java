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

package com.shigengyu.hyperion.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.shigengyu.hyperion.common.StringMessage;
import com.shigengyu.hyperion.core.Workflow;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowDefinitionException;
import com.shigengyu.hyperion.core.WorkflowStateException;
import com.shigengyu.hyperion.utils.ReflectionsHelper;

@Service
@Lazy(false)
public class WorkflowDefinitionCache {

	private static WorkflowDefinitionCache instance;

	public static WorkflowDefinitionCache getInstance() {
		return instance;
	}

	private LoadingCache<Class<? extends WorkflowDefinition>, WorkflowDefinition> cache;

	@Value("${hyperion.workflow.cache.definition.timeout.duration}")
	private int timeoutDuration;

	@Value("${hyperion.workflow.cache.definition.timeout.timeunit}")
	private TimeUnit timeoutTimeUnit;

	@Resource
	private WorkflowDefinitionCacheLoader workflowDefinitionCacheLoader;

	public <T extends WorkflowDefinition> WorkflowDefinition get(final Class<T> workflowDefinitionClass) {
		try {
			return cache.get(workflowDefinitionClass);
		}
		catch (final ExecutionException e) {
			throw new WorkflowStateException("Failed to get workflow definition by type [{}]", workflowDefinitionClass,
					e);
		}
	}

	public int getTimeoutDuration() {
		return timeoutDuration;
	}

	@PostConstruct
	private void initialize() {
		cache = CacheBuilder.newBuilder().expireAfterAccess(timeoutDuration, timeoutTimeUnit)
				.build(workflowDefinitionCacheLoader);

		instance = this;
	}

	public void loadPackages(final String... packageNames) {
		final Reflections reflections = ReflectionsHelper.createReflections(packageNames);
		for (final Class<?> clazz : reflections.getTypesAnnotatedWith(Workflow.class)) {
			if (!WorkflowDefinition.class.isAssignableFrom(clazz)) {
				final String message = StringMessage.with(
						"Class [{}] with @Workflow annotation does not extend [{}] class", clazz.getName(),
						WorkflowDefinition.class.getName());
				throw new WorkflowDefinitionException(message);
			}

			@SuppressWarnings("unchecked")
			final Class<WorkflowDefinition> workflowDefinitionClass = (Class<WorkflowDefinition>) clazz;
			final WorkflowDefinition workflowDefinition = this.get(workflowDefinitionClass);
		}
	}
}
