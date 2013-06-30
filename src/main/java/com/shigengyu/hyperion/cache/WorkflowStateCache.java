package com.shigengyu.hyperion.cache;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.shigengyu.hyperion.core.WorkflowState;

@Service
public class WorkflowStateCache {

	private LoadingCache<Class<? extends WorkflowState>, WorkflowState> cache;

	@Value("${hyperion.cache.state.timeout.duration}")
	private int timeoutDuration;

	@Value("${hyperion.cache.state.timeout.timeunit}")
	private TimeUnit timeoutTimeUnit;

	@Autowired
	private WorkflowStateCacheLoader workflowStateCacheLoader;

	public int getTimeoutDuration() {
		return timeoutDuration;
	}

	@PostConstruct
	private void initialize() {
		cache = CacheBuilder.newBuilder()
				.expireAfterAccess(timeoutDuration, timeoutTimeUnit)
				.build(workflowStateCacheLoader);
	}
}
