package de.cgarbs.lib.glue;

import javax.swing.JComponent;
import javax.swing.JTextField;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.i18n.Resource;

public abstract class TextFieldBinding extends Binding
{

	protected JTextField jTextField;

	public TextFieldBinding(DataAttribute attribute, Resource resource, String label)
	{
		super(attribute, resource, label);
	}

	@Override
	protected JComponent createDataEntryComponent()
	{
		return jTextField = new JTextField();
	}

	@Override
	public void setViewValue(Object value)
	{
		super.setViewValue(value);

		String s = (String) value;

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