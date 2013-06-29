package com.shigengyu.hyperion;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shigengyu.hyperion.services.ExecutionService;

public class SimpleTest {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"com/shigengyu/hyperion/config/application-context.xml");
		final ExecutionService executionService = applicationContext
				.getBean(ExecutionService.class);
		System.out.println(executionService.getName());
	}
}
