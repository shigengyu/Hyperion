/*******************************************************************************
 * Copyright 2013-2014 Gengyu Shi
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
package com.shigengyu.hyperion.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jcip.annotations.GuardedBy;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shigengyu.hyperion.core.TransitionExecutionLog.Type;

public class TransitionExecutionResult {

	public static enum Status {

		NOT_EXCUTED,

		SUCCESS,

		FAILED
	}

	public static TransitionExecutionResult notExecuted() {
		return new TransitionExecutionResult(Status.NOT_EXCUTED);
	}

	public static TransitionExecutionResult success() {
		return new TransitionExecutionResult(Status.SUCCESS);
	}

	private final Map<String, Object> passoverObjects = Maps.newHashMap();

	private final List<TransitionExecutionLog> logs = Lists.newArrayList();

	private Status status;

	public TransitionExecutionResult() {
	}

	public TransitionExecutionResult(Status status) {
		this.status = status;
	}

	public final TransitionExecutionResult addLog(TransitionExecutionLog log) {
		logs.add(log);
		return this;
	}

	public final Collection<TransitionExecutionLog> getErrors() {
		return Collections2.filter(logs, new Predicate<TransitionExecutionLog>() {

			@Override
			public boolean apply(TransitionExecutionLog item) {
				return item.getType() == Type.ERROR;
			}
		});
	}

	public final Collection<TransitionExecutionLog> getInfo() {
		return Collections2.filter(logs, new Predicate<TransitionExecutionLog>() {

			@Override
			public boolean apply(TransitionExecutionLog item) {
				return item.getType() == Type.INFO;
			}
		});
	}

	public final Collection<TransitionExecutionLog> getLogs() {
		return logs;
	}

	@GuardedBy("this")
	public final synchronized Object getPassoverObject(final String key) {
		return passoverObjects.get(key);
	}

	@GuardedBy("this")
	public final synchronized <T> T getPassoverObject(final String key, Class<T> clazz) {
		return clazz.cast(getPassoverObject(key));
	}

	public final Status getStatus() {
		return status;
	}

	public final Collection<TransitionExecutionLog> getWarnings() {
		return Collections2.filter(logs, new Predicate<TransitionExecutionLog>() {

			@Override
			public boolean apply(TransitionExecutionLog item) {
				return item.getType() == Type.WARNING;
			}
		});
	}

	@GuardedBy("this")
	public final synchronized <T> void setPassoverObject(final Class<T> key, T value) {
		setPassoverObject(key.getName(), value);
	}

	@GuardedBy("this")
	public final synchronized <T> void setPassoverObject(final String key, final T value) {
		passoverObjects.put(key, value);
	}

	public final void setStatus(Status status) {
		this.status = status;
	}
}
