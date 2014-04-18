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
import com.shigengyu.hyperion.core.TransitionParameterSet;
import com.shigengyu.hyperion.environment.TestEnvironment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(TestEnvironment.APPLICATION_CONTEXT_CONFIG)
public class TransitionParameterSetTest {

	private static class SetAndGetThread extends Thread {

		private final TransitionParameterSet parameters;

		private SetAndGetThread(TransitionParameterSet parameters) {
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
					Assert.assertEquals(number, parameters.get(Integer.class, parameterKey).intValue());
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
		TransitionParameterSet parameters = TransitionParameterSet.create();

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
