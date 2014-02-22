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
import org.springframework.transaction.annotation.Transactional;

import com.google.common.cache.CacheLoader;
import com.shigengyu.hyperion.core.WorkflowState;
import com.shigengyu.hyperion.dao.WorkflowStateDao;

@Service
public class WorkflowStateCacheLoader extends CacheLoader<Class<? extends WorkflowState>, WorkflowState> {

	@Resource(name = "workflowStateDao")
	private WorkflowStateDao workflowStateDao;

	@Override
	@Transactional
	public WorkflowState load(final Class<? extends WorkflowState> key) throws Exception {
		WorkflowState workflowState = key.getConstructor().newInstance();
		workflowStateDao.saveOrUpdate(workflowState.toEntity());
		return workflowState;
	}
}
