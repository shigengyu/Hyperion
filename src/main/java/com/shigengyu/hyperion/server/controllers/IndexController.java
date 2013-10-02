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
