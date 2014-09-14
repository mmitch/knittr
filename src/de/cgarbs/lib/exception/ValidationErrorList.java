package de.cgarbs.lib.exception;

import java.util.ArrayList;
import java.util.List;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.data.DataModel;
import de.cgarbs.lib.glue.Binding;

public class ValidationErrorList extends Exception
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private List<String> validationErrors = new ArrayList<String>();

	public void addValidationError(String text)
	{
		validationErrors.add(text);
	}

	public void addValidationError(String text, ValidationError e)
	{
		addValidationError(text + ": " + e.getLocalizedMessage());
	}

	public void addValidationError(DataModel model, ValidationError e)
	{
		addValidationError(model.getModelName(), e);
	}

	public void addValidationError(DataAttribute attribute, ValidationError e)
	{
		addValidationError(attribute.getAttributeName(), e);
	}

	public void addValidationError(Binding binding, ValidationError e)
	{
		addValidationError(binding.getTxtLabel(), e);
	}

	public void addValidationError(Binding binding, DataException e)
	{
		addValidationError(binding.getTxtLabel(), new ValidationError(binding.getAttribute(), e));
	}

	public boolean isEmpty()
	{
		return validationErrors.isEmpty();
	}

	public String getMessage()
	{
		return validationErrors.size() + " validation errors";
	}

	public String getErrorList()
	{
		StringBuilder sb = new StringBuilder();

		for (String s: validationErrors)
		{
			sb.append(s).append('\n');
		}

		return sb.toString();
	}
}
