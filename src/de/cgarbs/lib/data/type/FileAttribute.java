package de.cgarbs.lib.data.type;

import java.io.File;
import java.net.URI;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;

public class FileAttribute extends DataAttribute
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private File value;

	private final boolean mustExist;
	private final boolean mustRead;
	private final boolean mustWrite;

	// Builder pattern start
	public static class Builder extends DataAttribute.Builder<Builder>
	{
		public FileAttribute build()
		{
			return new FileAttribute(this);
		}
		public Builder setMustExist(boolean mustExist)
		{
			this.mustExist = mustExist;
			return this;
		}
		public Builder setMustRead(boolean mustRead)
		{
			this.mustRead = mustRead;
			return this;
		}
		public Builder setMustWrite(boolean mustWrite)
		{
			this.mustWrite = mustWrite;
			return this;
		}

		private boolean mustExist = false;
		private boolean mustRead  = false;
		private boolean mustWrite = false;
	}

	public static Builder builder()
	{
		return new Builder();
	}

	private FileAttribute(Builder builder)
	{
		super(builder);
		this.mustExist = builder.mustExist;
		this.mustRead  = builder.mustRead;
		this.mustWrite = builder.mustWrite;
	}
	// Builder pattern end

	@Override
	public void setValue(Object newValue) throws DataException
	{
		if (newValue == null)
		{
			value = null;
		}
		else if (newValue instanceof File)
		{
			value = new File(((File) newValue).toURI());
		}
		else if (newValue instanceof String)
		{
			value = new File((String) newValue);
		}
		else if (newValue instanceof URI)
		{
			value = new File((URI) newValue);
		}
		else
		{
			throw new DataException(
					DataException.ERROR.INVALID_VALUE,
					"wrong type: " + newValue.getClass() + " != " + File.class
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

		boolean exists   = value.exists();
		boolean writable = value.canWrite();
		boolean readable = value.canRead();

		if (mustExist && ! exists)
		{
			throw new DataException(
					DataException.ERROR.VALIDATION_ERROR,
					"file does not exist: " + value
					);
		}
		if (mustRead && ! readable)
		{
			throw new DataException(
					DataException.ERROR.VALIDATION_ERROR,
					"file not readable: " + value
					);
		}
		if (mustWrite && exists && ! writable)
		{
			throw new DataException(
					DataException.ERROR.VALIDATION_ERROR,
					"file not writable: " + value
					);
		}
	}

}
