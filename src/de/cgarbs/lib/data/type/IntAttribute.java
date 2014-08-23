package de.cgarbs.lib.data.type;

import de.cgarbs.lib.data.NumberAttribute;
import de.cgarbs.lib.exception.DataException;

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
	}

	// Builder pattern end

	@Override
	public void setValue(Object newValue) throws DataException
	{
		if (newValue == null)
		{
			value = null;
		}
		else if (newValue instanceof Integer)
		{
			value = Integer.valueOf(((Integer)newValue).intValue());
		}
		else
		{
			try
			{
				value = Integer.valueOf(String.valueOf(newValue));
			}
			catch (NumberFormatException nfe)
			{
				throw new DataException(
						DataException.ERROR.INVALID_VALUE,
						"can't parse Integer",
						nfe
						);
			}
		}
		validate();
	}
}
