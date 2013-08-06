package com.shigengyu.hyperion.core;

import java.util.Iterator;
import java.util.List;

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

	public WorkflowTransition first() {
		if (transitions.isEmpty()) {
			return null;
		}
		else {
			return transitions.get(0);
		}
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

	public WorkflowTransitionSet where(Predicate<WorkflowTransition> predicate) {
		List<WorkflowTransition> transitions = Lists.newArrayList();
		for (WorkflowTransition transition : transitions) {
			if (predicate.apply(transition)) {
				transitions.add(transition);
			}
		}
		return new WorkflowTransitionSet(transitions);
	}
}
