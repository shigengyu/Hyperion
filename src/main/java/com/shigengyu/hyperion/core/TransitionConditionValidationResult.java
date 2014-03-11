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

import com.google.common.collect.Lists;

public class TransitionConditionValidationResult {

	public static final TransitionConditionValidationResult create() {
		return passed();
	}

	public static final TransitionConditionValidationResult passed() {
		return new TransitionConditionValidationResult(true);
	}

	private boolean passed;

	private final List<String> reasons = Lists.newArrayList();

	private TransitionConditionValidationResult(boolean passed) {
		this.passed = passed;
	}

	public final TransitionConditionValidationResult fail(String reason) {
		reasons.add(reason);
		passed = false;
		return this;
	}

	public Collection<String> getReasons() {
		return reasons;
	}

	public final boolean isPassed() {
		return passed;
	}

	public TransitionConditionValidationResult merge(final TransitionConditionValidationResult result) {
		for (String reason : result.reasons) {
			reasons.add(reason);
		}

		if (passed && !result.passed) {
			passed = false;
		}

		return this;
	}
}
