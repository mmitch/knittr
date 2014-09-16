package de.cgarbs.lib.glue.type;

import javax.swing.JComponent;
import javax.swing.JProgressBar;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.data.type.IntAttribute;
import de.cgarbs.lib.glue.Binding;
import de.cgarbs.lib.i18n.Resource;

public class ProgressBarBinding extends Binding
{
	protected JProgressBar jProgressBar;

//	protected final static Border border = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	public ProgressBarBinding(DataAttribute attribute, Resource resource, String label)
	{
		super(attribute, resource, label);
	}

	@Override
	public Object getViewValue()
	{
		return jProgressBar.getValue();
	}

	@Override
	protected JComponent createDataEntryComponent()
	{
		IntAttribute i = (IntAttribute)attribute;
		jProgressBar = new JProgressBar();
		jProgressBar.setMinimum(i.getMinValue().intValue());
		jProgressBar.setMaximum(i.getMaxValue().intValue());
		return jProgressBar;
	}

	@Override
	public void setViewValue(Object value)
	{
		super.setViewValue(value);

		Integer i = (Integer) value;

		if (i != null)
		{
			jProgressBar.setValue(i.intValue());
		}
	}
}
