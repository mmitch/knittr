package de.cgarbs.lib.glue.type;

import javax.swing.JComponent;
import javax.swing.JProgressBar;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.data.type.IntAttribute;
import de.cgarbs.lib.exception.DataException;
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
	public void syncToView()
	{
		setViewValue((Integer)attribute.getValue());
	}

	@Override
	public void syncToModel() throws DataException
	{
		// output only!
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

	private void setViewValue(Integer value)
	{
		if (value != null)
		{
			jProgressBar.setValue(value.intValue());
		}
	}
}
