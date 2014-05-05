package com.shigengyu.hyperion.core;

import com.shigengyu.common.StringMessage;

public class WorkflowBusinessViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WorkflowBusinessViolationException(final String message) {
		super(message);
	}

	public WorkflowBusinessViolationException(final String message, final Object... args) {
		super(StringMessage.with(message, args));
	}

	public WorkflowBusinessViolationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public WorkflowBusinessViolationException(final String message, final Throwable cause, final Object... args) {
		super(StringMessage.with(message, args), cause);
	}

	public WorkflowBusinessViolationException(final Throwable cause) {
		super(cause);
	}
}
