package com.shigengyu.hyperion.core;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class WorkflowContextSerializers {

	@Resource
	private WorkflowContextSerializer workflowContextSerializer;

	private static WorkflowContextSerializer workflowContextSerializerPrototype;

	private static ThreadLocal<WorkflowContextSerializer> workflowContextSerializers = new ThreadLocal<WorkflowContextSerializer>();

	public static WorkflowContextSerializer get() {
		WorkflowContextSerializer serializer = workflowContextSerializers.get();
		if (serializer == null) {
			serializer = workflowContextSerializerPrototype.getCloned();
			workflowContextSerializers.set(serializer);
		}
		return serializer;
	}

	@PostConstruct
	private void initialize() {
		workflowContextSerializerPrototype = workflowContextSerializer;
	}
}
