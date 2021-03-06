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
public @interface Transition {

	/**
	 * Whether the transition is auto performed. Auto transitions are only invoked as part of a non-auto transition
	 *
	 * @return A boolean value indicating whether the transition is auto performed
	 */
	boolean auto() default false;

	Compensation[] compensations() default {};

	Class<? extends TransitionCondition>[] conditions() default {};

	Class<? extends WorkflowState>[] fromStates() default {};

	/**
	 * Whether the transition is hidden. Hidden transitions can be executed programmatically but is not exposed to the
	 * Hyperion runtime interface
	 *
	 * @return A boolean value indicating whether the transition is hidden
	 */
	boolean hidden() default false;

	/**
	 * The maximum entries if the transition is multi-entry. This value will be ignored if the transition is not
	 * multi-entry
	 *
	 * @return An integrate indicating the maximum entries allowed for the transition.
	 */
	int maxEntry() default 1;

	/**
	 * Whether multi-entry is allowed on this transition, i.e. transition triggering itself
	 *
	 * @return A boolean value indicating whether the tranistion is multi-entry
	 */
	boolean multiEntry() default false;

	/**
	 * Name of the transition
	 *
	 * @return The name of the transition
	 */
	String name() default "";

	/**
	 * Whether or not to override the values of the {@link TransitionShared} annotation
	 *
	 * @return A boolean value indicating whether this {@link Transition} annotation can override the
	 *         {@link TransitionShared} annotation
	 */
	boolean override() default true;

	StateTransitionStyle stateTransitionStyle() default StateTransitionStyle.REMOVE_AND_ADD;

	Class<? extends WorkflowState>[] toStates() default {};
}
