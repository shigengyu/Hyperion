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
package com.shigengyu.hyperion.logging.services;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.shigengyu.hyperion.core.TransitionExecutionResult;
import com.shigengyu.hyperion.core.WorkflowExecutionException;
import com.shigengyu.hyperion.core.WorkflowInstance;

@Component
@Aspect
public class WorkflowExecutionServiceLoggingAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowExecutionServiceLoggingAspect.class);

	@Around("execution(* com.shigengyu.hyperion.services.WorkflowExecutionService.execute(..))")
	public TransitionExecutionResult aroundExecute(ProceedingJoinPoint joinPoint) {

		try {
			WorkflowInstance workflowInstance = (WorkflowInstance) joinPoint.getArgs()[0];
			Object transition = joinPoint.getArgs()[1];

			LOGGER.debug("Before executing transition [{}] on workflow instance [{}]", transition, workflowInstance);

			TransitionExecutionResult result = (TransitionExecutionResult) joinPoint.proceed();

			LOGGER.debug("After executing transition [{}] on workflow instance [{}]", transition, workflowInstance);
			return result;
		}
		catch (WorkflowExecutionException e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			throw e;
		}
		catch (Throwable e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			throw new RuntimeException("Unexcepted exception type caught when executing workflow transition.", e);
		}
	}
}
