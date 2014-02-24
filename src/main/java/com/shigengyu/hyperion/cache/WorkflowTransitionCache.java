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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.shigengyu.common.ListHashMap;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowStateSet;
import com.shigengyu.hyperion.core.WorkflowTransition;
import com.shigengyu.hyperion.core.WorkflowTransitionCollection;
import com.shigengyu.hyperion.core.WorkflowTransitionException;

@Service
public class WorkflowTransitionCache {

	private static WorkflowTransitionCache instance;

	private static Logger LOGGER = LoggerFactory.getLogger(WorkflowTransitionCache.class);

	public static WorkflowTransitionCache getInstance() {
		return instance;
	}

	private LoadingCache<WorkflowDefinition, ImmutableList<WorkflowTransition>> cache;

	private final Map<WorkflowDefinition, ListHashMap<String, WorkflowTransition>> transitionsByName = Maps
			.newHashMap();

	private final Map<WorkflowDefinition, ListHashMap<WorkflowStateSet, WorkflowTransition>> transitionsByStates = Maps
			.newHashMap();

	@Resource
	private WorkflowTransitionCacheLoader workflowTransitionCacheLoader;

	public WorkflowTransitionCollection get(final WorkflowDefinition workflowDefinition) {
		try {
			ImmutableList<WorkflowTransition> transitions = cache.getIfPresent(workflowDefinition);
			if (transitions != null) {
				return WorkflowTransitionCollection.copyOf(transitions);
			}

			transitions = cache.get(workflowDefinition);

			for (WorkflowTransition transition : transitions) {
				LOGGER.info("Workflow transition [{}] loaded into cache", transition);
			}

			// Cache transitions by workflow definition and transition name
			transitionsByName.put(
					workflowDefinition,
					ListHashMap.<String, WorkflowTransition> newListHashMap().addAll(transitions,
							new Function<WorkflowTransition, String>() {

								@Override
								public String apply(WorkflowTransition input) {
									return input.getName();
								}
							}));

			transitionsByStates.put(
					workflowDefinition,
					ListHashMap.<WorkflowStateSet, WorkflowTransition> newListHashMap().addAll(transitions,
							new Function<WorkflowTransition, WorkflowStateSet>() {

								@Override
								public WorkflowStateSet apply(WorkflowTransition input) {
									return input.getFromStates();
								}
							}));

			return WorkflowTransitionCollection.copyOf(transitions);
		}
		catch (final ExecutionException e) {
			throw new WorkflowTransitionException("Failed to get workflow transitions by definition [{}]",
					workflowDefinition.getName(), e);
		}
	}

	public WorkflowTransitionCollection get(final WorkflowDefinition workflowDefinition, String transitionName) {
		List<WorkflowTransition> list = transitionsByName.get(workflowDefinition).get(transitionName);
		if (list != null) {
			return WorkflowTransitionCollection.copyOf(list);
		}
		else {
			return WorkflowTransitionCollection.empty();
		}
	}

	public WorkflowTransitionCollection get(final WorkflowDefinition workflowDefinition,
			final WorkflowStateSet workflowStateSet) {
		ListHashMap<WorkflowStateSet, WorkflowTransition> map = transitionsByStates.get(workflowDefinition);
		List<WorkflowTransition> list = map.get(workflowStateSet);

		if (list != null) {
			return WorkflowTransitionCollection.copyOf(list);
		}
		else {
			return WorkflowTransitionCollection.empty();
		}
	}

	@PostConstruct
	private void initialize() {
		cache = CacheBuilder.newBuilder().build(workflowTransitionCacheLoader);
		instance = this;
	}
}
