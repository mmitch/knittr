package de.cgarbs.lib.data;

import de.cgarbs.lib.exception.ValidationError;

public abstract class NumberAttribute extends DataAttribute
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected Number value;

	private transient final Number minValue;
	private transient final Number maxValue;

	// Builder pattern start
	public abstract static class Builder<N extends Number> extends DataAttribute.Builder<Builder<N>>
	{
		public Builder<N> setMinValue(N minValue)
		{
			this.minValue = minValue;
			return this;
		}
		public Builder<N> setMaxValue(N maxValue)
		{
			this.maxValue = maxValue;
			return this;
		}

		private Number minValue;
		private Number maxValue;
	}

	protected NumberAttribute(Builder<?> builder)
	{
		super(builder);
		minValue = builder.minValue;
		maxValue = builder.maxValue;
	}

	// Builder pattern end

	@Override
	public Object getValue()
	{
		return value;
	}

	@Override
	public void validate() throws ValidationError
	{
		super.validate(value);

		double dvalue = value.doubleValue();

		if (minValue != null && dvalue < minValue.doubleValue())
		{
			throw new ValidationError(
					this,
					"value too small: " + value + " < " + minValue
					);
		}
		if (maxValue != null && dvalue > maxValue.doubleValue())
		{
			throw new ValidationError(
					this,
					"value too big: " + value + " > " + maxValue
					);
		}
	}

}
