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

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.shigengyu.hyperion.server.ControllerMethod;
import com.shigengyu.hyperion.server.HyperionController;
import com.shigengyu.hyperion.server.RestServer;

@Component
@HyperionController
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class IndexController {

	@Resource
	private RestServer restServer;

	@GET
	@Path("/favicon.ico/")
	public String getWorkflowDefinitions() {
		return null;
	}

	@GET
	@Path("/")
	public String index() {

		StringBuilder sb = new StringBuilder();

		for (ControllerMethod controllerMethod : restServer.getControllerMethods()) {
			sb.append("<a href=\"" + controllerMethod.getUrl() + "\">" + controllerMethod.getUrl() + "</a>" + "<br />");
		}

		return sb.toString();
	}
}
