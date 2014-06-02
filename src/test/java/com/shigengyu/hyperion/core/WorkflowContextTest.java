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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.shigengyu.hyperion.environment.TestEnvironment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(TestEnvironment.APPLICATION_CONTEXT_CONFIG)
public class WorkflowContextTest {

	@Test(expected = WorkflowContextException.class)
	public void testInvalidValueTypeCast() {
		WorkflowContext workflowContext = new WorkflowContext();
		workflowContext.put("Name", "Hyperion");
		Integer number = workflowContext.get(Integer.class, "Name");
		Assert.fail("Failed to catch class cast exception when casting a string to number [" + number + "]");
	}
}
