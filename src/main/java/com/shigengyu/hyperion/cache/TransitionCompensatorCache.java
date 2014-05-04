package com.shigengyu.hyperion.cache;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.shigengyu.hyperion.core.TransitionCompensator;
import com.shigengyu.hyperion.core.WorkflowTransitionException;

public class TransitionCompensatorCache {

	private final LoadingCache<Class<? extends TransitionCompensator>, TransitionCompensator> cache;

	@Autowired
	private TransitionCompensatorCache(final TransitionCompensatorCacheLoader transitionCompensatorCacheLoader) {
		cache = CacheBuilder.newBuilder().build(transitionCompensatorCacheLoader);
	}

	public synchronized <T extends TransitionCompensator> TransitionCompensator get(
			final Class<T> transitionCompensatorClass) {
		try {
			return cache.get(transitionCompensatorClass);
		}
		catch (ExecutionException e) {
			throw new WorkflowTransitionException("Failed to get transition compensator by type [{}]",
					transitionCompensatorClass, e);
		}
	}
}
