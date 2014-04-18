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

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

import com.google.common.collect.Maps;

public class TransitionParameterSet {

	public static final TransitionParameterSet EMPTY = new TransitionParameterSet();

	public static final TransitionParameterSet create() {
		return new TransitionParameterSet();
	}

	private final Map<String, TransitionParameter> parameters = Maps.newHashMap();

	private TransitionParameterSet() {
	}

	public TransitionParameterSet clear() {
		parameters.clear();
		return this;
	}

	public <T> T get(final Class<T> clazz, String name) {
		if (parameters.containsKey(name)) {
			Object value = parameters.get(name).getValue();
			if (value == null) {
				return null;
			}
			try {
				return clazz.cast(value);
			}
			catch (ClassCastException e) {
				throw new WorkflowExecutionException("Cannot cast value of transition parameter [" + name
						+ "] from type [" + value.getClass().getName() + "] to type [" + clazz.getName() + "]");
			}
		}
		else {
			return null;
		}
	}

	public <T> TransitionParameterSet set(String name, T value) {
		if (!parameters.containsKey(name) || !ObjectUtils.equals(parameters.get(name).getValue(), value)) {
			parameters.put(name, new TransitionParameter(name, value));
		}
		return this;
	}
}
