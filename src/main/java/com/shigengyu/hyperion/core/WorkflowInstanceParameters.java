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
import java.util.concurrent.ConcurrentHashMap;

public class WorkflowInstanceParameters {

	private final Map<String, Object> map;

	public WorkflowInstanceParameters() {
		map = new ConcurrentHashMap<String, Object>();
	}

	public WorkflowInstanceParameters clear() {
		map.clear();
		return this;
	}

	public <T> T get(Class<T> key) {
		if (key == null) {
			return null;
		}

		return (T) map.get(key.getName());
	}

	public <T> T get(String key) {
		if (key == null) {
			return null;
		}

		return (T) map.get(key);
	}

	public <T> WorkflowInstanceParameters set(String key, T value) {
		map.put(key, value);
		return this;
	}

	public <T> WorkflowInstanceParameters set(T value) {
		if (value != null) {
			map.put(value.getClass().getName(), value);
		}
		return this;
	}
}
