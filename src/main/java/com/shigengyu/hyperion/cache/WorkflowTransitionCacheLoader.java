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

package com.shigengyu.hyperion.cache;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.shigengyu.hyperion.core.Transition;
import com.shigengyu.hyperion.core.TransitionShared;
import com.shigengyu.hyperion.core.Transitions;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowTransition;

@Service
public class WorkflowTransitionCacheLoader extends CacheLoader<WorkflowDefinition, ImmutableList<WorkflowTransition>> {

	@Override
	public ImmutableList<WorkflowTransition> load(WorkflowDefinition workflowDefinition) throws Exception {

		List<WorkflowTransition> workflowTransitions = Lists.newArrayList();

		for (Method method : ReflectionUtils.getAllDeclaredMethods(workflowDefinition.getWorkflowDefinitionType())) {
			if (method.isAnnotationPresent(Transition.class)) {
				Transition transition = method.getAnnotation(Transition.class);
				TransitionShared transitionShared = method.getAnnotation(TransitionShared.class);
				workflowTransitions.add(new WorkflowTransition(method, transition, transitionShared));
			}
			else if (method.isAnnotationPresent(Transitions.class)) {
				Transitions transitions = method.getAnnotation(Transitions.class);
				for (Transition transition : transitions.value()) {
					TransitionShared transitionShared = method.getAnnotation(TransitionShared.class);
					workflowTransitions.add(new WorkflowTransition(method, transition, transitionShared));
				}
			}
		}

		return ImmutableList.copyOf(workflowTransitions);
	}
}
