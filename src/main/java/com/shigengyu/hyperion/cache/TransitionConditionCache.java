package com.shigengyu.hyperion.cache;

import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.shigengyu.hyperion.core.TransitionCondition;
import com.shigengyu.hyperion.core.WorkflowStateException;

@Service
public class TransitionConditionCache {

	private static TransitionConditionCache instance;

	public static TransitionConditionCache getInstance() {
		return instance;
	}

	private LoadingCache<Class<? extends TransitionCondition>, TransitionCondition> cache;

	@Resource
	private TransitionConditionClassLoader transitionConditionClassLoader;

	public synchronized <T extends TransitionCondition> TransitionCondition get(final Class<T> transitionConditionClass) {
		try {
			return cache.get(transitionConditionClass);
		}
		catch (final ExecutionException e) {
			throw new WorkflowStateException("Failed to get transition condition by type [{}]",
					transitionConditionClass, e);
		}
	}

	@PostConstruct
	private void initialize() {
		cache = CacheBuilder.newBuilder().build(transitionConditionClassLoader);
		instance = this;
	}
}
