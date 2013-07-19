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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
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

	@Resource
	private WorkflowTransitionCacheLoader workflowTransitionCacheLoader;

	public <T extends WorkflowTransition> ImmutableList<WorkflowTransition> get(
			final WorkflowDefinition workflowDefinition) {
		try {
			ImmutableList<WorkflowTransition> transitions = cache.getIfPresent(workflowDefinition);
			if (transitions != null) {
				return transitions;
			}

			transitions = cache.get(workflowDefinition);
			return transitions;
		}
		catch (final ExecutionException e) {
			throw new WorkflowTransitionException("Failed to get workflow transitions by definition [{}]",
					workflowDefinition.getName(), e);
		}
	}

	@PostConstruct
	private void initialize() {
		cache = CacheBuilder.newBuilder().build(workflowTransitionCacheLoader);
		instance = this;
	}
}
