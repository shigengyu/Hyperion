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

import java.util.Map;
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
import com.google.common.collect.Maps;
import com.shigengyu.hyperion.core.State;
import com.shigengyu.hyperion.core.WorkflowState;
import com.shigengyu.hyperion.core.WorkflowStateException;
import com.shigengyu.hyperion.dao.WorkflowStateDao;
import com.shigengyu.hyperion.utils.ReflectionsHelper;

@Service
@Lazy(false)
public class WorkflowStateCache {

	private static Logger LOGGER = LoggerFactory.getLogger(WorkflowStateCache.class);

	private LoadingCache<Class<? extends WorkflowState>, WorkflowState> cache;

	private final Map<String, WorkflowState> statesById = Maps.newHashMap();

	@Resource
	private WorkflowStateCacheLoader workflowStateCacheLoader;

	@Resource
	private WorkflowStateDao workflowStateDao;

	private WorkflowStateCache() {
	}

	public final WorkflowState byId(final String workflowStateId) {
		return WorkflowState.class.cast(statesById.get(workflowStateId));
	}

	public <T extends WorkflowState> T get(final Class<T> workflowStateClass) {
		try {
			return workflowStateClass.cast(cache.get(workflowStateClass));
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
	}

	public void scanPackages(final String... packageNames) {
		Set<Class<?>> stateTypes = ReflectionsHelper.createReflections(packageNames).getTypesAnnotatedWith(State.class);
		for (Class<?> clazz : stateTypes) {
			if (WorkflowState.class.isAssignableFrom(clazz)) {
				@SuppressWarnings("unchecked")
				Class<? extends WorkflowState> workflowStateClass = (Class<? extends WorkflowState>) clazz;

				// Get the workflow state to allow it to be cached
				WorkflowState state = get(workflowStateClass);

				statesById.put(state.getWorkflowStateId(), state);

				// Save or update the workflow state in database
				workflowStateDao.saveOrUpdate(state.toEntity());

				LOGGER.info("Workflow state [{}] loaded into cache", state);
			}
			else {
				throw new WorkflowStateException("Workflow state [{}] does not inherit [{}]", clazz.getName(),
						WorkflowState.class.getName());
			}
		}
	}
}
