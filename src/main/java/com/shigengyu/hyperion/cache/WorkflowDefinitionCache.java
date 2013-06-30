package com.shigengyu.hyperion.cache;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.shigengyu.hyperion.core.WorkflowDefinition;

public class WorkflowDefinitionCache {

	private LoadingCache<Class<? extends WorkflowDefinition>, WorkflowDefinition> cache;

	@Value("${hyperion.cache.definition.timeout.duration}")
	private int timeoutDuration;

	@Value("${hyperion.cache.definition.timeout.timeunit}")
	private TimeUnit timeoutTimeUnit;

	@Autowired
	private WorkflowDefinitionCacheLoader workflowDefinitionCacheLoader;

	public int getTimeoutDuration() {
		return timeoutDuration;
	}

	@PostConstruct
	private void initialize() {
		cache = CacheBuilder.newBuilder()
				.expireAfterAccess(timeoutDuration, timeoutTimeUnit)
				.build(workflowDefinitionCacheLoader);
	}
}
