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
package com.shigengyu.hyperion.cache;

import java.util.concurrent.ExecutionException;

import net.jcip.annotations.Immutable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.shigengyu.hyperion.core.TransitionCondition;
import com.shigengyu.hyperion.core.WorkflowTransitionException;

@Service
@Immutable
public class TransitionConditionCache {

	private final LoadingCache<Class<? extends TransitionCondition>, TransitionCondition> cache;

	@Autowired
	private TransitionConditionCache(final TransitionConditionCacheLoader transitionConditionCacheLoader) {
		cache = CacheBuilder.newBuilder().build(transitionConditionCacheLoader);
	}

	public synchronized <T extends TransitionCondition> TransitionCondition get(final Class<T> transitionConditionClass) {
		try {
			return cache.get(transitionConditionClass);
		}
		catch (ExecutionException e) {
			throw new WorkflowTransitionException("Failed to get transition condition by type [{}]",
					transitionConditionClass, e);
		}
	}
}
