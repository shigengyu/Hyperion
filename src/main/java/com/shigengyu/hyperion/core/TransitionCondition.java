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

import java.util.Collection;

import com.google.common.collect.Lists;

public interface TransitionCondition {

	public static class DefaultTransitionCondition implements TransitionCondition {

		@Override
		public boolean canPerform(WorkflowTransition workflowTransition, WorkflowInstance workflowInstance) {
			return true;
		}

		@Override
		public Collection<String> reasons() {
			return Lists.newArrayList();
		}
	}

	boolean canPerform(WorkflowTransition workflowTransition, WorkflowInstance workflowInstance);

	Collection<String> reasons();
}
