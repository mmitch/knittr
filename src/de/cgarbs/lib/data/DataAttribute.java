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


	private transient final boolean nullable;
	private String key;
	private DataModel model;
	protected transient Object cleanValue;

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
					"null not allowed",
					ValidationError.ERROR.NULL_NOT_ALLOWED
					);
		}
	}

	public String getAttributeName()
	{
		return getModel().getModelName() + "." + getKey();
	}

	void setKey(String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key;
	}

	void setModel(DataModel model)
	{
		this.model = model;
	}

	public DataModel getModel()
	{
		return model;
	}

	public void resetDirty()
	{
		cleanValue = getValue();
	}

	public boolean isDirty()
	{
		if (getValue() == null)
		{
			return cleanValue != null;
		}
		return ! getValue().equals(cleanValue);
	}
}
