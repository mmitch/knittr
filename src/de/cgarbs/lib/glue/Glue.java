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
import de.cgarbs.lib.glue.type.ImageBinding;
import de.cgarbs.lib.glue.type.IntBinding;
import de.cgarbs.lib.glue.type.ProgressBarBinding;
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
		return addBinding(key, (String)null);
	}

	public Binding addBinding(String key, String label) throws GlueException, DataException
	{
		// FIXME catch duplicate bindings (same key)
		DataAttribute attribute = model.getAttribute(key);
		if (attribute instanceof StringAttribute)
		{
			return addBinding(key, StringBinding.class, label);
		}
		else if (attribute instanceof IntAttribute)
		{
			return addBinding(key, IntBinding.class, label);
		}
		else if (attribute instanceof FloatAttribute)
		{
			return addBinding(key, FloatBinding.class, label);
		}
		else if (attribute instanceof FileAttribute)
		{
			return addBinding(key, FileBinding.class, label);
		}
		else if (attribute instanceof ColorAttribute)
		{
			return addBinding(key, ColorBinding.class, label);
		}
		else
		{
			throw new GlueException(
					GlueException.ERROR.BINDING_NOT_IMPLEMENTED,
					"no binding exists for " + attribute.getClass()
					);
		}
	}

	// FIXME restrict clazz to subtype of Binding?
	public Binding addBinding(String key, Class<?> clazz) throws GlueException, DataException
	{
		return addBinding(key, clazz, null);
	}

	// FIXME restrict clazz to subtype of Binding?
	public Binding addBinding(String key, Class<?> clazz, String label) throws GlueException, DataException
	{
		// FIXME catch duplicate bindings (same key)
		Binding binding;
		DataAttribute attribute = model.getAttribute(key);
		Resource resource = model.getResource();
		// FIXME catch incompatible attribute classes!
		if (StringBinding.class.equals(clazz))
		{
			binding = new StringBinding(attribute, resource, label);
		}
		else if (IntBinding.class.equals(clazz))
		{
			binding = new IntBinding(attribute, resource, label);
		}
		else if (FloatBinding.class.equals(clazz))
		{
			binding = new FloatBinding(attribute, resource, label);
		}
		else if (FileBinding.class.equals(clazz))
		{
			binding = new FileBinding(attribute, resource, label);
		}
		else if (ColorBinding.class.equals(clazz))
		{
			binding = new ColorBinding(attribute, resource, label);
		}
		else if (ImageBinding.class.equals(clazz))
		{
			binding = new ImageBinding(attribute, resource, label);
		}
		else if (ProgressBarBinding.class.equals(clazz))
		{
			binding = new ProgressBarBinding(attribute, resource, label);
		}
		else
		{
			throw new GlueException(
					GlueException.ERROR.BINDING_NOT_IMPLEMENTED,
					"no binding defined for " + clazz
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
