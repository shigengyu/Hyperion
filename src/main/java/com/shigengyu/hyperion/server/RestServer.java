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
package com.shigengyu.hyperion.server;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.eclipse.jetty.http.HttpMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class RestServer implements BeanPostProcessor {

	private static ClassPathXmlApplicationContext applicationContext;

	private static final Logger LOGGER = LoggerFactory.getLogger(RestServer.class);

	private static Collection<ControllerMethod> extractControllerMethods(Iterable<ResourceProvider> resourceProviders) {

		TreeMap<String, ControllerMethod> controllerMethods = Maps.newTreeMap(new Comparator<String>() {

			@Override
			public int compare(String first, String second) {
				return first.compareTo(second);
			}
		});

		for (ResourceProvider resourceProvider : resourceProviders) {
			String controllerPath = resourceProvider.getResourceClass().getAnnotation(Path.class).value();
			for (Method method : resourceProvider.getResourceClass().getMethods()) {
				if (!method.isAnnotationPresent(Path.class)) {
					continue;
				}

				String methodPath = method.getAnnotation(Path.class).value();

				String httpMethod = null;
				if (method.isAnnotationPresent(GET.class)) {
					httpMethod = HttpMethods.GET;
				}
				else if (method.isAnnotationPresent(POST.class)) {
					httpMethod = HttpMethods.POST;
				}

				ControllerMethod controllerMethod = new ControllerMethod(httpMethod, controllerPath, methodPath);
				controllerMethods.put(controllerMethod.getUrl(), controllerMethod);
			}
		}

		return controllerMethods.values();
	}

	public static void main(final String args[]) throws Exception {

		applicationContext = new ClassPathXmlApplicationContext(
				"classpath:com/shigengyu/hyperion/config/application-context.xml");

		if (applicationContext.isActive()) {
			LOGGER.info("application-context.xml loaded");
		}

		applicationContext.getBean(RestServer.class).startServer();
	}

	private Collection<ControllerMethod> controllerMethods;

	private final List<ResourceProvider> resourceProviders = Lists.newArrayList();

	@Value("${hyperion.rest.server.host}")
	private String restServerHost;

	@Value("${hyperion.rest.server.port}")
	private int restServerPort;

	public final Collection<ControllerMethod> getControllerMethods() {
		return controllerMethods;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean.getClass().isAnnotationPresent(HyperionController.class)) {
			resourceProviders.add(new SingletonResourceProvider(bean));
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	private void startServer() {
		controllerMethods = extractControllerMethods(resourceProviders);
		final JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceProviders(resourceProviders);
		final String address = "http://" + restServerHost + ":" + restServerPort + "/";
		sf.setAddress(address);
		sf.create();

		LOGGER.info("REST server started on " + address);
	}
}
