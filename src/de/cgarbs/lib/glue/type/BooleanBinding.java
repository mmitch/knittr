package de.cgarbs.lib.glue.type;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.glue.Binding;
import de.cgarbs.lib.i18n.Resource;

public class BooleanBinding extends Binding
{
	protected JCheckBox jCheckBox;

	public BooleanBinding(DataAttribute attribute, Resource resource, String label)
	{
		super(attribute, resource, label);
	}

	@Override
	public void syncToModel() throws DataException
	{
		attribute.setValue(jCheckBox.isSelected());
	}

	@Override
	protected JComponent createDataEntryComponent()
	{
		jCheckBox = new JCheckBox();
		return jCheckBox;
	}

	@Override
	public void setViewValue(Object value)
	{
		super.setViewValue(value);
		jCheckBox.setSelected((Boolean)attribute.getValue());
	}
}
