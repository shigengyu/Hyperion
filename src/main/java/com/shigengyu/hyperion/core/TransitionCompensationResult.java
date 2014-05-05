package com.shigengyu.hyperion.core;

import net.jcip.annotations.Immutable;

@Immutable
public final class TransitionCompensationResult {

	public static enum Status {

		SUCCESS,

		NOT_COMPENSATED,

		FAILED
	}

	public static final TransitionCompensationResult failed() {
		return new TransitionCompensationResult(Status.FAILED, null);
	}

	public static final TransitionCompensationResult failed(String message) {
		return new TransitionCompensationResult(Status.FAILED, message);
	}

	public static final TransitionCompensationResult notCompensated() {
		return new TransitionCompensationResult(Status.NOT_COMPENSATED, null);
	}

	public static final TransitionCompensationResult success() {
		return new TransitionCompensationResult(Status.SUCCESS, null);
	}

	private final String message;

	private final Status status;

	private TransitionCompensationResult(Status status, String message) {
		this.status = status;
		this.message = message;
	}

	public final String getMessage() {
		return message;
	}

	public final Status getStatus() {
		return status;
	}

	public final boolean isSuccess() {
		return status == Status.SUCCESS;
	}
}
