package de.cgarbs.lib.glue;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.exception.GlueException;

abstract public class Binding
{
	protected DataAttribute attribute;
	protected JLabel jLabel;

	abstract public void syncToView();
	abstract public void syncToModel() throws DataException;
	abstract public void addToContainer(JComponent parent, int gridx, int gridy) throws GlueException;  

	public Binding(DataAttribute attribute)
	{
		this.attribute = attribute;
		this.jLabel = createJLabel(attribute.getAttributeName());
		createComponents();
	}
	
	abstract protected void createComponents();
	
	private JLabel createJLabel(String label)
	{
		return new JLabel(label);
	}
}
