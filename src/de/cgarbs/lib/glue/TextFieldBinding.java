package de.cgarbs.lib.glue;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JTextField;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.GlueException;

public abstract class TextFieldBinding extends Binding
{

	protected JTextField jTextField;

	public TextFieldBinding(DataAttribute attribute)
	{
		super(attribute);
	}

	@Override
	public void addToContainer(JComponent parent, int gridx, int gridy)
			throws GlueException
	{
		if (! (parent.getLayout() instanceof GridBagLayout))
		{
			throw new GlueException(
					GlueException.ERROR.BINDING_NOT_IMPLEMENTED,
					parent.getLayout().getClass().getName()
					);
		}
		
		GridBagLayout l = (GridBagLayout) parent.getLayout();
		
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = gridx;
		gbc1.gridy = gridy;
		
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = gridx + 1;
		gbc2.gridy = gridy;
		
		l.setConstraints(jLabel, 	 gbc1);
		l.setConstraints(jTextField, gbc2);
	}

	@Override
	protected void createComponents()
	{
		jTextField = new JTextField();
	}

}