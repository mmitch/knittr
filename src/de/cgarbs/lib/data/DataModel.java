package de.cgarbs.lib.data;

import java.util.LinkedHashMap;
import java.util.Map;

import de.cgarbs.lib.exception.DataException;

abstract public class DataModel
{
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
}