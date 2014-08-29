package de.cgarbs.lib.glue.type;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.glue.TextFieldBinding;
import de.cgarbs.lib.i18n.Resource;

public class StringBinding extends TextFieldBinding
{
	public StringBinding(DataAttribute attribute, Resource resource, String label)
	{
		super(attribute, resource, label);
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
