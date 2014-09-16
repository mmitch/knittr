package de.cgarbs.lib.data;

import java.text.NumberFormat;
import java.text.ParseException;

import de.cgarbs.lib.exception.DataException;
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

	public void validate(Object value) throws ValidationError
	{
		Number n = (Number) convertType(value);

		super.validate(n);

		if (n != null)
		{
			double dvalue = n.doubleValue();

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

	@Override
	protected void setValueInternal(Object newValue) throws DataException
	{
		value = (Number) newValue;
	}

	@Override
	protected Object convertType(Object newValue) throws ValidationError
	{
		if (newValue == null)
		{
			return null;
		}
		else if (newValue instanceof Number)
		{
			return (Number) newValue;
		}
		else
		{
			try
			{
				String s = String.valueOf(newValue);
				if (s.length() == 0)
				{
					return null;
				}
				else
				{
					return numberFormat.parse(s);
				}
			}
			catch (ParseException e)
			{
				throw new ValidationError(
						this,
						"can't parse " + newValue.getClass() + " as number: " + newValue.toString(),
						ValidationError.ERROR.NUMBER_FORMAT
						);
			}
		}
	}
}
