package com.shigengyu.hyperion.extensions.parameterconversion;

import com.shigengyu.hyperion.core.TransitionParameter;

public abstract class ParameterConverterBase<TSource, TTarget> implements ParameterConverter<TSource, TTarget> {

	private final Class<TSource> sourceClass;

	private final Class<TTarget> targetClass;

	protected ParameterConverterBase(Class<TSource> sourceClass, Class<TTarget> targetClass) {
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
	}

	@Override
	public final TransitionParameter convert(TransitionParameter parameter) {

		// Skip conversion if parameter value is null
		if (parameter.getValue() == null) {
			return parameter;
		}

		if (!sourceClass.isAssignableFrom(parameter.getValue().getClass())) {
			throw new ParameterConversionException(
					"Parameter type does not match converter. Unable to convert parameter [{}] with value [{}] from [{}] to [{}] using converter [{}]",
					parameter.getName(), parameter.getValue(), parameter.getValue().getClass().getName(), targetClass
					.getName(), this.getClass().getName());
		}

		TTarget convertedValue = doConversion(sourceClass.cast(parameter.getValue()));
		return new TransitionParameter(parameter.getName(), convertedValue);
	}

	protected abstract TTarget doConversion(TSource source);
}
