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

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.cache.CacheLoader;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.dao.WorkflowDefinitionDao;
import com.shigengyu.hyperion.entities.WorkflowDefinitionEntity;

@Service
public class WorkflowDefinitionCacheLoader extends CacheLoader<Class<? extends WorkflowDefinition>, WorkflowDefinition> {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowDefinitionCacheLoader.class);

	@Resource
	private WorkflowDefinitionDao workflowDefinitionDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public WorkflowDefinition load(final Class<? extends WorkflowDefinition> key) throws Exception {

		final WorkflowDefinition workflowDefinition = key.getConstructor().newInstance();

		WorkflowDefinitionEntity entity = workflowDefinitionDao.get(workflowDefinition.getId());
		if (entity == null) {
			entity = new WorkflowDefinitionEntity();
			entity.setWorkflowDefinitionId(workflowDefinition.getId());
			entity.setName(workflowDefinition.getName());
			workflowDefinitionDao.save(entity);

			LOGGER.info("Workflow definition [{}] saved. ID = [{}]", workflowDefinition.getName(),
					workflowDefinition.getId());
		}
		else {
			entity.setName(workflowDefinition.getName());
			workflowDefinitionDao.update(entity);

			LOGGER.info("Workflow definition [{}] updated. ID = [{}]", workflowDefinition.getName(),
					workflowDefinition.getId());
		}

		return workflowDefinition;
	}
}
