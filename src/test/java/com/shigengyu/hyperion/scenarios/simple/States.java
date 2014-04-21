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
package com.shigengyu.hyperion.scenarios.simple;

import com.shigengyu.hyperion.core.State;
import com.shigengyu.hyperion.core.WorkflowState;

public class States {

	@State(id = "71e185d4-e4d6-451b-a98f-af4d41b4e943", name = "Endless")
	public static class Endless extends WorkflowState {

	}

	@State(id = "87456550-3c7d-4292-a054-14f423839716", name = "Initialized")
	public static class Initialized extends WorkflowState {

	}

	@State(id = "220dc5bb-5669-4c21-a7c8-7e6b32e5f522", name = "Completed")
	public static class WorkCompleted extends WorkflowState {

	}

	@State(id = "832f4ccd-4704-4e3e-a906-79b055ef6106", name = "Work in Progress")
	public static class WorkInProgress extends WorkflowState {

	}
}
