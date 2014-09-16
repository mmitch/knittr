package de.cgarbs.lib.data;

import java.text.NumberFormat;

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

	protected transient NumberFormat numberFormat;

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
		numberFormat = NumberFormat.getInstance();
	}

	// Builder pattern end

	@Override
	public Object getValue()
	{
		return value;
	}

	public void validate(Object value) throws ValidationError
	{
		super.validate(value);

		if (value != null)
		{
			double dvalue = ((Double) value).doubleValue();

			if (minValue != null && dvalue < minValue.doubleValue())
			{
				throw new ValidationError(
						this,
						"value too small: " + value + " < " + minValue,
						ValidationError.ERROR.NUMBER_TOO_SMALL,
						String.valueOf(value), String.valueOf(minValue)
						);
			}
			if (maxValue != null && dvalue > maxValue.doubleValue())
			{
				throw new ValidationError(
						this,
						"value too big: " + value + " > " + maxValue,
						ValidationError.ERROR.NUMBER_TOO_LARGE,
						String.valueOf(value), String.valueOf(minValue)
						);
			}
		}
	}

	public String getFormattedValue()
	{
		if (getValue() == null)
		{
			return null; // FIXME or ""?
		}
		else
		{
			return numberFormat.format(getValue());
		}
	}

	public Number getMinValue()
	{
		return this.minValue;
	}

	public Number getMaxValue()
	{
		return this.maxValue;
	}
}
