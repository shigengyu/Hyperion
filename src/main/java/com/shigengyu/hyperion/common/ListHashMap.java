package com.shigengyu.hyperion.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Function;

public class ListHashMap<K, V> {

	private final Map<K, List<V>> map = new ConcurrentHashMap<>();

	public void addAll(Iterable<V> values, Function<V, K> keySelector) {
		for (V value : values) {
			K key = keySelector.apply(value);
			addItem(key, value);
		}
	}

	public void addItem(K key, V value) {
		if (!map.containsKey(key)) {
			map.put(key, new ArrayList<V>());
		}
		map.get(key).add(value);
	}

	public boolean containsItems(String key, V item) {
		return map.containsKey(key) && map.get(key).contains(item);
	}

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	public List<V> get(K key) {
		return map.get(key);
	}

	public List<V> remove(String key) {
		return map.remove(key);
	}

	public boolean removeItem(String key, V value) {
		if (!map.containsKey(key)) {
			return false;
		}
		return map.get(key).remove(value);
	}

	public int size() {
		return map.size();
	}
}
