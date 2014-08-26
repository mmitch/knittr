package de.cgarbs.lib.glue;

import java.util.ArrayList;
import java.util.List;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.data.DataModel;
import de.cgarbs.lib.data.type.ColorAttribute;
import de.cgarbs.lib.data.type.FileAttribute;
import de.cgarbs.lib.data.type.FloatAttribute;
import de.cgarbs.lib.data.type.IntAttribute;
import de.cgarbs.lib.data.type.StringAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.exception.GlueException;
import de.cgarbs.lib.exception.ValidationError;
import de.cgarbs.lib.exception.ValidationErrorList;
import de.cgarbs.lib.glue.type.ColorBinding;
import de.cgarbs.lib.glue.type.FileBinding;
import de.cgarbs.lib.glue.type.FloatBinding;
import de.cgarbs.lib.glue.type.IntBinding;
import de.cgarbs.lib.glue.type.StringBinding;
import de.cgarbs.lib.i18n.Resource;

public class Glue<T extends DataModel>
{
	List<Binding> bindings = new ArrayList<Binding>();
	T model;

	public Glue(T model)
	{
		this.model = model;
	}

	public Binding addBinding(String key) throws GlueException, DataException
	{
		// FIXME catch duplicate bindings (same key)
		Binding binding;
		DataAttribute attribute = model.getAttribute(key);
		Resource resource = model.getResource();
		if (attribute instanceof StringAttribute)
		{
			binding = new StringBinding(attribute, resource);
		}
		else if (attribute instanceof IntAttribute)
		{
			binding = new IntBinding(attribute, resource);
		}
		else if (attribute instanceof FloatAttribute)
		{
			binding = new FloatBinding(attribute, resource);
		}
		else if (attribute instanceof FileAttribute)
		{
			binding = new FileBinding(attribute, resource);
		}
		else if (attribute instanceof ColorAttribute)
		{
			binding = new ColorBinding(attribute, resource);
		}
		else
		{
			throw new GlueException(
					GlueException.ERROR.BINDING_NOT_IMPLEMENTED,
					"no binding exists for " + attribute.getClass()
					);
		}
		bindings.add(binding);
		return binding;
	}

	public void syncToView()
	{
		for (Binding binding: bindings)
		{
			binding.syncToView();
		}
	}

	public void syncToModel() throws DataException
	{
		for (Binding binding: bindings)
		{
			binding.syncToModel();
		}
	}

	// FIXME check if syncToModel()+getModel() should be combined

	public T getModel()
	{
		return model;
	}

	public void validate() throws ValidationErrorList
	{
		ValidationErrorList ex = new ValidationErrorList(getModel());
		for (Binding binding: bindings)
		{
			Object oldValue = binding.attribute.getValue();
			try
			{
				binding.syncToModel();
				binding.attribute.validate();
			}
			catch (ValidationError e)
			{
				ex.addValidationError(e);
			}
			catch (DataException e)
			{
				ex.addValidationError(binding.attribute, e);
			}
			finally
			{
				try
				{
					binding.attribute.setValue(oldValue);
				}
				catch (DataException e)
				{
					// well, if this fails, the value was broken from the start...
					// can't do anything about it
				}
			}
		}

		try
		{
			model.additionalValidation();
		}
		catch (ValidationError e)
		{
			ex.addValidationError(e);
		}

		if (! ex.getValidationErrors().isEmpty())
		{
			throw ex;
		}
	}
}
