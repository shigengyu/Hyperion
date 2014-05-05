/*******************************************************************************
 * Copyright 2013-2014 Gengyu (Univer) Shi
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

/**
 * The common interface for all compensators. Compensators need to be designed as stateless.
 *
 * @author Gengyu (Univer) Shi
 *
 */
public interface TransitionCompensator {

	boolean canHandle(Exception exception);

	TransitionCompensationResult compensate(WorkflowInstance workflowInstance);
}
