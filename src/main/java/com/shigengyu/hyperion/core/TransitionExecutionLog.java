package com.shigengyu.hyperion.core;

public class TransitionExecutionLog {

	public static enum Type {

		INFO,

		WARNING,

		ERROR
	}

	public static final TransitionExecutionLog error() {
		return new TransitionExecutionLog(Type.ERROR);
	}

	public static final TransitionExecutionLog info() {
		return new TransitionExecutionLog(Type.INFO);
	}

	public static final TransitionExecutionLog wanring() {
		return new TransitionExecutionLog(Type.WARNING);
	}

	private final Type type;

	private String message;

	private Exception exception;

	private TransitionExecutionLog(Type type) {
		this.type = type;
	}

	public final Exception getException() {
		return exception;
	}

	public final String getMessage() {
		return message;
	}

	public final Type getType() {
		return type;
	}

	public TransitionExecutionLog setException(Exception e) {
		exception = e;
		return this;
	}

	public TransitionExecutionLog setMessage(String message) {
		this.message = message;
		return this;
	}
}
