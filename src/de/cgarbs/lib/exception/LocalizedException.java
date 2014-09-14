package de.cgarbs.lib.exception;

public abstract class LocalizedException extends Exception
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected String localizedMessage = null;

	public LocalizedException(String message)
	{
		super(message);
	}

	public LocalizedException(String message, String localizedMessage)
	{
		this(message);
		this.localizedMessage = localizedMessage;
	}

	public LocalizedException(String message, Throwable t)
	{
		super(message, t);
	}

	public LocalizedException(String message, String localizedMessage, Throwable t)
	{
		this(message, t);
		this.localizedMessage = localizedMessage;
	}

	public String getLocalizedMessage()
	{
		return localizedMessage;
	}
}
