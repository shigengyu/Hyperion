package com.shigengyu.hyperion;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shigengyu.hyperion.models.Course;
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

		final SessionFactory sessionFactory = (SessionFactory) applicationContext
				.getBean("sessionFactory");
		final Session session = sessionFactory.openSession();
		final Course course = new Course();
		course.setCourseName("Test");
		session.save(course);
		session.flush();

		final Course reloadedCourse = (Course) session
				.createSQLQuery("select {c.*} from COURSE c")
				.addEntity("c", Course.class).uniqueResult();

		System.out.println(reloadedCourse.getCourseName());
	}
}
