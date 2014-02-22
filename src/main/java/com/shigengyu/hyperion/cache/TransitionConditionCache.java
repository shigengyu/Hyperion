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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.shigengyu.hyperion.core.TransitionCondition;
import com.shigengyu.hyperion.core.WorkflowStateException;

@Service
public class TransitionConditionCache {

	private static TransitionConditionCache instance;

	public static TransitionConditionCache getInstance() {
		return instance;
	}

	private LoadingCache<Class<? extends TransitionCondition>, TransitionCondition> cache;

	@Resource
	private TransitionConditionClassLoader transitionConditionClassLoader;

	public synchronized <T extends TransitionCondition> TransitionCondition get(final Class<T> transitionConditionClass) {
		try {
			return cache.get(transitionConditionClass);
		}
		catch (final ExecutionException e) {
			throw new WorkflowStateException("Failed to get transition condition by type [{}]",
					transitionConditionClass, e);
		}
	}

	@PostConstruct
	private void initialize() {
		cache = CacheBuilder.newBuilder().build(transitionConditionClassLoader);
		instance = this;
	}
}
