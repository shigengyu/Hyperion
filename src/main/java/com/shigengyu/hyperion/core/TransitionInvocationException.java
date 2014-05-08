package com.shigengyu.hyperion.core;

import com.shigengyu.common.StringMessage;

/**
 * Wraps exception thrown when invoking transition methods. This exception is not a Hyperion engine exception.
 *
 * @author Gengyu (Univer) Shi
 *
 */
public class TransitionInvocationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TransitionInvocationException(final String message) {
		super(message);
	}

	public TransitionInvocationException(final String message, final Object... args) {
		super(StringMessage.with(message, args));
	}

	public TransitionInvocationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public TransitionInvocationException(final String message, final Throwable cause, final Object... args) {
		super(StringMessage.with(message, args), cause);
	}

	public TransitionInvocationException(final Throwable cause) {
		super(cause);
	}
}
