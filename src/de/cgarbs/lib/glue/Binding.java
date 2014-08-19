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

	public void addToContainer(JComponent parent, int gridx, int gridy) throws GlueException
	{
		if (! (parent.getLayout() instanceof GridBagLayout))
		{
			throw new GlueException(
					GlueException.ERROR.LAYOUT_MANAGER_WRONG,
					parent.getLayout().getClass().getName()
					);
		}
		
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = gridx;
		gbc1.gridy = gridy;
		
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = gridx + 1;
		gbc2.gridy = gridy;
		
		parent.add(jLabel, gbc1);
		parent.add(jData,  gbc2);
	}

	public Binding(DataAttribute attribute)
	{
		this.attribute = attribute;
		this.jLabel = createJLabel(attribute.getAttributeName());
		this.jData  = createDataEntryComponent();
	}
	
	abstract protected JComponent createDataEntryComponent();

	
	private JLabel createJLabel(String label)
	{
		return new JLabel(label);
	}
}
