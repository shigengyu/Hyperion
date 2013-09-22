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

import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.shigengyu.hyperion.core.State;
import com.shigengyu.hyperion.core.WorkflowState;
import com.shigengyu.hyperion.core.WorkflowStateException;
import com.shigengyu.hyperion.utils.ReflectionsHelper;

@Service
@Lazy(false)
public class WorkflowStateCache {

	private static WorkflowStateCache instance;

	private static Logger LOGGER = LoggerFactory.getLogger(WorkflowStateCache.class);

	public static WorkflowStateCache getInstance() {
		return instance;
	}

	private LoadingCache<Class<? extends WorkflowState>, WorkflowState> cache;

	@Resource
	private WorkflowStateCacheLoader workflowStateCacheLoader;

	private WorkflowStateCache() {
	}

	public <T extends WorkflowState> WorkflowState get(final Class<T> workflowStateClass) {
		try {
			return cache.get(workflowStateClass);
		}
		catch (final ExecutionException e) {
			throw new WorkflowStateException("Failed to get workflow state by type [{}]", workflowStateClass, e);
		}
	}

	public ImmutableSet<WorkflowState> getAll() {
		return ImmutableSet.copyOf(cache.asMap().values());
	}

	@PostConstruct
	private void initialize() {
		cache = CacheBuilder.newBuilder().build(workflowStateCacheLoader);
		instance = this;
	}

	public void scanPackages(final String... packageNames) {
		Set<Class<?>> stateTypes = ReflectionsHelper.createReflections(packageNames).getTypesAnnotatedWith(State.class);
		for (Class<?> clazz : stateTypes) {
			if (WorkflowState.class.isAssignableFrom(clazz)) {
				@SuppressWarnings("unchecked")
				Class<? extends WorkflowState> workflowStateClass = (Class<? extends WorkflowState>) clazz;
				WorkflowState state = WorkflowStateCache.getInstance().get(workflowStateClass);

				LOGGER.info("Workflow state [{}] loaded into cache", state);
			}
			else {
				throw new WorkflowStateException("Workflow state [{}] does not inherit [{}]", clazz.getName(),
						WorkflowState.class.getName());
			}
		}
	}
}
