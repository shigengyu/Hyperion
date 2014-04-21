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
package com.shigengyu.hyperion.server.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.shigengyu.hyperion.HyperionRuntime;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.server.HyperionController;

@Component
@HyperionController
@Path("/runtime/")
@Produces(MediaType.TEXT_HTML)
public class HyperionRuntimeEnvironmentController {

	@Resource
	private HyperionRuntime hyperionRuntime;

	@GET
	@Path("/workflow/list/")
	public String getWorkflowDefinitions() {
		List<String> workflowDefinitionNames = Lists.transform(hyperionRuntime.getWorkflowDefinitionCache().getAll(),
				new Function<WorkflowDefinition, String>() {

					@Override
					public String apply(final WorkflowDefinition input) {
						return input.getName();
					}
				});

		if (workflowDefinitionNames.size() == 0) {
			return "No workflow definition found";
		}

		return StringUtils.join(workflowDefinitionNames, "<br />");
	}

	@PostConstruct
	private void initialize() {
		hyperionRuntime.start();
	}
}
