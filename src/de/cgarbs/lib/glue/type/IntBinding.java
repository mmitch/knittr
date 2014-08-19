package de.cgarbs.lib.glue.type;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.glue.TextFieldBinding;

public class IntBinding extends TextFieldBinding
{
	public IntBinding(DataAttribute attribute)
	{
		super(attribute);
	}

	@Override
	public void syncToView()
	{
		Integer value = (Integer)attribute.getValue();
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
