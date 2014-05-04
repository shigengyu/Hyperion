package com.shigengyu.hyperion.core;

public class StateDefinitionException extends HyperionDefinitionException {

	private static final long serialVersionUID = 1L;

	public StateDefinitionException(String message) {
		super(message);
	}

	public StateDefinitionException(String message, Object... args) {
		super(message, args);
	}

	public StateDefinitionException(String message, Throwable cause) {
		super(message, cause);
	}

	public StateDefinitionException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public StateDefinitionException(Throwable cause) {
		super(cause);
	}
}
