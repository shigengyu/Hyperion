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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

@Component
public class HyperionProperties {

	private static final Logger LOGGER = LoggerFactory.getLogger(HyperionProperties.class);

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

	public final String[] getWorkflowContextScanPackages() {
		return workflowContextScanPackages.toArray(new String[0]);
	}

	public final String[] getWorkflowDefinitionScanPackages() {
		return workflowDefinitionScanPackages.toArray(new String[0]);
	}

	public final String[] getWorkflowStateScanPackages() {
		return workflowStateScanPackages.toArray(new String[0]);
	}

	@PostConstruct
	private void parse() {
		workflowContextScanPackages = ImmutableList.copyOf(StringUtils.split(workflowContextScanPackageValue,
				SEPARATOR_CHARACTER));
		workflowDefinitionScanPackages = ImmutableList.copyOf(StringUtils.split(workflowDefinitionScanPackageValue,
				SEPARATOR_CHARACTER));
		workflowStateScanPackages = ImmutableList.copyOf(StringUtils.split(workflowStateScanPackageValue,
				SEPARATOR_CHARACTER));

		printProperties();
	}

	private void printProperties() {
		LOGGER.info("Workflow context scan packages = [{}]", workflowContextScanPackageValue);
		LOGGER.info("Workflow definition scan packages = [{}]", workflowDefinitionScanPackageValue);
		LOGGER.info("Workflow state scan packages = [{}]", workflowStateScanPackageValue);
	}
}
