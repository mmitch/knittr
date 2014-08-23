package de.cgarbs.lib.glue;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.exception.GlueException;

abstract public class Binding
{
	protected DataAttribute attribute;
	protected JLabel     jLabel;
	protected JComponent jData;

	abstract public void syncToView();
	abstract public void syncToModel() throws DataException;

	public JComponent getJLabel()
	{
		return jLabel;
	}

	public JComponent getJData()
	{
		return jData;
	}

	public Binding(DataAttribute attribute)
	{
		this.attribute = attribute;
		this.jLabel = createJLabel(attribute.getAttributeName());
		this.jData  = createDataEntryComponent();
	}

	abstract protected JComponent createDataEntryComponent(); // FIXME NAME cdec <-> JData


	private JLabel createJLabel(String label)
	{
		return new JLabel(label);
	}
}
