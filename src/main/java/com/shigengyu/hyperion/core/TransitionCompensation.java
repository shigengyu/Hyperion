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
