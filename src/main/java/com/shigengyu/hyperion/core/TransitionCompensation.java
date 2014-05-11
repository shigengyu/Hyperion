/*******************************************************************************
 * Copyright 2013-2014 Gengyu (Univer) Shi
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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.shigengyu.hyperion.cache.TransitionCompensatorCache;

public class TransitionCompensation {

	private static class TransitionCompensationFactory {

		private static TransitionCompensationFactory instance;

		@Resource
		private TransitionCompensatorCache transitionCompensatorCache;

		private final TransitionCompensator getCompensator(
				final Class<? extends TransitionCompensator> transitionCompensatorClass) {
			return transitionCompensatorCache.get(transitionCompensatorClass);
		}

		@PostConstruct
		private void initialize() {
			instance = this;
		}
	}

	private final Class<? extends Exception> exception;

	private final TransitionCompensator transitionCompensator;

	public TransitionCompensation(Class<? extends Exception> exception,
			Class<? extends TransitionCompensator> compensator) {

		this.exception = exception;
		transitionCompensator = TransitionCompensationFactory.instance.getCompensator(compensator);
	}

	public final Class<? extends Exception> getException() {
		return exception;
	}

	public final TransitionCompensator getTransitionCompensator() {
		return transitionCompensator;
	}
}
