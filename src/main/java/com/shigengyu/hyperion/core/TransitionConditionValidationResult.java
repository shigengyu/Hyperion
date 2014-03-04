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
}
