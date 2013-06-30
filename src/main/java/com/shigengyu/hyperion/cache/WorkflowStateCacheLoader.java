package com.shigengyu.hyperion.cache;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheLoader;
import com.shigengyu.hyperion.core.WorkflowState;

@Service
public class WorkflowStateCacheLoader extends
		CacheLoader<Class<? extends WorkflowState>, WorkflowState> {

	@Override
	public WorkflowState load(final Class<? extends WorkflowState> key)
			throws Exception {
		return key.getConstructor().newInstance();
	}
}
