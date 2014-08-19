package de.cgarbs.lib.data.types;

import de.cgarbs.lib.data.NumberAttribute;
import de.cgarbs.lib.exception.DataException;

public class FloatAttribute extends NumberAttribute
{
	
	// FIXME add decimal places
	
	// Builder pattern start
	public static class Builder extends NumberAttribute.Builder<Float>
	{
		public FloatAttribute build()
		{
			return new FloatAttribute(this);
		}
	}
	
	public static Builder builder()
	{
		return new Builder();
	}
	
	private FloatAttribute(Builder builder)
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
		else if (newValue instanceof Float)
		{
			value = Float.valueOf(((Float)newValue).floatValue());
		}
		else
		{
			try
			{
				value = Float.valueOf(String.valueOf(newValue));
			}
			catch (NumberFormatException nfe)
			{
				throw new DataException(
						DataException.ERROR.INVALID_VALUE,
						"can't parse Float",
						nfe
						);
			}
		}
		validate();
	}
}
