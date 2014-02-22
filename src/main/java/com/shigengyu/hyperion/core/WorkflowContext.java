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
import com.shigengyu.hyperion.services.WorkflowContextXmlSerializer;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("WorkflowContext")
public class WorkflowContext implements Serializable {

	public static final String MAP_ENTRIES_ELEMENT_NAME = "MapEntries";

	private static final long serialVersionUID = -7410578804012994691L;

	private final Map<String, Object> map = Maps.newHashMap();

	public WorkflowContext() {
	}

	public <T> T get(final String key) {
		return (T) map.get(key);
	}

	public WorkflowContext loadXml(final String xml) {
		return WorkflowContextXmlSerializer.getInstance().deserialize(xml);
	}

	public <T> void put(final String key, final T value) {
		map.put(key, value);
	}

	public String toXml() {
		return WorkflowContextXmlSerializer.getInstance().serialize(this);
	}
}
