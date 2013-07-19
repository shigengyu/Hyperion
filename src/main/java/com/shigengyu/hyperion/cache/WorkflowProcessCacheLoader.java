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

import org.springframework.stereotype.Service;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.google.common.cache.CacheLoader;
import com.shigengyu.hyperion.core.WorkflowProcess;
import com.shigengyu.hyperion.dao.WorkflowProcessDao;
import com.shigengyu.hyperion.entities.WorkflowProcessEntity;

@Service
public class WorkflowProcessCacheLoader extends CacheLoader<Integer, WorkflowProcess> {

	@Resource
	private WorkflowProcessDao workflowProcessDao;

	@Override
	public WorkflowProcess load(final Integer key) throws Exception {
		final WorkflowProcessEntity entity = workflowProcessDao.get(key);
		throw new NotImplementedException();
	}
}
