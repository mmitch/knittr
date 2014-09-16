package de.cgarbs.lib.data.type;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;

public class BooleanAttribute extends DataAttribute
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected Boolean value;

	// Builder pattern start
	public static class Builder extends DataAttribute.Builder<Builder>
	{
		public BooleanAttribute build()
		{
			return new BooleanAttribute(this);
		}
	}

	private BooleanAttribute(Builder builder)
	{
		super(builder);
	}

	public static Builder builder()
	{
		return new Builder();
	}

	// Builder pattern end

	@Override
	public Object getValue()
	{
		return value;
	}

	@Override
	public void setValue(Object newValue) throws DataException
	{
		if (newValue == null)
		{
			value = null;
		}
		else if (newValue instanceof Boolean)
		{
			value = (Boolean) newValue;
		}
		else
		{
			throw new DataException(
					DataException.ERROR.INVALID_VALUE,
					"wrong type: " + newValue.getClass() + " != " + Boolean.class
					);
		}
	}
}
