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

package com.shigengyu.hyperion.environment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestEnvironment {

	public static final String APPLICATION_CONTEXT_CONFIG = "classpath:com/shigengyu/hyperion/config/application-context-test.xml";

	@Value("${hyperion.workflow.context.scan}")
	private String workflowContextScanPackage;

	@Value("${hyperion.workflow.definition.scan}")
	private String workflowDefinitionScanPackage;

	@Value("${hyperion.workflow.state.scan}")
	private String workflowStateScanPackage;

	public String getWorkflowContextScanPackage() {
		return workflowContextScanPackage;
	}

	public String getWorkflowDefinitionScanPackage() {
		return workflowDefinitionScanPackage;
	}

	public String getWorkflowStateScanPackage() {
		return workflowStateScanPackage;
	}
}
