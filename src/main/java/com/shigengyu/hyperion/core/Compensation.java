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
