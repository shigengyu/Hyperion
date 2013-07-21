package com.shigengyu.hyperion.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorkflowInstanceParameters {

	private final Map<String, Object> map;

	public WorkflowInstanceParameters() {
		map = new ConcurrentHashMap<String, Object>();
	}

	public WorkflowInstanceParameters clear() {
		map.clear();
		return this;
	}

	public <T> T get(Class<T> key) {
		if (key == null) {
			return null;
		}

		return (T) map.get(key.getName());
	}

	public <T> T get(String key) {
		if (key == null) {
			return null;
		}

		return (T) map.get(key);
	}

	public <T> WorkflowInstanceParameters set(String key, T value) {
		map.put(key, value);
		return this;
	}

	public <T> WorkflowInstanceParameters set(T value) {
		if (value != null) {
			map.put(value.getClass().getName(), value);
		}
		return this;
	}
}
