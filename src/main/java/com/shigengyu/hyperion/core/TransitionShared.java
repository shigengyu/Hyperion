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

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TransitionShared {

	boolean auto() default false;

	Class<? extends TransitionCondition>[] conditions() default {};

	Class<? extends WorkflowState>[] fromStates() default {};

	boolean hidden() default false;

	int maxEntry() default 1;

	boolean multiEntry() default false;

	String name() default "";

	StateTransitionStyle stateTransitionStyle() default StateTransitionStyle.REMOVE_AND_ADD;

	Class<? extends WorkflowState>[] toStates() default {};
}
