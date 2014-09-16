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

	abstract public Object getValue();
	abstract protected void setValueInternal(Object newValue) throws DataException;
	abstract protected Object convertType(Object newValue) throws ValidationError;

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

	public final void validate() throws ValidationError
	{
		validate(getValue());
	}

	public void validate(Object value) throws ValidationError // FIXME rename parameter!
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

	public final void setValue(Object newValue) throws DataException
	{
		try
		{
			setValueInternal(convertType(newValue));
		}
		catch (ValidationError e)
		{
			throw new DataException(
					DataException.ERROR.INVALID_VALUE,
					e
					);
		}
	}
}
