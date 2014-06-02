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
package com.shigengyu.hyperion.services;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowInstance.WorkflowInstanceFactory;
import com.shigengyu.hyperion.dao.WorkflowInstanceDao;
import com.shigengyu.hyperion.entities.WorkflowInstanceEntity;

/**
 * The workflow persistent service is the layer between Hyperion runtime and database. Responsible for creating new
 * instances and persisting existing instances.
 * 
 * @author Univer
 * 
 */
@Service
public class WorkflowPersistenceServiceImpl implements WorkflowPersistenceService {

	@Resource
	private WorkflowInstanceDao workflowInstanceDao;

	@Resource
	private WorkflowExecutionService workflowExecutionService;

	@Resource
	private WorkflowInstanceFactory workflowInstanceFactory;

	@Override
	@Transactional
	public WorkflowInstance createWorkflowInstance(WorkflowDefinition workflowDefinition) {
		WorkflowInstance workflowInstance = workflowInstanceFactory.create(workflowDefinition);
		WorkflowInstanceEntity entity = workflowInstanceDao.saveOrUpdate(workflowInstance.toEntity());
		workflowInstance.setWorkflowInstanceId(entity.getWorkflowInstanceId());

		// Stabilize workflow instance
		workflowExecutionService.stabilize(workflowInstance);

		// Persist workflow instance
		persistWorkflowInstance(workflowInstance);

		return workflowInstance;
	}

	@Override
	public void persistWorkflowInstance(WorkflowInstance workflowInstance) {
		workflowInstanceDao.saveOrUpdate(workflowInstance.toEntity());
	}
}
