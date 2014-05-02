package com.shigengyu.hyperion.extensions.parameterconversion;

import com.shigengyu.hyperion.HyperionException;

public class ParameterConversionException extends HyperionException {

	private static final long serialVersionUID = 1L;

	public ParameterConversionException(String message) {
		super(message);
	}

	public ParameterConversionException(String message, Object... args) {
		super(message, args);
	}

	public ParameterConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParameterConversionException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public ParameterConversionException(Throwable cause) {
		super(cause);
	}
}
