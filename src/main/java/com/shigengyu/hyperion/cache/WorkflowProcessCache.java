package com.shigengyu.hyperion.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.shigengyu.hyperion.core.WorkflowProcess;
import com.shigengyu.hyperion.core.WorkflowStateException;

@Service
@Lazy(false)
public class WorkflowProcessCache {

	private static WorkflowProcessCache instance;

	public static WorkflowProcessCache getInstance() {
		return instance;
	}

	private LoadingCache<Class<? extends WorkflowProcess>, WorkflowProcess> cache;

	@Value("${hyperion.workflow.cache.process.timeout.duration}")
	private int timeoutDuration;

	@Value("${hyperion.workflow.cache.process.timeout.timeunit}")
	private TimeUnit timeoutTimeUnit;

	@Resource
	private WorkflowProcessCacheLoader WorkflowProcessCacheLoader;

	public <T extends WorkflowProcess> WorkflowProcess get(
			final Class<T> WorkflowProcessClass) {
		try {
			return cache.get(WorkflowProcessClass);
		} catch (final ExecutionException e) {
			throw new WorkflowStateException(
					"Failed to get workflow process by type [{}]",
					WorkflowProcessClass, e);
		}
	}

	public int getTimeoutDuration() {
		return timeoutDuration;
	}

	@PostConstruct
	private void initialize() {
		cache = CacheBuilder.newBuilder()
				.expireAfterAccess(timeoutDuration, timeoutTimeUnit)
				.build(WorkflowProcessCacheLoader);

		instance = this;
	}
}
