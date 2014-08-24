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
				String s = String.valueOf(newValue);
				if (s.length() == 0)
				{
					value = 0;
				}
				else
				{
					value = Integer.valueOf(s);
				}
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
	}
}
