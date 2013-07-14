package com.shigengyu.hyperion.server.controllers;

import javax.annotation.Nullable;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.shigengyu.hyperion.cache.WorkflowDefinitionCache;
import com.shigengyu.hyperion.core.WorkflowDefinition;

@Path("/runtime/")
@Produces(MediaType.TEXT_PLAIN)
public class HyperionRuntimeEnvironmentController {

	@GET
	@Path("/workflow/list/")
	public String getWorkflowDefinitions() {
		return StringUtils.join(
				Lists.transform(
						WorkflowDefinitionCache.getInstance().loadPackages("com.shigengyu.hyperion.scenarios.simple")
								.getAll(), new Function<WorkflowDefinition, String>() {

							@Override
							public String apply(@Nullable final WorkflowDefinition input) {
								return input.getName();
							}
						}), "<br />");
	}
}
