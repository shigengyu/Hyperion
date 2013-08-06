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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.shigengyu.hyperion.cache.WorkflowStateCache;

public class WorkflowStateSet implements Iterable<WorkflowState> {

	public static WorkflowStateSet empty() {
		return new WorkflowStateSet();
	}

	@SafeVarargs
	public static WorkflowStateSet from(Class<? extends WorkflowState>... workflowStates) {
		if (workflowStates == null || workflowStates.length == 0) {
			return WorkflowStateSet.empty();
		}

		return new WorkflowStateSet(Lists.transform(Lists.newArrayList(workflowStates),
				new Function<Class<? extends WorkflowState>, WorkflowState>() {

					@Override
					public WorkflowState apply(Class<? extends WorkflowState> input) {
						return WorkflowStateCache.getInstance().get(input);
					}
				}));
	}

	public static WorkflowStateSet from(Iterable<WorkflowState> workflowStates) {
		return new WorkflowStateSet(workflowStates);
	}

	public static WorkflowStateSet from(final WorkflowState... workflowStates) {
		return new WorkflowStateSet(workflowStates);
	}

	private final List<WorkflowState> workflowStates;

	private WorkflowStateSet() {
		workflowStates = Lists.newArrayList();
	}

	private WorkflowStateSet(final Iterable<WorkflowState> workflowStates) {
		this.workflowStates = Lists.newArrayList(workflowStates);
	}

	private WorkflowStateSet(final WorkflowState... workflowStates) {
		this.workflowStates = Lists.newArrayList(workflowStates);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof WorkflowStateSet)) {
			return false;
		}

		return CollectionUtils.isEqualCollection(workflowStates, ((WorkflowStateSet) obj).workflowStates);
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		for (WorkflowState workflowState : workflowStates) {
			hashCode ^= workflowState.hashCode();
		}
		return hashCode;
	}

	@Override
	public Iterator<WorkflowState> iterator() {
		return workflowStates.iterator();
	}

	public WorkflowStateSet merge(final Class<?>... workflowStateClasses) {
		return this.merge(Lists.transform(Arrays.asList(workflowStateClasses), new Function<Class<?>, WorkflowState>() {

			@Override
			@SuppressWarnings("unchecked")
			public WorkflowState apply(final Class<?> input) {
				if (!input.isAssignableFrom(WorkflowState.class)) {
					return null;
				}

				return WorkflowStateCache.getInstance().get((Class<? extends WorkflowState>) input);
			}

		}));
	}

	public WorkflowStateSet merge(final Iterable<WorkflowState> workflowStates) {
		if (workflowStates == null || workflowStates.iterator().hasNext()) {
			return this;
		}

		for (final WorkflowState workflowState : workflowStates) {
			if (workflowState != null) {
				this.workflowStates.add(workflowState);
			}
		}

		return this;
	}

	public WorkflowStateSet merge(WorkflowStateSet workflowStateSet) {
		merge(workflowStateSet.workflowStates);
		return this;
	}

	public WorkflowStateSet remove(Class<? extends WorkflowState> workflowState) {
		workflowStates.remove(WorkflowState.of(workflowState));
		return this;
	}

	public WorkflowStateSet remove(Iterable<WorkflowState> workflowStates) {
		for (WorkflowState workflowState : workflowStates) {
			remove(workflowState);
		}
		return this;
	}

	public WorkflowStateSet remove(WorkflowState workflowState) {
		workflowStates.remove(workflowState);
		return this;
	}
}
