package de.cgarbs.lib.data.type;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.exception.ValidationError;

public class FileAttribute extends DataAttribute
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private File value;

	private transient final boolean mustExist;
	private transient final boolean mustRead;
	private transient final boolean mustWrite;
	private transient final FileFilter[] fileFilters;

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
		public Builder addFileFilter(String name, String... extensions)
		{
			this.filefilters.add(
					new FileNameExtensionFilter(name, extensions)
					);
			return this;
		}

		private boolean mustExist = false;
		private boolean mustRead  = false;
		private boolean mustWrite = false;
		private List<FileFilter> filefilters = new ArrayList<FileFilter>();
	}

	public static Builder builder()
	{
		return new Builder();
	}

	private FileAttribute(Builder builder)
	{
		super(builder);
		this.mustExist   = builder.mustExist;
		this.mustRead    = builder.mustRead;
		this.mustWrite   = builder.mustWrite;
		this.fileFilters = builder.filefilters.toArray(new FileFilter[0]);
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
	}

	@Override
	public Object getValue()
	{
		return value;
	}

	@Override
	public void validate() throws ValidationError
	{
		super.validate(value);

		boolean exists   = value.exists();
		boolean writable = value.canWrite();
		boolean readable = value.canRead();

		if (mustExist && ! exists)
		{
			throw new ValidationError(
					this,
					"file does not exist: " + value
					);
		}
		if (mustRead && ! readable)
		{
			throw new ValidationError(
					this,
					"file not readable: " + value
					);
		}
		if (mustWrite && exists && ! writable)
		{
			throw new ValidationError(
					this,
					"file not writable: " + value
					);
		}
	}

	public FileFilter[] getFileFilters()
	{
		return fileFilters;
	}

	@Override
	public boolean isDirty()
	{
		boolean dirty = super.isDirty();
		// consider relative and absolute paths equal, if they match
		if (dirty && cleanValue != null && value != null)
		{
			dirty = ! ((File)cleanValue).getAbsoluteFile().equals(value.getAbsoluteFile());
		}
		return dirty;
	}

}
