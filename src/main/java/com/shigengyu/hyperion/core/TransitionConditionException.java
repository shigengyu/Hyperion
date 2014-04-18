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
