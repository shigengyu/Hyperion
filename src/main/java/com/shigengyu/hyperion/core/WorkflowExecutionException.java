package com.shigengyu.hyperion.core;

import com.shigengyu.hyperion.HyperionException;

public class WorkflowExecutionException extends HyperionException {

	private static final long serialVersionUID = 1L;

	public WorkflowExecutionException(String message) {
		super(message);
	}

	public WorkflowExecutionException(String message, Object... args) {
		super(message, args);
	}

	public WorkflowExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public WorkflowExecutionException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public WorkflowExecutionException(Throwable cause) {
		super(cause);
	}

}
