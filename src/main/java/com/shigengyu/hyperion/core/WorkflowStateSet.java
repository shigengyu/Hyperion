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
package com.shigengyu.hyperion.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.jcip.annotations.Immutable;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.shigengyu.hyperion.entities.WorkflowStateEntity;

@Immutable
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
						return WorkflowState.of(input);
					}
				}));
	}

	public static WorkflowStateSet from(Collection<String> workflowStateIds) {

		List<WorkflowState> workflowStates = Lists.transform(Lists.newArrayList(workflowStateIds),
				new Function<String, WorkflowState>() {

					@Override
					public WorkflowState apply(String input) {
						return WorkflowState.byId(input);
					}
				});

		return WorkflowStateSet.from(workflowStates);
	}

	public static WorkflowStateSet from(Iterable<WorkflowState> workflowStates) {
		return new WorkflowStateSet(workflowStates);
	}

	public static WorkflowStateSet from(final WorkflowState... workflowStates) {
		return new WorkflowStateSet(workflowStates);
	}

	private final ImmutableSet<WorkflowState> workflowStates;

	private WorkflowStateSet(final Iterable<WorkflowState> workflowStates) {
		this.workflowStates = ImmutableSet.copyOf(workflowStates);
	}

	private WorkflowStateSet(final WorkflowState... workflowStates) {
		this.workflowStates = ImmutableSet.copyOf(workflowStates);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof WorkflowStateSet)) {
			return false;
		}

		return workflowStates.hashCode() == ((WorkflowStateSet) obj).workflowStates.hashCode();
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		for (WorkflowState workflowState : workflowStates) {
			hashCode ^= workflowState.hashCode();
		}
		return hashCode;
	}

	public boolean isSameWith(WorkflowStateSet workflowStateSet) {
		return CollectionUtils.isEqualCollection(workflowStates, workflowStateSet.workflowStates);
	}

	public boolean isSubSetOf(WorkflowStateSet workflowStateSet) {
		return CollectionUtils.isSubCollection(workflowStates, workflowStateSet.workflowStates);
	}

	public boolean isSuperSetOf(WorkflowStateSet workflowStateSet) {
		return CollectionUtils.isSubCollection(workflowStateSet.workflowStates, workflowStates);
	}

	@Override
	public Iterator<WorkflowState> iterator() {
		return workflowStates.iterator();
	}

	@SuppressWarnings("unchecked")
	public WorkflowStateSet merge(final Class<? extends WorkflowState>... workflowStateClasses) {
		return merge(Lists.transform(Arrays.asList(workflowStateClasses),
				new Function<Class<? extends WorkflowState>, WorkflowState>() {

					@Override
					public WorkflowState apply(final Class<? extends WorkflowState> input) {
						return WorkflowState.of(input);
					}

				}));
	}

	public WorkflowStateSet merge(final Iterable<WorkflowState> workflowStates) {
		if (workflowStates == null || !workflowStates.iterator().hasNext()) {
			return this;
		}

		Set<WorkflowState> states = Sets.newHashSet(workflowStates);
		for (final WorkflowState workflowState : workflowStates) {
			if (workflowState != null) {
				states.add(workflowState);
			}
		}
		return new WorkflowStateSet(states);
	}

	public WorkflowStateSet merge(WorkflowStateSet workflowStateSet) {
		return merge(workflowStateSet.workflowStates);
	}

	public WorkflowStateSet remove(Class<? extends WorkflowState> workflowState) {
		return remove(WorkflowState.of(workflowState));
	}

	public WorkflowStateSet remove(Iterable<WorkflowState> workflowStates) {
		Set<WorkflowState> states = Sets.newHashSet(this.workflowStates);
		for (WorkflowState workflowState : Lists.newArrayList(workflowStates)) {
			states.remove(workflowState);
		}
		return new WorkflowStateSet(states);
	}

	public WorkflowStateSet remove(WorkflowState workflowState) {
		Set<WorkflowState> states = Sets.newHashSet(workflowStates);
		states.remove(workflowState);
		return new WorkflowStateSet(states);
	}

	public int size() {
		return workflowStates.size();
	}

	public List<WorkflowStateEntity> toEntityList() {
		return Lists.transform(Lists.newArrayList(workflowStates), new Function<WorkflowState, WorkflowStateEntity>() {

			@Override
			public WorkflowStateEntity apply(WorkflowState workflowState) {
				return workflowState.toEntity();
			}
		});
	}

	@Override
	public String toString() {
		return StringUtils.join(workflowStates, ",");
	}
}
