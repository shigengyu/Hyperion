/*******************************************************************************
 * Copyright 2013 Gengyu Shi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.shigengyu.hyperion.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Function;

public class ListHashMap<K, V> implements Iterable<Entry<K, List<V>>> {

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

	@Override
	public Iterator<Entry<K, List<V>>> iterator() {
		return map.entrySet().iterator();
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
