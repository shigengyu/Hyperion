package com.shigengyu.hyperion.server;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

import com.shigengyu.hyperion.server.controllers.HyperionRuntimeEnvironmentController;

public class RestServer {

	public static void main(final String args[]) throws Exception {
		new RestServer();
		System.out.println("Server ready...");

		Thread.sleep(5 * 6000 * 1000);
		System.out.println("Server exiting");
		System.exit(0);
	}

	protected RestServer() throws Exception {
		final JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(HyperionRuntimeEnvironmentController.class);
		sf.setResourceProvider(HyperionRuntimeEnvironmentController.class, new SingletonResourceProvider(
				new HyperionRuntimeEnvironmentController()));
		sf.setAddress("http://localhost:9999/");

		sf.create();
	}
}
