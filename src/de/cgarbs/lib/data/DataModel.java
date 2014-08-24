package de.cgarbs.lib.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.exception.ValidationError;
import de.cgarbs.lib.exception.ValidationErrorList;

abstract public class DataModel implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	private Map<String, DataAttribute> attributes = new LinkedHashMap<String, DataAttribute>();

	public void addAttribute(String key, DataAttribute attribute) throws DataException
	{
		if (attributes.containsKey(key))
		{
			throw new DataException(
					DataException.ERROR.DUPLICATE_ATTRIBUTE,
					key
					);
		}

		String newAttributeName = getModelName() + "." + key;

		if (attribute.getAttributeName() != null)
		{
			throw new DataException(
					DataException.ERROR.DUPLICATE_USAGE,
					newAttributeName + ", " + attribute.getAttributeName()
					);
		}
		attribute.setAttributeName(newAttributeName);
		attributes.put(key, attribute);
	}

	public void setValue(String key, Object value) throws DataException
	{
		checkForAttribute(key);
		try
		{
			attributes.get(key).setValue(value);
		}
		catch (DataException dex)
		{
			throw dex.prependMessage("attribute " + key);
		}
	}

	/**
	 * FIXME return Object -> not type safe!
	 */
	public Object getValue(String key) throws DataException
	{
		return getAttribute(key).getValue();
	}

	public DataAttribute getAttribute(String key) throws DataException
	{
		checkForAttribute(key);
		return attributes.get(key);
	}

	private void checkForAttribute(String key) throws DataException
	{
		if (!attributes.containsKey(key))
		{
			throw new DataException(
					DataException.ERROR.UNKNOWN_ATTRIBUTE,
					key
					);
		}
	}

	abstract public String getModelName();

	// FIXME -> toString() bauen!

	public void writeToFile(File file) throws FileNotFoundException, IOException // FIXME wrap exceptions or not?!
	{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		out.writeObject(this);
		out.close();
	}

	public void readFromFile(File file) throws FileNotFoundException, IOException, ClassNotFoundException, DataException
	{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		DataModel d = (DataModel) in.readObject();
		in.close();

		for (String key: d.attributes.keySet())
		{
			this.setValue(key, d.getValue(key));
		}
	}

	public void validate() throws ValidationErrorList // FIXME: unneeded? remove?
	{
		ValidationErrorList ex = new ValidationErrorList(this);
		for (DataAttribute attribute: attributes.values())
		{
			try
			{
				attribute.validate();
			}
			catch (ValidationError e)
			{
				ex.addValidationError(e);
			}
		}
		if (! ex.getValidationErrors().isEmpty())
		{
			throw ex;
		}
	}
}
