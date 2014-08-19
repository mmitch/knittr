package de.cgarbs.lib.glue;

import javax.swing.JComponent;
import javax.swing.JTextField;

import de.cgarbs.lib.data.DataAttribute;

public abstract class TextFieldBinding extends Binding
{

	protected JTextField jTextField;

	public TextFieldBinding(DataAttribute attribute)
	{
		super(attribute);
	}

	@Override
	protected JComponent createDataEntryComponent()
	{
		return jTextField = new JTextField();
	}

}