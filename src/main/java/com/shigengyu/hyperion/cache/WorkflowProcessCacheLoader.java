package com.shigengyu.hyperion.cache;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheLoader;
import com.shigengyu.hyperion.core.WorkflowProcess;

@Service
public class WorkflowProcessCacheLoader extends
		CacheLoader<Class<? extends WorkflowProcess>, WorkflowProcess> {

	@Override
	public WorkflowProcess load(final Class<? extends WorkflowProcess> key)
			throws Exception {
		return key.getConstructor().newInstance();
	}
}