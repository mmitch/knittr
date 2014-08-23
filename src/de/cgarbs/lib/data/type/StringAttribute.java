package de.cgarbs.lib.data.type;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;

public class StringAttribute extends DataAttribute
{
	private String value;

	private final Integer minLength;
	private final Integer maxLength;

	// Builder pattern start
	public static class Builder extends DataAttribute.Builder<Builder>
	{
		public StringAttribute build()
		{
			return new StringAttribute(this);
		}
		public Builder setMinLength(Integer minLength)
		{
			this.minLength = minLength;
			return this;
		}
		public Builder setMaxLength(Integer maxLength)
		{
			this.maxLength = maxLength;
			return this;
		}

		private Integer minLength;
		private Integer maxLength;
	}

	public static Builder builder()
	{
		return new Builder();
	}

	private StringAttribute(Builder builder)
	{
		super(builder);
		minLength = builder.minLength;
		maxLength = builder.maxLength;
	}
	// Builder pattern end

	@Override
	public void setValue(Object newValue) throws DataException
	{
		if (newValue == null)
		{
			value = null;
		}
		else if (newValue instanceof String)
		{
			value = (String)newValue; // Strings are immutable, this is OK
		}
		else
		{
			value = String.valueOf(newValue);
		}
		validate();
	}

	@Override
	public Object getValue()
	{
		return value;
	}

	@Override
	protected void validate() throws DataException
	{
		super.validate(value);
		int length = (value == null ? 0 : value.length());
		if (minLength != null && length < minLength)
		{
			throw new DataException(
					DataException.ERROR.INVALID_VALUE,
					"string too short: " + length + " < " + minLength
					);
		}
		if (maxLength != null && length > maxLength)
		{
			throw new DataException(
					DataException.ERROR.INVALID_VALUE,
					"string too long: " + length + " > " + maxLength
					);
		}
	}

}
