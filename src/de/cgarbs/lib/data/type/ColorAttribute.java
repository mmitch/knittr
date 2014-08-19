package de.cgarbs.lib.data.type;

import java.awt.Color;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;

public class ColorAttribute extends DataAttribute
{
	private Color value;

	// Builder pattern start
	public static class Builder extends DataAttribute.Builder<Builder>
	{
		public ColorAttribute build()
		{
			return new ColorAttribute(this);
		}
	}
	
	public static Builder builder()
	{
		return new Builder();
	}

	private ColorAttribute(Builder builder)
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
		else if (newValue instanceof Color)
		{
			value = new Color(
					((Color) newValue).getColorSpace(),
					((Color) newValue).getColorComponents(null),
					((Color) newValue).getAlpha()
					);
		}
		else
		{
			throw new DataException(
					DataException.ERROR.INVALID_VALUE,
					"wrong type: " + newValue.getClass() + " != " + Color.class
					);
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
	}

}
