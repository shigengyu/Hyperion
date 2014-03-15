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
import com.shigengyu.hyperion.cache.WorkflowInstanceCache;
import com.shigengyu.hyperion.cache.WorkflowStateCache;
import com.shigengyu.hyperion.config.HyperionProperties;
import com.shigengyu.hyperion.core.TransitionExecutionResult;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.services.WorkflowExecutionService;
import com.shigengyu.hyperion.services.WorkflowPersistenceService;

@Service
public class HyperionRuntime {

	@Resource
	private WorkflowDefinitionCache workflowDefinitionCache;

	@Resource
	private WorkflowExecutionService workflowExecutionService;

	@Resource
	private WorkflowInstanceCache workflowInstanceCache;

	@Resource
	private WorkflowPersistenceService workflowPersistenceService;

	@Resource
	private WorkflowStateCache workflowStateCache;

	@Resource
	private HyperionProperties hyperionProperties;

	public TransitionExecutionResult executeTransition(WorkflowInstance workflowInstance, String transitionName) {
		return workflowExecutionService.execute(workflowInstance, transitionName);
	}

	public WorkflowInstance getExistingWorkflowInstance(int workflowInstanceId) {
		return workflowInstanceCache.get(workflowInstanceId);
	}

	public final WorkflowDefinitionCache getWorkflowDefinitionCache() {
		return workflowDefinitionCache;
	}

	public final WorkflowInstanceCache getWorkflowInstanceCache() {
		return workflowInstanceCache;
	}

	public final WorkflowStateCache getWorkflowStateCache() {
		return workflowStateCache;
	}

	public WorkflowInstance newWorkflowInstance(Class<? extends WorkflowDefinition> workflowDefinitionClass) {
		WorkflowDefinition workflowDefinition = workflowDefinitionCache.get(workflowDefinitionClass);
		WorkflowInstance workflowInstance = workflowPersistenceService.createWorkflowInstance(workflowDefinition);
		workflowExecutionService.stabilize(workflowInstance);
		return workflowInstance;
	}

	/**
	 * Start the runtime
	 */
	public void start() {
		workflowDefinitionCache.scanPackages(hyperionProperties.getWorkflowDefinitionScanPackages());
		workflowStateCache.scanPackages(hyperionProperties.getWorkflowStateScanPackages());
	}
}
