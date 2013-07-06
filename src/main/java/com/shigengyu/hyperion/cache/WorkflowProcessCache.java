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

package com.shigengyu.hyperion.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.shigengyu.hyperion.core.WorkflowProcess;
import com.shigengyu.hyperion.core.WorkflowStateException;

@Service
public class WorkflowProcessCache {

	private static WorkflowProcessCache instance;

	public static WorkflowProcessCache getInstance() {
		return instance;
	}

	private LoadingCache<Integer, WorkflowProcess> cache;

	@Value("${hyperion.workflow.cache.process.timeout.duration}")
	private int timeoutDuration;

	@Value("${hyperion.workflow.cache.process.timeout.timeunit}")
	private TimeUnit timeoutTimeUnit;

	@Resource
	private WorkflowProcessCacheLoader WorkflowProcessCacheLoader;

	public <T extends WorkflowProcess> WorkflowProcess get(final Integer workflowProcessId) {
		try {
			return cache.get(workflowProcessId);
		}
		catch (final ExecutionException e) {
			throw new WorkflowStateException("Failed to get workflow process [{}]", workflowProcessId, e);
		}
	}

	public int getTimeoutDuration() {
		return timeoutDuration;
	}

	@PostConstruct
	private void initialize() {
		cache = CacheBuilder.newBuilder().expireAfterAccess(timeoutDuration, timeoutTimeUnit)
				.build(WorkflowProcessCacheLoader);

		instance = this;
	}
}
