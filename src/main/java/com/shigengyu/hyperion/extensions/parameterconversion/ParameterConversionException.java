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
