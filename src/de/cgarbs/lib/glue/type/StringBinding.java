package de.cgarbs.lib.glue.type;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.glue.TextFieldBinding;

public class StringBinding extends TextFieldBinding
{
	public StringBinding(DataAttribute attribute)
	{
		super(attribute);
	}

	@Override
	public void syncToView()
	{
		jTextField.setText((String)attribute.getValue());
	}

	@Override
	public void syncToModel() throws DataException
	{
		attribute.setValue(jTextField.getText());
	}


}