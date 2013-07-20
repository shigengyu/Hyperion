package com.shigengyu.hyperion.logging.cache;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.shigengyu.hyperion.cache.WorkflowStateCacheLoader;
import com.shigengyu.hyperion.core.WorkflowState;

@Component
@Aspect
public class WorkflowStateCacheLoaderLoggingAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowStateCacheLoader.class);

	/**
	 * Log loaded state after returning
	 * 
	 * @param joinPoint
	 * @param result
	 */
	@AfterReturning(pointcut = "execution(* com.shigengyu.hyperion.cache.WorkflowStateCacheLoader.load(..))", returning = "result")
	public void afterLoadReturning(JoinPoint joinPoint, WorkflowState result) {
		LOGGER.debug("State [{}] loaded", result.getName());
	}
}
