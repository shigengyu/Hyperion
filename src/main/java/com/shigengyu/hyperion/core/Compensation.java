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

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Compensation {

	/**
	 * The compensator to be triggered
	 *
	 * @return
	 */
	Class<? extends TransitionCompensator> compensator();

	/**
	 * The type of exception to trigger the compensation. By default handles all exceptions.
	 *
	 * @return
	 */
	Class<? extends Exception> exception() default Exception.class;
}
