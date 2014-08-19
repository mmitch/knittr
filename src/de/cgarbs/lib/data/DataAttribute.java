package de.cgarbs.lib.data;

import de.cgarbs.lib.exception.DataException;

abstract public class DataAttribute
{
	private final Boolean nullable;

	abstract public void setValue(Object newValue) throws DataException;
	abstract public Object getValue() throws DataException;
	
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
		
		private Boolean nullable;
	};
	
	protected DataAttribute(Builder<?> builder)
	{
		this.nullable = builder.nullable;
	}
	
	// Builder pattern end
	
	protected void validate(Object value) throws DataException
	{
		if (Boolean.FALSE.equals(nullable) && value == null)
		{
			throw new DataException(
					DataException.ERROR.INVALID_VALUE,
					"null not allowed"
					);
		}
	}
}
