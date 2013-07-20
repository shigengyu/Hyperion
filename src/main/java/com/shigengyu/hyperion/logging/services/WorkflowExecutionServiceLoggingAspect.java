package com.shigengyu.hyperion.logging.services;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.shigengyu.hyperion.core.TransitionExecutionResult;

@Component
@Aspect
public class WorkflowExecutionServiceLoggingAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowExecutionServiceLoggingAspect.class);

	@Around("execution(* com.shigengyu.hyperion.services.WorkflowExecutionService.execute(..))")
	public TransitionExecutionResult aroundExecute(ProceedingJoinPoint joinPoint) {

		try {
			LOGGER.debug("Before execute");

			TransitionExecutionResult result = (TransitionExecutionResult) joinPoint.proceed();

			LOGGER.debug("After execute");
			return result;
		}
		catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}
