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
