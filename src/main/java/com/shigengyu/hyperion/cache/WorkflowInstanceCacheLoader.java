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

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheLoader;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowInstance.WorkflowInstanceFactory;
import com.shigengyu.hyperion.dao.WorkflowInstanceDao;
import com.shigengyu.hyperion.entities.WorkflowInstanceEntity;

@Service
public class WorkflowInstanceCacheLoader extends CacheLoader<Integer, WorkflowInstance> {

	@Resource
	private WorkflowInstanceDao workflowInstanceDao;

	@Resource
	private WorkflowDefinitionCache workflowDefinitionCache;

	@Resource
	private WorkflowInstanceFactory workflowInstanceFactory;

	@Override
	public WorkflowInstance load(final Integer key) throws Exception {
		final WorkflowInstanceEntity entity = workflowInstanceDao.get(key);

		WorkflowDefinition workflowDefinition = workflowDefinitionCache.get(entity.getWorkflowDefinitionEntity()
				.getWorkflowDefinitionId());

		return workflowInstanceFactory.create(workflowDefinition, entity);
	}
}
