package de.cgarbs.lib.ui;

import javax.swing.JComponent;

import de.cgarbs.lib.glue.Binding;

public class BindingElement extends Element
{
	protected Binding binding;

	public BindingElement(Binding binding)
	{
		this.binding = binding;
	}

	@Override
	public void addToComponent(JComponent component, int x, int y)
	{
		component.add(binding.getJLabel(), position(x,   y, 1, 1));
		component.add(binding.getJData(),  position(x+1, y, 1, 1));
	}
}
