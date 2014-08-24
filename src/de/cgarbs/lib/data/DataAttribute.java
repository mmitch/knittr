package de.cgarbs.lib.data;

import java.io.Serializable;

import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.exception.ValidationError;

abstract public class DataAttribute implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	private final boolean nullable;
	private String attributeName;

	abstract public void setValue(Object newValue) throws DataException;
	abstract public Object getValue();

	abstract public void validate() throws ValidationError;

	// Builder pattern start
	public abstract static class Builder<T extends Builder<?>>
	{
		public abstract DataAttribute build();

		@SuppressWarnings("unchecked")
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

	protected void validate(Object value) throws ValidationError
	{
		if (! nullable && value == null)
		{
			throw new ValidationError(
					this,
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
