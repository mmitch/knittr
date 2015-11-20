/*
 * Copyright 2015 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.knittr.exception;

import java.util.HashMap;
import java.util.Map;

import de.cgarbs.lib.exception.LocalizedException;
import de.cgarbs.lib.i18n.Resource;

public class RenderException extends LocalizedException
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Map<ERROR, String> ERRORTEXT = new HashMap<ERROR, String>();
	private static Resource R = new Resource("de.cgarbs.knittr.resource.RenderException");

	public enum ERROR
	{
		RENDER,
		IO
	}

	static
	{
		ERRORTEXT.put(ERROR.RENDER, "RENDER_EXCEPTION.RENDER");
		ERRORTEXT.put(ERROR.IO,     "RENDER_EXCEPTION.IO");
	};

	public RenderException(ERROR error, Throwable t)
	{
		super(
				ERRORTEXT.get(error) + t.getMessage(),
				R._(ERRORTEXT.get(error), t.getLocalizedMessage()),
				t);
	}
}
