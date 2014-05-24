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

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.shigengyu.hyperion.HyperionException;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowStateException;

public class LocalWorkflowInstanceCache implements WorkflowInstanceCacheProvider {

	private LoadingCache<Integer, WorkflowInstance> cache;

	@Value("${hyperion.workflow.cache.instance.timeout.duration}")
	private int timeoutDuration;

	@Value("${hyperion.workflow.cache.instance.timeout.timeunit}")
	private TimeUnit timeoutTimeUnit;

	@Resource
	private WorkflowInstanceCacheLoader workflowInstanceCacheLoader;

	private final Map<Integer, ReentrantLock> locks = Maps.newConcurrentMap();

	private LocalWorkflowInstanceCache() {
	}

	@Override
	public <T extends WorkflowInstance> WorkflowInstance acquire(final Integer workflowInstanceId) {
		try {
			ReentrantLock lock = getLock(workflowInstanceId);
			lock.lock();

			WorkflowInstance workflowInstance = cache.get(workflowInstanceId);
			return workflowInstance;
		}
		catch (final ExecutionException e) {
			throw new WorkflowStateException("Failed to get workflow instance [{}]", workflowInstanceId, e);
		}
	}

	private synchronized ReentrantLock getLock(final Integer workflowInstanceId) {
		ReentrantLock lock = locks.get(workflowInstanceId);
		if (lock == null) {
			lock = new ReentrantLock();
			locks.put(workflowInstanceId, lock);
		}
		return lock;
	}

	public int getTimeoutDuration() {
		return timeoutDuration;
	}

	@PostConstruct
	private void initialize() {
		cache = CacheBuilder.newBuilder().expireAfterAccess(timeoutDuration, timeoutTimeUnit)
				.build(workflowInstanceCacheLoader);
	}

	@Override
	public <T extends WorkflowInstance> void release(final T workflowInstance) {
		ReentrantLock lock = getLock(workflowInstance.getWorkflowInstanceId());
		if (!lock.isHeldByCurrentThread()) {
			throw new HyperionException(
					"Cannot release workflow instance [{}] as current thread does not own the lock",
					workflowInstance.getWorkflowInstanceId());
		}
		lock.unlock();
	}
}
