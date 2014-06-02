package com.shigengyu.hyperion.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shigengyu.hyperion.HyperionException;

@Configuration
public class WorkflowContextSerializerConfiguration {

	@Value("${hyperion.workflow.context.serializer.type}")
	private String workflowContextSerializerType;

	@Value("${hyperion.workflow.context.format.datetime}")
	private String dateTimeFormat;

	@Bean(name = "workflowContextSerializer")
	public WorkflowContextSerializer workflowContextSerializer() {

		WorkflowContextSerializer workflowContextSerializer = null;

		switch (workflowContextSerializerType) {
		case "binary":
			workflowContextSerializer = new WorkflowContextBinarySerializer();
			break;
		case "xml":
			workflowContextSerializer = new WorkflowContextXmlSerializer(dateTimeFormat);
			break;
		default:
			throw new HyperionException("Unknown workflow context serializer type [{}]", workflowContextSerializerType);
		}

		return workflowContextSerializer;
	}
}
