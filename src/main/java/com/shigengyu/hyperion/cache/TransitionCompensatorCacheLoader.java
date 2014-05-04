package com.shigengyu.hyperion.cache;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.cache.CacheLoader;
import com.shigengyu.hyperion.core.TransitionCompensator;

public class TransitionCompensatorCacheLoader extends
		CacheLoader<Class<? extends TransitionCompensator>, TransitionCompensator> implements ApplicationContextAware {

	@Override
	public TransitionCompensator load(Class<? extends TransitionCompensator> key) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub

	}
}
