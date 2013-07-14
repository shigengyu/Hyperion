package com.shigengyu.hyperion.server;

import javax.annotation.PostConstruct;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shigengyu.hyperion.server.controllers.HyperionRuntimeEnvironmentController;

public class RestServer {

	private static ClassPathXmlApplicationContext applicationContext;

	private static final Logger LOGGER = LoggerFactory.getLogger(RestServer.class);

	public static void main(final String args[]) throws Exception {

		applicationContext = new ClassPathXmlApplicationContext(
				"classpath:com/shigengyu/hyperion/config/application-context.xml");
		applicationContext.getBean(RestServer.class);
	}

	@Value("${hyperion.rest.server.host}")
	private String restServerHost;

	@Value("${hyperion.rest.server.port}")
	private int restServerPort;

	@PostConstruct
	private void startServer() {
		final JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(HyperionRuntimeEnvironmentController.class);
		sf.setResourceProvider(HyperionRuntimeEnvironmentController.class, new SingletonResourceProvider(
				new HyperionRuntimeEnvironmentController()));
		final String address = "http://" + restServerHost + ":" + restServerPort + "/";
		sf.setAddress(address);
		sf.create();

		LOGGER.info("REST server started on " + address);
	}
}
