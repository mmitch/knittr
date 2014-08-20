package de.cgarbs.lib.exception;

import java.util.HashMap;
import java.util.Map;

public class GlueException extends Exception
{
	private static final long serialVersionUID = -2514745080334864317L;
	
	private static final Map<ERROR, String> ERRORTEXT = new HashMap<ERROR, String>();
	private ERROR error = ERROR.UNDEFINED;
	
	public enum ERROR {
		UNDEFINED,
		BINDING_NOT_IMPLEMENTED,
	}

	static {
		ERRORTEXT.put(ERROR.UNDEFINED, "undefined");
		ERRORTEXT.put(ERROR.BINDING_NOT_IMPLEMENTED, "binding not implemented");
	};
	
	public GlueException(ERROR error)
	{
		this.error = error;
	}
	
	public GlueException(ERROR error, String message)
	{
		super(message);
		this.error = error;
	}
	
	public GlueException(ERROR error, String message, Throwable t)
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

	public ERROR getError()
	{
		return error;
	}
	
	public GlueException prependMessage(String prefix)
	{
		return new GlueException(
				this.error,
				prefix + "::" + super.getMessage(),
				this.getCause()
				);
	}
}
