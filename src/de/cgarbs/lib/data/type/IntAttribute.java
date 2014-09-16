package de.cgarbs.lib.data.type;

import java.text.NumberFormat;

import de.cgarbs.lib.data.NumberAttribute;

public class IntAttribute extends NumberAttribute
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// Builder pattern start
	public static class Builder extends NumberAttribute.Builder<Integer>
	{
		public IntAttribute build()
		{
			return new IntAttribute(this);
		}
	}

	public static Builder builder()
	{
		return new Builder();
	}

	private IntAttribute(Builder builder)
	{
		super(builder);
		numberFormat = NumberFormat.getIntegerInstance();
	}
	// Builder pattern end

	@Override
	public Object getValue()
	{
		if (value == null)
		{
			return null;
		}
		return value.intValue();
	}
}
