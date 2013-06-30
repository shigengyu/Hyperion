package com.shigengyu.hyperion;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shigengyu.hyperion.cache.WorkflowStateCache;
import com.shigengyu.hyperion.cache.WorkflowStateCacheLoader;
import com.shigengyu.hyperion.entities.WorkflowProcessEntity;

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

		final WorkflowProcessEntity workflowProcessEntity = new WorkflowProcessEntity();
		workflowProcessEntity.setName("Test");

		session.save(workflowProcessEntity);
		session.flush();

		final WorkflowStateCacheLoader stateCacheLoader = applicationContext
				.getBean(WorkflowStateCacheLoader.class);
		System.out.println(stateCacheLoader);

		final WorkflowStateCache workflowStateCache = applicationContext
				.getBean(WorkflowStateCache.class);
		System.out.println(workflowStateCache.getTimeoutDuration());
	}
}
