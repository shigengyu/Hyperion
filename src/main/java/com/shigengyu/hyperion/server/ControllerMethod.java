package com.shigengyu.hyperion.server;

import org.apache.commons.lang.StringUtils;

public class ControllerMethod {

	private final String controller;

	private final String httpMethod;

	private final String method;

	public ControllerMethod(String httpMethod, String controller, String method) {
		this.httpMethod = httpMethod;

		controller = "/" + StringUtils.strip(controller, "/");
		this.controller = controller;

		method = "/" + StringUtils.strip(method, "/");
		this.method = method;
	}

	public final String getController() {
		return controller;
	}

	public final String getHttpMethod() {
		return httpMethod;
	}

	public final String getMethod() {
		return method;
	}

	public String getUrl() {

		String url = StringUtils.EMPTY;
		if (!StringUtils.equals(controller, "/")) {
			url += controller;
		}
		if (!StringUtils.equals(method, "/")) {
			url += method;
		}
		return url + "/";
	}
}
