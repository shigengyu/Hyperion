/*******************************************************************************
 * Copyright 2013-2014 Gengyu Shi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

	public static final TransitionExecutionLog error(final String message) {
		return error().setMessage(message);
	}

	public static final TransitionExecutionLog info() {
		return new TransitionExecutionLog(Type.INFO);
	}

	public static final TransitionExecutionLog info(final String message) {
		return info().setMessage(message);
	}

	public static final TransitionExecutionLog warning() {
		return new TransitionExecutionLog(Type.WARNING);
	}

	public static final TransitionExecutionLog warning(final String message) {
		return warning().setMessage(message);
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
