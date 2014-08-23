package de.cgarbs.lib.ui;

import javax.swing.JComponent;

public class JComponentElement extends Element
{
	protected JComponent component;

	public JComponentElement(JComponent component)
	{
		this.component = component;
	}

	@Override
	public void addToComponent(JComponent component, int x, int y)
	{
		component.add(this.component, position(x, y, 2, 1));
	}
}
