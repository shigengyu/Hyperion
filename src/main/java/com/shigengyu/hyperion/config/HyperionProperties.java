package com.shigengyu.hyperion.config;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

@Component
public class HyperionProperties {

	public static final String APPLICATION_CONTEXT_CONFIG = "classpath:com/shigengyu/hyperion/config/application-context-test.xml";

	public static final String SEPARATOR_CHARACTER = ",";

	private ImmutableList<String> workflowContextScanPackages;

	@Value("${hyperion.workflow.context.scan}")
	private String workflowContextScanPackageValue;

	private ImmutableList<String> workflowDefinitionScanPackages;

	@Value("${hyperion.workflow.definition.scan}")
	private String workflowDefinitionScanPackageValue;

	private ImmutableList<String> workflowStateScanPackages;

	@Value("${hyperion.workflow.state.scan}")
	private String workflowStateScanPackageValue;

	public final ImmutableList<String> getWorkflowContextScanPackages() {
		return workflowContextScanPackages;
	}

	public final ImmutableList<String> getWorkflowDefinitionScanPackages() {
		return workflowDefinitionScanPackages;
	}

	public final ImmutableList<String> getWorkflowStateScanPackages() {
		return workflowStateScanPackages;
	}

	@PostConstruct
	private void parse() {
		workflowContextScanPackages = ImmutableList.copyOf(StringUtils.split(workflowContextScanPackageValue,
				SEPARATOR_CHARACTER));
		workflowDefinitionScanPackages = ImmutableList.copyOf(StringUtils.split(workflowDefinitionScanPackageValue,
				SEPARATOR_CHARACTER));
		workflowStateScanPackages = ImmutableList.copyOf(StringUtils.split(workflowStateScanPackageValue,
				SEPARATOR_CHARACTER));
	}
}
