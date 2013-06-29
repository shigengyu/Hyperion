package com.shigengyu.hyperion;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SimpleTest {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"com/shigengyu/hyperion/config/application-context.xml");

		final SessionFactory sessionFactory = (SessionFactory) applicationContext
				.getBean("sessionFactory");
		final Session session = sessionFactory.openSession();
	}
}
