package com.shigengyu.hyperion.cache;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheLoader;
import com.shigengyu.hyperion.core.WorkflowTransition;

@Service
public class WorkflowTransitionCacheLoader extends CacheLoader<Class<? extends WorkflowTransition>, WorkflowTransition> {

	@Override
	public WorkflowTransition load(final Class<? extends WorkflowTransition> key) throws Exception {
		return key.getConstructor().newInstance();
	}
}