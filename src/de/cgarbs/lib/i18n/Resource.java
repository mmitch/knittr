package de.cgarbs.lib.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Resource
{
	private ResourceBundle rb;
	private Pattern pattern = Pattern.compile("(\\$?)\\$(\\d*+)");

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

		StringBuffer sb = new StringBuffer();
		Matcher matcher = pattern.matcher(text);
		while (matcher.find())
		{
			String repl;
			if (matcher.group(1).isEmpty() && matcher.group(2).isEmpty())
			{
				// single "$" -> don't do anything, replace by $
				repl = "\\$"; // quote this!
			}
			else if (matcher.group(1).isEmpty())
			{
				// replacement of variable $n
				int i = Integer.valueOf(matcher.group(2));
				if (i < values.length)
				{
					repl = values[i];
				}
				else
				{
					repl = "{PARAMETER "+i+" NOT SET}";
					// FIXME warn via exception?
				}
			}
			else
			{
				// duplicate $$ -> escaped $
				// change $$ to $, don't change anything else
				repl = "\\$$2"; // quoted $ (\$) plus group 2 ($2)
			}
			matcher.appendReplacement(sb, repl);
		}
		matcher.appendTail(sb);

		return sb.toString();
	}
}
