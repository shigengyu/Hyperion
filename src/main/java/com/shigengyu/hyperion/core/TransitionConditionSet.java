package com.shigengyu.hyperion.core;

import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.shigengyu.hyperion.cache.TransitionConditionCache;

public class TransitionConditionSet implements Iterable<TransitionCondition> {

	public static TransitionConditionSet empty() {
		return new TransitionConditionSet();
	}

	public static TransitionConditionSet from(Class<? extends TransitionCondition>[] conditionClasses) {
		return new TransitionConditionSet(conditionClasses);
	}

	private ImmutableSet<TransitionCondition> conditions;

	private TransitionConditionSet() {
		conditions = ImmutableSet.of();
	}

	private TransitionConditionSet(Class<? extends TransitionCondition>[] conditionClasses) {
		conditions = ImmutableSet.copyOf(Lists.transform(Lists.newArrayList(conditionClasses),
				new Function<Class<? extends TransitionCondition>, TransitionCondition>() {

					@Override
					public TransitionCondition apply(@Nullable Class<? extends TransitionCondition> input) {
						return TransitionConditionCache.getInstance().get(input);
					}
				}));
	}

	@Override
	public Iterator<TransitionCondition> iterator() {
		return conditions.iterator();
	}
}
