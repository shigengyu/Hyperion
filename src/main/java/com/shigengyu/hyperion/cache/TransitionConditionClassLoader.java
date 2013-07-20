package com.shigengyu.hyperion.cache;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheLoader;
import com.shigengyu.hyperion.core.TransitionCondition;

@Service
public class TransitionConditionClassLoader extends
		CacheLoader<Class<? extends TransitionCondition>, TransitionCondition> {

	@Override
	public TransitionCondition load(Class<? extends TransitionCondition> key) throws Exception {
		return key.newInstance();
	}
}
