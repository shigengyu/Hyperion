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
package com.shigengyu.hyperion;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shigengyu.hyperion.cache.WorkflowDefinitionCache;
import com.shigengyu.hyperion.cache.WorkflowInstanceCacheProvider;
import com.shigengyu.hyperion.cache.WorkflowStateCache;
import com.shigengyu.hyperion.config.HyperionProperties;
import com.shigengyu.hyperion.core.TransitionExecutionResult;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.services.WorkflowExecutionService;
import com.shigengyu.hyperion.services.WorkflowPersistenceService;

@Service
public class HyperionRuntime {

	public static class WorkflowInstanceHolder implements AutoCloseable {

		private boolean isReleased = false;

		private final WorkflowInstance workflowInstance;

		private final WorkflowInstanceCacheProvider workflowInstanceCacheProvider;

		private WorkflowInstanceHolder(final WorkflowInstance workflowInstance,
				final WorkflowInstanceCacheProvider workflowInstanceCacheProvider) {

			this.workflowInstance = workflowInstance;
			this.workflowInstanceCacheProvider = workflowInstanceCacheProvider;
		}

		@Override
		public void close() throws Exception {
			if (isReleased) {
				return;
			}

			workflowInstanceCacheProvider.release(workflowInstance.getWorkflowInstanceId());
			isReleased = true;
		}

		public final WorkflowInstance getWorkflowInstance() {
			return workflowInstance;
		}

		public void release() {
			try {
				if (!isReleased) {
					close();
				}
			}
			catch (Exception e) {
				throw new HyperionException(e);
			}
		}
	}

	@Resource
	private WorkflowDefinitionCache workflowDefinitionCache;

	@Resource
	private WorkflowExecutionService workflowExecutionService;

	@Resource
	private WorkflowInstanceCacheProvider workflowInstanceCacheProvider;

	@Resource
	private WorkflowPersistenceService workflowPersistenceService;

	@Resource
	private WorkflowStateCache workflowStateCache;

	@Resource
	private HyperionProperties hyperionProperties;

	/**
	 * Acquire the workflow instance and lock it. The workflow instance holder must be close after use in order to
	 * release the workflow instance.
	 * 
	 * @param workflowInstanceId
	 * @return the {@link WorkflowInstanceHolder} containing the retrieved workflow instance
	 */
	public WorkflowInstanceHolder acquireWorkflowInstance(int workflowInstanceId) {
		WorkflowInstance workflowInstance = workflowInstanceCacheProvider.acquire(workflowInstanceId);
		return new WorkflowInstanceHolder(workflowInstance, workflowInstanceCacheProvider);
	}

	public TransitionExecutionResult executeTransition(WorkflowInstance workflowInstance, String transitionName) {
		return workflowExecutionService.execute(workflowInstance, transitionName);
	}

	public final WorkflowDefinitionCache getWorkflowDefinitionCache() {
		return workflowDefinitionCache;
	}

	public final WorkflowInstanceCacheProvider getWorkflowInstanceCache() {
		return workflowInstanceCacheProvider;
	}

	public final WorkflowStateCache getWorkflowStateCache() {
		return workflowStateCache;
	}

	public WorkflowInstanceHolder newWorkflowInstance(Class<? extends WorkflowDefinition> workflowDefinitionClass) {
		WorkflowDefinition workflowDefinition = workflowDefinitionCache.get(workflowDefinitionClass);

		// Create the workflow instance in database
		WorkflowInstance workflowInstance = workflowPersistenceService.createWorkflowInstance(workflowDefinition);

		// Load the workflow instance into cache and lock it
		WorkflowInstance cached = workflowInstanceCacheProvider.acquire(workflowInstance.getWorkflowInstanceId());

		return new WorkflowInstanceHolder(cached, workflowInstanceCacheProvider);
	}

	/**
	 * Start the runtime
	 */
	public void start() {
		workflowDefinitionCache.scanPackages(hyperionProperties.getWorkflowDefinitionScanPackages());
		workflowStateCache.scanPackages(hyperionProperties.getWorkflowStateScanPackages());
	}
}
