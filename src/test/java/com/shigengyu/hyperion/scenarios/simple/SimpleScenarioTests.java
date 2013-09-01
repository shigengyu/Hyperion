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

package com.shigengyu.hyperion.scenarios.simple;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.shigengyu.hyperion.cache.WorkflowDefinitionCache;
import com.shigengyu.hyperion.cache.WorkflowStateCache;
import com.shigengyu.hyperion.cache.WorkflowTransitionCache;
import com.shigengyu.hyperion.config.HyperionProperties;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowState;
import com.shigengyu.hyperion.environment.TestEnvironment;
import com.shigengyu.hyperion.services.WorkflowContextBinarySerializer;
import com.shigengyu.hyperion.services.WorkflowContextXmlSerializer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(TestEnvironment.APPLICATION_CONTEXT_CONFIG)
public class SimpleScenarioTests {

	@Resource
	private WorkflowContextBinarySerializer binarySerializer;

	@Resource
	private HyperionProperties hyperionProperties;

	@Resource
	private WorkflowContextXmlSerializer xmlSerializer;

	@Test
	public void binarySerialization() {
		SimpleWorkflowContext workflowContext = new SimpleWorkflowContext();
		workflowContext.setName("shigengyu");
		workflowContext.setNumber(42);
		String base64String = binarySerializer.serialize(workflowContext);

		SimpleWorkflowContext deserializedWorkflowContext = binarySerializer.deserialize(base64String);
		Assert.assertEquals("shigengyu", deserializedWorkflowContext.getName());
		Assert.assertEquals(42, deserializedWorkflowContext.getNumber());
	}

	@Test
	public void loadDefinitions() {
		WorkflowDefinitionCache.getInstance().scanPackages("com.shigengyu.hyperion.scenarios.simple");
	}

	@Test
	public void loadStates() {
		WorkflowStateCache.getInstance().scanPackages("com.shigengyu.hyperion.scenarios.simple");
		Assert.assertTrue(WorkflowStateCache.getInstance().getAll().size() > 0);
		Assert.assertNotNull(WorkflowState.of(InitializedState.class));
	}

	@Test
	public void loadTransitions() {
		WorkflowDefinition workflowDefinition = WorkflowDefinitionCache.getInstance().get(SimpleWorkflow.class);
		Assert.assertNotNull(workflowDefinition);

		Assert.assertEquals(2, WorkflowTransitionCache.getInstance().get(workflowDefinition).size());
		Assert.assertEquals(1, WorkflowTransitionCache.getInstance().get(workflowDefinition, "start").size());
		Assert.assertEquals(0, WorkflowTransitionCache.getInstance().get(workflowDefinition, "doesNotExist").size());
	}

	@Test
	public void xmlSerialization() {
		SimpleWorkflowContext workflowContext = new SimpleWorkflowContext();
		workflowContext.setName("shigengyu");
		workflowContext.setNumber(42);
		String xml = xmlSerializer.serialize(workflowContext);

		SimpleWorkflowContext deserializedWorkflowContext = xmlSerializer.deserialize(xml);
		Assert.assertEquals("shigengyu", deserializedWorkflowContext.getName());
		Assert.assertEquals(42, deserializedWorkflowContext.getNumber());
	}
}
