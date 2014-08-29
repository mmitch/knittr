package de.cgarbs.lib.ui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComponent;

public abstract class Element
{

	protected final static Insets labelInsets = new Insets(6, 2, 2, 2);
	protected final static Insets valueInsets = new Insets(0, 2, 0, 2);

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
		gbc.insets = labelInsets;
		gbc.weightx = 0;
		gbc.weighty = 0;
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
		gbc.insets = valueInsets;
		gbc.weightx = 1;
		gbc.weighty = 0;
		return gbc;
	}

}