package de.cgarbs.lib.data.type;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.exception.ValidationError;

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
	protected void setValueInternal(Object newValue) throws DataException
	{
		value = (Boolean) newValue;
	}

	@Override
	protected Object convertType(Object newValue) throws ValidationError
	{
		if (newValue == null)
		{
			return null;
		}
		else if (newValue instanceof Boolean)
		{
			return (Boolean) newValue;
		}
		else
		{
			throw new ValidationError(
					this,
					"wrong type: " + newValue.getClass() + " != " + Boolean.class,
					ValidationError.ERROR.NUMBER_FORMAT // FIXME this is not number format error!
					);
		}
	}
}
