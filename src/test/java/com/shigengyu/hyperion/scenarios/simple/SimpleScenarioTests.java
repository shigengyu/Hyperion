package com.shigengyu.hyperion.scenarios.simple;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.shigengyu.hyperion.cache.WorkflowDefinitionCache;
import com.shigengyu.hyperion.core.WorkflowState;
import com.shigengyu.hyperion.environment.TestEnvironment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(TestEnvironment.APPLICATION_CONTEXT_CONFIG)
public class SimpleScenarioTests {

	@Resource
	private TestEnvironment testEnvironment;

	@Test
	public void testLoadDefinitions() {
		WorkflowDefinitionCache.getInstance().loadPackages("com.shigengyu.hyperion.scenarios.simple");
	}

	@Test
	public void testLoadStates() {
		Assert.notNull(WorkflowState.of(InitializedState.class));
	}
}
