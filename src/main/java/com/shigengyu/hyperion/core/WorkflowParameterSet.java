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

package com.shigengyu.hyperion.core;

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

import com.google.common.collect.Maps;

public class WorkflowParameterSet {

	public static final WorkflowParameterSet EMPTY = new WorkflowParameterSet();

	public static final WorkflowParameterSet create() {
		return new WorkflowParameterSet();
	}

	private final Map<String, WorkflowParameter> parameters = Maps.newHashMap();

	private WorkflowParameterSet() {
	}

	public WorkflowParameterSet clear() {
		parameters.clear();
		return this;
	}

	public <T> T get(String name) {
		if (parameters.containsKey(name)) {
			return (T) parameters.get(name).getValue();
		}
		else {
			return null;
		}
	}

	public <T> WorkflowParameterSet set(String name, T value) {
		if (!parameters.containsKey(name) || !ObjectUtils.equals(parameters.get(name).getValue(), value)) {
			parameters.put(name, new WorkflowParameter(name, value));
		}
		return this;
	}
}
