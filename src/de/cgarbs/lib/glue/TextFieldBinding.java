package de.cgarbs.lib.glue;

import javax.swing.JComponent;
import javax.swing.JTextField;

import de.cgarbs.lib.data.DataAttribute;
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

}