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

	protected GridBagConstraints pos_label(int x, int y, int w, int h)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.2;
		gbc.weighty = 1;
		return gbc;
	}

	protected GridBagConstraints pos_value(int x, int y, int w, int h)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.weighty = 1;
		return gbc;
	}

}