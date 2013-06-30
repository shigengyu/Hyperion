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

package com.shigengyu.hyperion.services;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shigengyu.hyperion.core.WorkflowContext;
import com.shigengyu.hyperion.utils.ReflectionsHelper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

@Service
@Lazy(false)
public class WorkflowContextSerializer {

	@Value("${hyperion.workflow.context.format.datetime}")
	private String dateTimeFormat;

	protected XStream xStream = new XStream(new DomDriver());

	private WorkflowContextSerializer() {
		xStream.registerConverter(new DateConverter(dateTimeFormat,
				new String[0]));
		xStream.autodetectAnnotations(true);
	}

	@SuppressWarnings("unchecked")
	public <T extends WorkflowContext> T deserialize(final String xml) {
		return (T) xStream.fromXML(xml);
	}

	public void initialize(final String... packageNames) {
		final Reflections reflections = ReflectionsHelper
				.createReflections(packageNames);
		for (final Class<?> clazz : reflections
				.getSubTypesOf(WorkflowContext.class)) {
			try {
				Class.forName(clazz.getName());
			} catch (final ClassNotFoundException e) {
			}
		}
	}

	public String serialize(final WorkflowContext workflowContext) {
		return xStream.toXML(workflowContext);
	}
}