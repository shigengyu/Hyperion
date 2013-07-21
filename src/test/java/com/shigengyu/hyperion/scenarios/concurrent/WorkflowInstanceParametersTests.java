package com.shigengyu.hyperion.scenarios.concurrent;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.shigengyu.hyperion.core.WorkflowInstanceParameters;
import com.shigengyu.hyperion.environment.TestEnvironment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(TestEnvironment.APPLICATION_CONTEXT_CONFIG)
public class WorkflowInstanceParametersTests {

	private static class SetAndGetThread extends Thread {

		private final WorkflowInstanceParameters parameters;

		private SetAndGetThread(WorkflowInstanceParameters parameters) {
			this.parameters = parameters;
		}

		@Override
		public void run() {
			for (int i = 0; i <= 30; i++) {
				try {
					String parameterKey = Thread.currentThread().getName() + "_number";
					int number = RandomUtils.nextInt();
					parameters.set(parameterKey, number);
					Thread.sleep(RandomUtils.nextInt(5));
					Assert.assertEquals(number, parameters.get(parameterKey));
				}
				catch (InterruptedException e) {
				}
			}
		}
	}

	private static class ThreadUncaughtExceptionHandler implements UncaughtExceptionHandler {

		private final List<Throwable> exceptions = Lists.newArrayList();

		public List<Throwable> getExceptions() {
			return exceptions;
		}

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			exceptions.add(e);
		}
	}

	@Test
	public void setAndGetParameterValues() {
		WorkflowInstanceParameters parameters = new WorkflowInstanceParameters();

		ThreadUncaughtExceptionHandler exceptionHandler = new ThreadUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);

		Set<Thread> threads = new HashSet<Thread>();

		for (int i = 0; i < 30; i++) {
			SetAndGetThread thread = new SetAndGetThread(parameters);
			thread.start();
			threads.add(thread);
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (exceptionHandler.getExceptions().size() > 0) {
			Assert.fail(exceptionHandler.getExceptions().get(0).getMessage());
		}
	}
}
