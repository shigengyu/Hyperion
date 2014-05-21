/*******************************************************************************
 * Copyright 2013-2014 Gengyu (Univer) Shi
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

import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.shigengyu.common.StringMessage;
import com.shigengyu.hyperion.HyperionException;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowInstanceSerializer;

@Service
public class DistributedWorkflowInstanceCache implements WorkflowInstanceCacheProvider {

	private final HazelcastInstance hazelcastInstance;

	private static final String WORKFLOW_INSTANCE_MAP_NAME = "HyperionWorkflowInstanceMap";

	private final IMap<Integer, WorkflowInstance> map;

	@Resource
	private WorkflowInstanceCacheLoader workflowInstanceCacheLoader;

	public DistributedWorkflowInstanceCache() {
		Config config = new Config();
		hazelcastInstance = Hazelcast.newHazelcastInstance(config);
		SerializerConfig sc = new SerializerConfig();
		sc.setImplementation(new WorkflowInstanceSerializer()).setTypeClass(WorkflowInstance.class);
		config.getSerializationConfig().addSerializerConfig(sc);

		map = hazelcastInstance.<Integer, WorkflowInstance> getMap(WORKFLOW_INSTANCE_MAP_NAME);
	}

	@Override
	public <T extends WorkflowInstance> WorkflowInstance acquire(final Integer workflowInstanceId) {

		// Lock the workflow instance ID
		map.lock(workflowInstanceId);

		WorkflowInstance workflowInstance = map.get(workflowInstanceId);
		if (workflowInstance != null) {
			return workflowInstance;
		}

		try {
			workflowInstance = workflowInstanceCacheLoader.load(workflowInstanceId);
			map.replace(workflowInstanceId, workflowInstance);
			return workflowInstance;
		}
		catch (Exception e) {
			map.unlock(workflowInstanceId);
			throw new HyperionException(StringMessage.with("Failed to load workflow instance [{}] from database",
					workflowInstance), e);
		}
	}

	@Override
	public <T extends WorkflowInstance> void release(Integer workflowInstanceId) {
		if (!map.isLocked(workflowInstanceId)) {
			throw new HyperionException(
					"Cannot release workflow instance [{}] as current thread does not own the lock", workflowInstanceId);
		}

		map.unlock(workflowInstanceId);
	}
}
