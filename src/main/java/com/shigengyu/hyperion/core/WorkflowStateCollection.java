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

package com.shigengyu.hyperion.core;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.shigengyu.hyperion.cache.WorkflowStateCache;

public class WorkflowStateCollection {

	private final Collection<WorkflowState> workflowStates;

	public WorkflowStateCollection() {
		workflowStates = Lists.newArrayList();
	}

	protected WorkflowStateCollection(
			final Collection<WorkflowState> workflowStates) {
		this.workflowStates = workflowStates;
	}

	protected WorkflowStateCollection(final WorkflowState... workflowStates) {
		this.workflowStates = Lists.newArrayList(workflowStates);
	}

	public WorkflowStateCollection merge(final Class<?>... workflowStateClasses) {
		return this.merge(Lists.transform(Arrays.asList(workflowStateClasses),
				new Function<Class<?>, WorkflowState>() {

					@Override
					@SuppressWarnings("unchecked")
					public WorkflowState apply(@Nullable final Class<?> input) {
						if (!input.isAssignableFrom(WorkflowState.class)) {
							return null;
						}

						return WorkflowStateCache.getInstance().get(
								(Class<? extends WorkflowState>) input);
					}

				}).toArray(new WorkflowState[0]));
	}

	public WorkflowStateCollection merge(final WorkflowState... workflowStates) {
		if (workflowStates == null || workflowStates.length == 0) {
			return this;
		}

		for (final WorkflowState workflowState : workflowStates) {
			if (workflowState != null) {
				this.workflowStates.add(workflowState);
			}
		}

		return this;
	}
}
