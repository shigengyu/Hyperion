/*******************************************************************************
 * Copyright 2013 Gengyu Shi
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
