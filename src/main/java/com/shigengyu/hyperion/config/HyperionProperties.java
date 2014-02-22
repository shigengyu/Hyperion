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
