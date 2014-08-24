package de.cgarbs.lib.exception;

import java.util.HashMap;
import java.util.Map;

public class DataException extends Exception
{
	private static final long serialVersionUID = -2514745080334864317L;

	private static final Map<ERROR, String> ERRORTEXT = new HashMap<ERROR, String>();
	private ERROR error = ERROR.UNDEFINED;

	public enum ERROR {
		UNDEFINED,
		DUPLICATE_ATTRIBUTE,
		DUPLICATE_USAGE,
		UNKNOWN_ATTRIBUTE,
		INVALID_VALUE
	}

	static {
		ERRORTEXT.put(ERROR.UNDEFINED, "undefined");
		ERRORTEXT.put(ERROR.DUPLICATE_ATTRIBUTE, "duplicate attribute name");
		ERRORTEXT.put(ERROR.DUPLICATE_USAGE, "attribute used in multiple models");
		ERRORTEXT.put(ERROR.UNKNOWN_ATTRIBUTE, "unknown attribute");
		ERRORTEXT.put(ERROR.INVALID_VALUE, "invalid value");
	};

	public DataException(ERROR error)
	{
		this.error = error;
	}

	public DataException(ERROR error, String message)
	{
		super(message);
		this.error = error;
		System.err.println(this.toString());
	}

	public DataException(ERROR error, String message, Throwable t)
	{
		super(message, t);
		this.error = error;
	}

	@Override
	public String getMessage()
	{
		String message = ERRORTEXT.get(error);
		if (super.getMessage() != null)
		{
			message += "::" + super.getMessage();
		}
		return message;
	}

	public String getMessageOnly()
	{
		return super.getMessage();
	}

	public ERROR getError()
	{
		return error;
	}

	public DataException prependMessage(String prefix)
	{
		return new DataException(
				this.error,
				prefix + "::" + super.getMessage(),
				this.getCause()
				);
	}
}
