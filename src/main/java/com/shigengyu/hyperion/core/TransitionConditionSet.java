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