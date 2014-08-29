package de.cgarbs.lib.glue.type;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.data.type.FloatAttribute;
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
		String s = ((FloatAttribute)attribute).getFormattedValue();
		if (s == null)
		{
			jTextField.setText("");
		}
		else
		{
			jTextField.setText(s);
		}
	}

	@Override
	public void syncToModel() throws DataException
	{
		attribute.setValue(jTextField.getText());
	}


}
