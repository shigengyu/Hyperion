/*******************************************************************************
 * Copyright 2013 Gengyu Shi
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

public class TransitionExecutionResult {

	public static enum TransitionExecutionResultStatus {

		ERROR,

		SUCCESS,

		WARNING
	}

	public static TransitionExecutionResult success() {
		return new TransitionExecutionResult(TransitionExecutionResultStatus.SUCCESS);
	}

	private TransitionExecutionResultStatus status;

	public TransitionExecutionResult() {
	}

	public TransitionExecutionResult(TransitionExecutionResultStatus status) {
		this.status = status;
	}

	public final TransitionExecutionResultStatus getStatus() {
		return status;
	}

	public final void setStatus(TransitionExecutionResultStatus status) {
		this.status = status;
	}
}
