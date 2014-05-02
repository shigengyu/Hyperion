package com.shigengyu.hyperion.extensions.parameterconversion;

import com.shigengyu.hyperion.core.TransitionParameter;

public interface ParameterConverter<TSource, TTarget> {

	boolean canConvert(TransitionParameter parameter);

	TransitionParameter convert(TransitionParameter parameter);
}
