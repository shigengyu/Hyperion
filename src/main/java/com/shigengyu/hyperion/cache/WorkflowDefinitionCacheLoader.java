package com.shigengyu.hyperion.cache;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheLoader;
import com.shigengyu.hyperion.core.WorkflowDefinition;

@Service
public class WorkflowDefinitionCacheLoader extends
		CacheLoader<Class<? extends WorkflowDefinition>, WorkflowDefinition> {

	@Override
	public WorkflowDefinition load(final Class<? extends WorkflowDefinition> key)
			throws Exception {
		return key.getConstructor().newInstance();
	}
}
