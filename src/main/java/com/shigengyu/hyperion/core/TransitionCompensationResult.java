/*******************************************************************************
 * Copyright 2013-2014 Gengyu (Univer) Shi
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
