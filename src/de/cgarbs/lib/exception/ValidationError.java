package de.cgarbs.lib.exception;

import java.util.HashMap;
import java.util.Map;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.i18n.Resource;

public class ValidationError extends LocalizedException
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// FIXME: use enum + HashMap with String keys like this?
	// alternative: use subclass for "free" ValidationError texts
	// and only use public string constants here?

	private static final Map<ERROR, String> MESSAGEKEY = new HashMap<ERROR, String>();

	public enum ERROR
	{
		STRING_TOO_SHORT,
		STRING_TOO_LONG,
		FILE_NOT_EXISTING,
		FILE_NOT_READABLE,
		FILE_NOT_WRITABLE,
		NUMBER_TOO_SMALL,
		NUMBER_TOO_LARGE,
		NULL_NOT_ALLOWED
	}

	static
	{
		MESSAGEKEY.put(ERROR.STRING_TOO_SHORT,  "STRING_TOO_SHORT");
		MESSAGEKEY.put(ERROR.STRING_TOO_LONG,   "STRING_TOO_LONG");
		MESSAGEKEY.put(ERROR.FILE_NOT_EXISTING, "FILE_NOT_EXISTING");
		MESSAGEKEY.put(ERROR.FILE_NOT_READABLE, "FILE_NOT_READABLE");
		MESSAGEKEY.put(ERROR.FILE_NOT_WRITABLE, "FILE_NOT_WRITABLE");
		MESSAGEKEY.put(ERROR.NUMBER_TOO_SMALL,  "NUMBER_TOO_SMALL");
		MESSAGEKEY.put(ERROR.NUMBER_TOO_LARGE,  "NUMBER_TOO_LARGE");
		MESSAGEKEY.put(ERROR.NULL_NOT_ALLOWED,  "NULL_NOT_ALLOWED");
	}

	private static Resource R = new Resource("de.cgarbs.lib.i18n.resource.ValidationError");

	private DataAttribute attribute;

	public ValidationError(DataAttribute attribute, String message, ERROR error, String... params)
	{
		super(message, R._(MESSAGEKEY.get(error), params));
		this.attribute = attribute;
	}

	public ValidationError(DataAttribute attribute, String message, String localizedMessage)
	{
		super(message, localizedMessage);
		this.attribute = attribute;
	}

	public ValidationError(DataAttribute attribute, DataException e)
	{
		this(attribute, e.getMessageOnly(), e.getLocalizedMessage());
	}

	public String getMessage()
	{
		return attribute.getAttributeName() + ": " + super.getMessage();
	}
}
