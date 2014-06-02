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

import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.shigengyu.hyperion.utils.ReflectionsHelper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class WorkflowContextXmlSerializer implements WorkflowContextSerializer, BeanPostProcessor {

	protected XStream xStream = new XStream(new DomDriver());

	private final String dateTimeFormat;

	public WorkflowContextXmlSerializer(final String dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
		xStream.registerConverter(new DateConverter(dateTimeFormat, new String[0]));
		xStream.autodetectAnnotations(true);
	}

	@Override
	public <T extends WorkflowContext> T deserialize(final Class<T> clazz, final String input) {
		T workflowContext = null;
		try {
			Object deserializedObject = xStream.fromXML(input);
			if (deserializedObject == null) {
				return null;
			}
			workflowContext = clazz.cast(deserializedObject);

			// Initialize fields as default constructor will not be called when object is deserialized
			workflowContext.initializeFieldsOnDeserialization();

			return workflowContext;
		}
		catch (ClassCastException e) {
			if (workflowContext != null) {
				throw new WorkflowContextException("Unable to cast workflow context of type ["
						+ workflowContext.getClass().getName() + "] to [" + clazz.getName() + "]");
			}
			else {
				throw new WorkflowContextException(e);
			}
		}
	}

	@Override
	public WorkflowContextSerializer getCloned() {
		return new WorkflowContextXmlSerializer(dateTimeFormat);
	}

	public void initialize(final String... packageNames) {
		final Reflections reflections = ReflectionsHelper.createReflections(packageNames);
		for (final Class<?> clazz : reflections.getSubTypesOf(WorkflowContext.class)) {
			try {
				Class.forName(clazz.getName());
			}
			catch (final ClassNotFoundException e) {
			}
		}
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
		if (bean.getClass().isAnnotationPresent(XStreamAlias.class)) {
			final XStreamAlias alias = bean.getClass().getAnnotation(XStreamAlias.class);
			xStream.alias(alias.value(), bean.getClass());
		}

		return bean;
	}

	@Override
	public String serialize(final WorkflowContext workflowContext) {
		return xStream.toXML(workflowContext);
	}
}
