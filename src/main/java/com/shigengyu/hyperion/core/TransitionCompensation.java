package com.shigengyu.hyperion.core;

public class TransitionCompensation {

	private final Class<? extends Exception> exception;

	private final Class<? extends TransitionCompensator> compensator;

	public TransitionCompensation(Class<? extends Exception> exception,
			Class<? extends TransitionCompensator> compensator) {

		this.exception = exception;
		this.compensator = compensator;
	}

	public final Class<? extends TransitionCompensator> getCompensator() {
		return compensator;
	}

	public final Class<? extends Exception> getException() {
		return exception;
	}
}
