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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.shigengyu.hyperion.common.ListHashMap;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowTransition;
import com.shigengyu.hyperion.core.WorkflowTransitionException;

@Service
public class WorkflowTransitionCache {

	private static WorkflowTransitionCache instance;

	public static WorkflowTransitionCache getInstance() {
		return instance;
	}

	private LoadingCache<WorkflowDefinition, ImmutableList<WorkflowTransition>> cache;

	private final Map<WorkflowDefinition, ListHashMap<String, WorkflowTransition>> map = Maps.newHashMap();

	@Resource
	private WorkflowTransitionCacheLoader workflowTransitionCacheLoader;

	public ImmutableList<WorkflowTransition> get(final WorkflowDefinition workflowDefinition) {
		try {
			ImmutableList<WorkflowTransition> transitions = cache.getIfPresent(workflowDefinition);
			if (transitions != null) {
				return transitions;
			}

			transitions = cache.get(workflowDefinition);

			// Cache transitions by workflow definition and transition name
			ListHashMap<String, WorkflowTransition> listHashMap = new ListHashMap<String, WorkflowTransition>();
			listHashMap.addAll(transitions, new Function<WorkflowTransition, String>() {

				@Override
				public String apply(WorkflowTransition input) {
					return input.getName();
				}
			});
			map.put(workflowDefinition, listHashMap);

			return transitions;
		}
		catch (final ExecutionException e) {
			throw new WorkflowTransitionException("Failed to get workflow transitions by definition [{}]",
					workflowDefinition.getName(), e);
		}
	}

	public ImmutableList<WorkflowTransition> get(final WorkflowDefinition workflowDefinition, String transitionName) {
		List<WorkflowTransition> list = map.get(workflowDefinition).get(transitionName);
		if (list != null) {
			return ImmutableList.copyOf(list);
		}
		else {
			return ImmutableList.of();
		}
	}

	@PostConstruct
	private void initialize() {
		cache = CacheBuilder.newBuilder().build(workflowTransitionCacheLoader);
		instance = this;
	}
}
