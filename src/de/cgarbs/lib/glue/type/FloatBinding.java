package de.cgarbs.lib.glue.type;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.glue.TextFieldBinding;
import de.cgarbs.lib.i18n.Resource;

public class FloatBinding extends TextFieldBinding
{
	public FloatBinding(DataAttribute attribute, Resource resource)
	{
		super(attribute, resource);
	}

	@Override
	public void syncToView()
	{
		Float value = (Float)attribute.getValue();
		if (value == null)
		{
			jTextField.setText("");
		}
		else
		{
			jTextField.setText(value.toString());
		}
	}

	@Override
	public void syncToModel() throws DataException
	{
		attribute.setValue(jTextField.getText());
	}


}
