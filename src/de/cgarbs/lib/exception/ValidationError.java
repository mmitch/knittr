package de.cgarbs.lib.exception;

import de.cgarbs.lib.data.DataAttribute;

public class ValidationError extends Exception
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String message;
	private DataAttribute attribute;

	public ValidationError(DataAttribute attribute, String message)
	{
		this.attribute = attribute;
		this.message   = message;
	}

	public ValidationError(DataAttribute attribute, DataException e)
	{
		this(attribute, e.getMessageOnly());
	}

	public String getMessage()
	{
		return attribute.getAttributeName() + ": " + message;
	}


}
