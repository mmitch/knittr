package de.cgarbs.lib.exception;

import java.util.ArrayList;
import java.util.List;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.data.DataModel;

public class ValidationErrorList extends Exception
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private List<ValidationError> validationErrors = new ArrayList<ValidationError>();
	private DataModel model;

	public ValidationErrorList(DataModel model)
	{
		this.model = model;
	}

	public void addValidationError(ValidationError e)
	{
		validationErrors.add(e);
	}

	public List<ValidationError> getValidationErrors()
	{
		return validationErrors;
	}

	public String getMessage()
	{
		return validationErrors.size() + " validation errors from model " + model.getModelName();
	}

	public String getErrorList()
	{
		StringBuilder sb = new StringBuilder();

		for (ValidationError e: validationErrors)
		{
			sb.append(e.getMessage()).append('\n');
		}

		return sb.toString();
	}

	public void addValidationError(DataAttribute attribute, DataException e)
	{
		addValidationError(new ValidationError(attribute, e));
	}
}
