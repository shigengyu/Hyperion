package com.shigengyu.hyperion.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The owner of the state. Providing optional information for extension. This is not mandatory on workflow states.
 *
 * @author Univer
 *
 */
@Target({ ElementType.TYPE, ElementType.PACKAGE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface StateOwner {

	Class<?> workflow() default WorkflowDefinition.class;
}
