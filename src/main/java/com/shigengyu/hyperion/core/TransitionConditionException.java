package com.shigengyu.hyperion.core;

import com.shigengyu.hyperion.HyperionException;

public class TransitionConditionException extends HyperionException {

	private static final long serialVersionUID = 1L;

	public TransitionConditionException(String message) {
		super(message);
	}

	public TransitionConditionException(String message, Object... args) {
		super(message, args);
	}

	public TransitionConditionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransitionConditionException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public TransitionConditionException(Throwable cause) {
		super(cause);
	}
}
