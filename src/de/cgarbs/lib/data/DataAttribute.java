package de.cgarbs.lib.data;

import de.cgarbs.lib.exception.DataException;

abstract public class DataAttribute
{
	private final boolean nullable;
	private String attributeName;

	abstract public void setValue(Object newValue) throws DataException;
	abstract public Object getValue();

	abstract protected void validate() throws DataException;

	// Builder pattern start
	public abstract static class Builder<T extends Builder<?>>
	{
		public abstract DataAttribute build();

		public T setNullable(Boolean nullable)
		{
			this.nullable = nullable;
			return (T) this;
		}

		private boolean nullable = true;
	};

	protected DataAttribute(Builder<?> builder)
	{
		this.nullable = builder.nullable;
	}
	// Builder pattern end

	protected void validate(Object value) throws DataException
	{
		if (! nullable && value == null)
		{
			throw new DataException(
					DataException.ERROR.INVALID_VALUE,
					"null not allowed"
					);
		}
	}

	public String getAttributeName()
	{
		return attributeName;
	}

	void setAttributeName(String attributeName)
	{
		this.attributeName = attributeName;
	}
}
