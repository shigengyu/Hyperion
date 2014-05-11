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

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.shigengyu.hyperion.HyperionRuntime;
import com.shigengyu.hyperion.cache.WorkflowTransitionCache;
import com.shigengyu.hyperion.config.HyperionProperties;
import com.shigengyu.hyperion.core.AutoTransitionRecursionLimitExceededException;
import com.shigengyu.hyperion.core.WorkflowContextBinarySerializer;
import com.shigengyu.hyperion.core.WorkflowContextXmlSerializer;
import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.core.WorkflowState;
import com.shigengyu.hyperion.core.WorkflowStateSet;
import com.shigengyu.hyperion.environment.TestEnvironment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(TestEnvironment.APPLICATION_CONTEXT_CONFIG)
public class SimpleScenarioTests {

	@Resource
	private WorkflowContextBinarySerializer binarySerializer;

	@Resource
	private HyperionProperties hyperionProperties;

	@Resource
	private HyperionRuntime hyperionRuntime;

	@Resource
	private WorkflowContextXmlSerializer xmlSerializer;

	@Test
	@Transactional
	public void binarySerialization() {
		SimpleWorkflowContext workflowContext = new SimpleWorkflowContext();
		workflowContext.setName("shigengyu");
		workflowContext.setNumber(42);
		String base64String = binarySerializer.serialize(workflowContext);

		SimpleWorkflowContext deserializedWorkflowContext = binarySerializer.deserialize(SimpleWorkflowContext.class,
				base64String);
		Assert.assertEquals("shigengyu", deserializedWorkflowContext.getName());
		Assert.assertEquals(42, deserializedWorkflowContext.getNumber());
	}

	@Test
	@Transactional
	public void getSetParameters() {
		WorkflowInstance workflowInstance = hyperionRuntime.newWorkflowInstance(SimpleWorkflow.class);
		Assert.assertNotNull(workflowInstance);

		workflowInstance.setParameter("Name", "Hyperion");
		workflowInstance.setParameter("Number", 42);

		String name = workflowInstance.getParameter(String.class, "Name");
		Integer number = workflowInstance.getParameter(Integer.class, "Number");

		Assert.assertEquals("Hyperion", name);
		Assert.assertEquals(new Integer(42), number);
	}

	@Before
	public void initialize() {
		hyperionRuntime.start();
	}

	@Test
	@Transactional
	public void loadStates() {
		Assert.assertTrue(hyperionRuntime.getWorkflowStateCache().getAll().size() > 0);
		Assert.assertNotNull(WorkflowState.of(States.Initialized.class));
	}

	@Test
	@Transactional
	public void loadTransitions() {
		WorkflowDefinition workflowDefinition = hyperionRuntime.getWorkflowDefinitionCache().get(SimpleWorkflow.class);
		Assert.assertNotNull(workflowDefinition);

		Assert.assertEquals(2, WorkflowTransitionCache.getInstance().get(workflowDefinition).size());
		Assert.assertEquals(1, WorkflowTransitionCache.getInstance().get(workflowDefinition, "start").size());
		Assert.assertEquals(0, WorkflowTransitionCache.getInstance().get(workflowDefinition, "doesNotExist").size());
	}

	@Test
	@Transactional
	public void testAutoTransitions() {
		WorkflowInstance workflowInstance = hyperionRuntime.newWorkflowInstance(AutoTransitionWorkflow.class);
		Assert.assertNotNull(workflowInstance);
		Assert.assertTrue(workflowInstance.getWorkflowStateSet().isSameWith(
				WorkflowStateSet.from(States.WorkCompleted.class)));
	}

	@Test
	@Transactional
	public void testGetExistingWorkflowInstance() {
		WorkflowInstance workflowInstance = hyperionRuntime.newWorkflowInstance(SimpleWorkflow.class);
		hyperionRuntime.getExistingWorkflowInstance(workflowInstance.getWorkflowInstanceId());
	}

	@Test
	@Transactional
	public void testRecursion() {
		WorkflowInstance workflowInstance = hyperionRuntime.newWorkflowInstance(RecursiveTransitionWorkflow.class);
		Assert.assertNotNull(workflowInstance);
		Assert.assertTrue(workflowInstance.getWorkflowStateSet().isSameWith(
				WorkflowStateSet.from(States.Initialized.class)));

		hyperionRuntime.executeTransition(workflowInstance, "start");
		Assert.assertTrue(workflowInstance.getWorkflowStateSet().isSameWith(
				WorkflowStateSet.from(States.WorkCompleted.class)));
	}

	@Test(expected = AutoTransitionRecursionLimitExceededException.class)
	@Transactional
	public void testRecursionEndless() {
		WorkflowInstance workflowInstance = hyperionRuntime.newWorkflowInstance(RecursiveTransitionWorkflow.class);
		Assert.assertNotNull(workflowInstance);
		Assert.assertTrue(workflowInstance.getWorkflowStateSet().isSameWith(
				WorkflowStateSet.from(States.Initialized.class)));

		hyperionRuntime.executeTransition(workflowInstance, "startEndless");
	}

	@Test(expected = AutoTransitionRecursionLimitExceededException.class)
	@Transactional
	public void testRecursionExceedLimit() {
		WorkflowInstance workflowInstance = hyperionRuntime.newWorkflowInstance(RecursiveTransitionWorkflow.class);
		Assert.assertNotNull(workflowInstance);
		Assert.assertTrue(workflowInstance.getWorkflowStateSet().isSameWith(
				WorkflowStateSet.from(States.Initialized.class)));

		hyperionRuntime.executeTransition(workflowInstance, "startExceedLimit");
	}

	@Test
	@Transactional
	public void testTransition() {
		WorkflowInstance workflowInstance = hyperionRuntime.newWorkflowInstance(SimpleWorkflow.class);
		Assert.assertNotNull(workflowInstance);
		Assert.assertTrue(workflowInstance.getWorkflowStateSet().isSameWith(
				WorkflowStateSet.from(States.Initialized.class)));

		hyperionRuntime.executeTransition(workflowInstance, "start");
		Assert.assertTrue(workflowInstance.getWorkflowStateSet().isSameWith(
				WorkflowStateSet.from(States.WorkInProgress.class)));

		hyperionRuntime.executeTransition(workflowInstance, "complete");
		Assert.assertTrue(workflowInstance.getWorkflowStateSet().isSameWith(
				WorkflowStateSet.from(States.WorkCompleted.class)));
	}

	@Test
	@Transactional
	public void xmlSerialization() {
		SimpleWorkflowContext workflowContext = new SimpleWorkflowContext();
		workflowContext.setName("shigengyu");
		workflowContext.setNumber(42);
		String xml = xmlSerializer.serialize(workflowContext);

		SimpleWorkflowContext deserializedWorkflowContext = xmlSerializer.deserialize(SimpleWorkflowContext.class, xml);
		Assert.assertEquals("shigengyu", deserializedWorkflowContext.getName());
		Assert.assertEquals(42, deserializedWorkflowContext.getNumber());
	}
}
