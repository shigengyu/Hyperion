package com.shigengyu.hyperion.core;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.jcip.annotations.Immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@Immutable
public class TransitionCompensationList implements Iterable<TransitionCompensation> {

	public static TransitionCompensationList from(Compensation... compensations) {

		List<TransitionCompensation> list = Lists.newArrayList();

		for (Compensation compensation : compensations) {
			list.add(new TransitionCompensation(compensation.exception(), compensation.compensator()));
		}

		Collections.sort(list, new Comparator<TransitionCompensation>() {

			@Override
			public int compare(TransitionCompensation first, TransitionCompensation second) {
				if (first.getException() == second.getException()) {
					return 0;
				}

				return first.getException().isAssignableFrom(second.getException()) ? 1 : -1;
			}
		});

		return new TransitionCompensationList(list);
	}

	private final List<TransitionCompensation> transitionCompensations;

	private TransitionCompensationList(Collection<TransitionCompensation> transitionCompensations) {
		this.transitionCompensations = ImmutableList.copyOf(transitionCompensations);
	}

	@Override
	public Iterator<TransitionCompensation> iterator() {
		return transitionCompensations.iterator();
	}
}
