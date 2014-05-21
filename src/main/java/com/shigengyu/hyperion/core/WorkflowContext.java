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
package com.shigengyu.hyperion.core;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("WorkflowContext")
public class WorkflowContext implements Serializable {

	private static final long serialVersionUID = 53228700616826789L;

	private final Map<String, Object> map = Maps.newHashMap();

	public WorkflowContext() {
	}

	public <T> T get(final Class<T> clazz, final String key) {
		Object value = map.get(key);
		try {
			return clazz.cast(value);
		}
		catch (ClassCastException e) {
			throw new WorkflowContextException("Unable to cast workflow context value of key [" + key
					+ "] to the specified type", e);
		}
	}

	/**
	 * Override to specify custom logic to initialize the workflow context when it is deserialized, as the default
	 * constructor will not be called.
	 */
	void initializeFieldsOnDeserialization() {
	}

	public <T> void put(final String key, final T value) {
		map.put(key, value);
	}

	public <T> T unsafeGet(final String key) {
		return (T) map.get(key);
	}
}
