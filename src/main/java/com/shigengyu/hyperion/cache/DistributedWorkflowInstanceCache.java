package com.shigengyu.hyperion.cache;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowInstanceSerializer;

@Service
public class DistributedWorkflowInstanceCache implements WorkflowInstanceCacheProvider {

	private final HazelcastInstance hazelcastInstance;

	private static final String WORKFLOW_INSTANCE_MAP_NAME = "HyperionWorkflowInstanceMap";

	@Resource
	private WorkflowInstanceCacheLoader workflowInstanceCacheLoader;

	public DistributedWorkflowInstanceCache() {
		Config config = new Config();
		hazelcastInstance = Hazelcast.newHazelcastInstance(config);
		SerializerConfig sc = new SerializerConfig();
		sc.setImplementation(new WorkflowInstanceSerializer()).setTypeClass(WorkflowInstance.class);
		config.getSerializationConfig().addSerializerConfig(sc);
	}

	@Override
	public <T extends WorkflowInstance> WorkflowInstance get(Integer workflowInstanceId) {
		IMap<Integer, WorkflowInstance> map = hazelcastInstance
				.<Integer, WorkflowInstance> getMap(WORKFLOW_INSTANCE_MAP_NAME);

		if (map.containsKey(workflowInstanceId)) {
			try {
				map.tryLock(workflowInstanceId, 30, TimeUnit.SECONDS);
				return map.get(workflowInstanceId);
			}
			catch (InterruptedException e) {
				return null;
			}
		}
		else {
			try {
				WorkflowInstance workflowInstance = workflowInstanceCacheLoader.load(workflowInstanceId);
				map.tryPut(workflowInstanceId, workflowInstance, 30, TimeUnit.SECONDS);
				return workflowInstance;
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
