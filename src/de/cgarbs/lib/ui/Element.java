package de.cgarbs.lib.ui;

import java.awt.GridBagConstraints;

import javax.swing.JComponent;

public abstract class Element
{

	public Element()
	{
		super();
	}
	
	public abstract void addToComponent(JComponent component, int x, int y);

	protected GridBagConstraints position(int x, int y, int w,
			int h)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		return gbc;
	}

}