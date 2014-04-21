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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.shigengyu.hyperion.cache.TransitionConditionCache;

public class TransitionConditionSet implements Iterable<TransitionCondition> {

	@Service
	public static class TransitionConditionSetFactory {

		private static TransitionConditionSetFactory instance;

		@Resource
		private TransitionConditionCache transitionConditionCache;

		TransitionConditionSet create(Class<? extends TransitionCondition>[] conditionClasses) {

			ImmutableSet<TransitionCondition> conditions = ImmutableSet.copyOf(Lists.transform(
					Lists.newArrayList(conditionClasses),
					new Function<Class<? extends TransitionCondition>, TransitionCondition>() {

						@Override
						public TransitionCondition apply(Class<? extends TransitionCondition> input) {
							return transitionConditionCache.get(input);
						}
					}));

			return new TransitionConditionSet(conditions);
		}

		@PostConstruct
		private void initialize() {
			instance = this;
		}
	}

	public static TransitionConditionSet empty() {
		return new TransitionConditionSet();
	}

	public static TransitionConditionSet from(Class<? extends TransitionCondition>[] conditionClasses) {
		return TransitionConditionSetFactory.instance.create(conditionClasses);
	}

	private final ImmutableSet<TransitionCondition> conditions;

	private TransitionConditionSet() {
		conditions = ImmutableSet.of();
	}

	TransitionConditionSet(ImmutableSet<TransitionCondition> transitionConditions) {
		conditions = transitionConditions;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TransitionConditionSet other = (TransitionConditionSet) obj;
		if (conditions == null) {
			if (other.conditions != null) {
				return false;
			}
		}
		else if (!CollectionUtils.isEqualCollection(conditions, other.conditions)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 0;
		for (TransitionCondition condition : conditions) {
			result ^= condition.hashCode();
		}
		return result;
	}

	@Override
	public Iterator<TransitionCondition> iterator() {
		return conditions.iterator();
	}
}
