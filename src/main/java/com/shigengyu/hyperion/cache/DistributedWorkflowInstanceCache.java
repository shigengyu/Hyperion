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

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import com.shigengyu.common.StringMessage;
import com.shigengyu.hyperion.HyperionException;
import com.shigengyu.hyperion.core.WorkflowInstance;

@Service
public class DistributedWorkflowInstanceCache implements WorkflowInstanceCacheProvider {

	public static class WorkflowInstanceHolder implements DataSerializable {

		private WorkflowInstance workflowInstance;

		private Date lastAccessTime;

		private Date expireTime;

		private long timeoutDuration;

		/**
		 * Used by Hazelcast serializer only
		 */
		@SuppressWarnings("unused")
		private WorkflowInstanceHolder() {
		}

		public WorkflowInstanceHolder(final WorkflowInstance workflowInstance, long timeoutDuration) {
			this.workflowInstance = workflowInstance;
			this.timeoutDuration = timeoutDuration;
			lastAccessTime = new Date();
			expireTime = new Date(lastAccessTime.getTime() + timeoutDuration);
		}

		public final Date getCacheTimestamp() {
			return lastAccessTime;
		}

		public final Date getExpireTimestamp() {
			return expireTime;
		}

		public final WorkflowInstance getWorkflowInstance() {
			return workflowInstance;
		}

		@Override
		public void readData(ObjectDataInput in) throws IOException {
			workflowInstance = in.readObject();
			timeoutDuration = in.readLong();
			lastAccessTime = in.readObject();
			expireTime = in.readObject();
		}

		public void touch() {
			lastAccessTime = new Date();
			expireTime = new Date(lastAccessTime.getTime() + timeoutDuration);
		}

		@Override
		public void writeData(ObjectDataOutput out) throws IOException {
			out.writeObject(workflowInstance);
			out.writeLong(timeoutDuration);
			out.writeObject(lastAccessTime);
			out.writeObject(expireTime);
		}
	}

	@Value("${hyperion.workflow.cache.instance.timeout.duration}")
	private int timeoutDuration;

	@Value("${hyperion.workflow.cache.instance.timeout.timeunit}")
	private TimeUnit timeoutTimeUnit;

	private final HazelcastInstance hazelcastInstance;

	private static final String WORKFLOW_INSTANCE_MAP_NAME = "HyperionWorkflowInstanceMap";

	private final IMap<Integer, WorkflowInstanceHolder> map;

	@Resource
	private WorkflowInstanceCacheLoader workflowInstanceCacheLoader;

	public DistributedWorkflowInstanceCache() {
		Config config = new Config();

		// SerializerConfig sc = new SerializerConfig();
		// sc.setImplementation(new WorkflowInstanceSerializer()).setTypeClass(WorkflowInstanceHolder.class);
		// config.getSerializationConfig().addSerializerConfig(sc);
		hazelcastInstance = Hazelcast.newHazelcastInstance(config);

		map = hazelcastInstance.<Integer, WorkflowInstanceHolder> getMap(WORKFLOW_INSTANCE_MAP_NAME);
	}

	@Override
	public <T extends WorkflowInstance> WorkflowInstance acquire(final Integer workflowInstanceId) {
		// Lock the workflow instance ID
		map.lock(workflowInstanceId);

		WorkflowInstanceHolder workflowInstanceHolder = map.get(workflowInstanceId);
		if (workflowInstanceHolder != null) {
			return workflowInstanceHolder.getWorkflowInstance();
		}

		try {
			WorkflowInstance workflowInstance = workflowInstanceCacheLoader.load(workflowInstanceId);
			map.put(workflowInstanceId,
					new WorkflowInstanceHolder(workflowInstance, timeoutTimeUnit.toMillis(timeoutDuration)));
			return workflowInstance;
		}
		catch (Exception e) {
			map.unlock(workflowInstanceId);
			throw new HyperionException(StringMessage.with("Failed to load workflow instance [{}] from database",
					workflowInstanceId), e);
		}
	}

	public void cleanup() {

		Date now = new Date();

		for (Integer key : map.keySet()) {
			try {
				map.lock(key);

				WorkflowInstanceHolder workflowInstanceHolder = map.get(key);
				if (workflowInstanceHolder != null && workflowInstanceHolder.expireTime.before(now)) {
					map.remove(key);
				}
			}
			finally {
				map.unlock(key);
			}
		}
	}

	@Override
	public <T extends WorkflowInstance> void release(final T workflowInstance) {

		int workflowInstanceId = workflowInstance.getWorkflowInstanceId();

		if (!map.isLocked(workflowInstance.getWorkflowInstanceId())) {
			throw new HyperionException(
					"Cannot release workflow instance [{}] as current thread does not own the lock", workflowInstanceId);
		}

		WorkflowInstanceHolder workflowInstanceHolder = map.get(workflowInstanceId);
		// Update the expiry time
		workflowInstanceHolder.touch();
		map.put(workflowInstanceId, workflowInstanceHolder);

		map.unlock(workflowInstanceId);
	}
}
