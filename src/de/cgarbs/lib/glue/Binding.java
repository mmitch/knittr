package de.cgarbs.lib.glue;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.i18n.Resource;

abstract public class Binding
{
	protected DataAttribute attribute;
	protected JLabel     jLabel;
	protected JComponent jData;
	protected String     txtLabel;

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

	public Binding(DataAttribute attribute, Resource resource)
	{
		this.attribute = attribute;
		// FIXME: the resource file already is DataModel specific - the attributeName is again... that's duplicated!
		this.txtLabel = resource._("LBL_"+attribute.getAttributeName());
		this.jLabel = createJLabel(txtLabel);
		this.jData  = createDataEntryComponent();
	}

	abstract protected JComponent createDataEntryComponent(); // FIXME NAME cdec <-> JData


	private JLabel createJLabel(String label)
	{
		return new JLabel(label);
	}

//	public DataAttribute getAttribute() // FIXME: why is the field attribute visible, it's protected?!
//	{
//		return attribute;
//	}
}
