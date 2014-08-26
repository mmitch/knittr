package de.cgarbs.lib.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Resource
{
	private ResourceBundle rb;
	private Pattern pattern = Pattern.compile("[^$]\\$(\\d+)");

	public Resource(String baseName)
	{
		rb = ResourceBundle.getBundle(baseName);
	}

	public String _(String key, String... values)
	{
		if (key == null)
		{
			return null;
		}

		String text;

		try
		{
			text = rb.getString(key);
		}
		catch (MissingResourceException e)
		{
			return key;
			// FIXME warn via stderr?
		}

		Matcher matcher = pattern.matcher(text);
		while (matcher.find())
		{
			int i = Integer.valueOf(matcher.group(1));
			if (i < values.length)
			{
				text = matcher.replaceFirst(values[i]);
			}
			else
			{
				text = matcher.replaceFirst("{PARAMETER "+i+" NOT SET}");
				// FIXME warn via exception?
			}
			matcher = pattern.matcher(text);
		}

		return text;
	}
}
