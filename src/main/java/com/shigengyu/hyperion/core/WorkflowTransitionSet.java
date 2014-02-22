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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class WorkflowTransitionSet implements Iterable<WorkflowTransition> {

	private static final WorkflowTransitionSet EMPTY = new WorkflowTransitionSet(
			ImmutableList.<WorkflowTransition> of());

	public static WorkflowTransitionSet empty() {
		return EMPTY;
	}

	public static WorkflowTransitionSet with(Iterable<WorkflowTransition> transitions) {
		return new WorkflowTransitionSet(transitions);
	}

	private final ImmutableList<WorkflowTransition> transitions;

	private WorkflowTransitionSet(Iterable<WorkflowTransition> elements) {
		transitions = ImmutableList.copyOf(elements);
	}

	public WorkflowTransitionSet filter(Predicate<WorkflowTransition> predicate) {
		List<WorkflowTransition> filtered = Lists.newArrayList();
		for (WorkflowTransition transition : transitions) {
			if (predicate.apply(transition)) {
				filtered.add(transition);
			}
		}
		return new WorkflowTransitionSet(filtered);
	}

	public WorkflowTransition first() {
		if (transitions.isEmpty()) {
			return null;
		}
		else {
			return transitions.get(0);
		}
	}

	public WorkflowTransitionSet getAutoTransitions() {
		return filter(new Predicate<WorkflowTransition>() {

			@Override
			public boolean apply(WorkflowTransition input) {
				return input.isAuto();
			}
		});
	}

	public boolean isEmpty() {
		return transitions.isEmpty();
	}

	@Override
	public Iterator<WorkflowTransition> iterator() {
		return transitions.iterator();
	}

	public int size() {
		return transitions.size();
	}

	@Override
	public String toString() {
		return StringUtils.join(transitions, ",");
	}
}
