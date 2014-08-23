package de.cgarbs.lib.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import de.cgarbs.lib.glue.Binding;

public class Group
{
	private List<Element> elements = new ArrayList<Element>();
	private String title;

	public Group(String title)
	{
		this.title = title;
	}

	public void addBinding(Binding binding)
	{
		elements.add(new BindingElement(binding));
	}

	public void addComponent(JComponent component)
	{
		elements.add(new JComponentElement(component));
	}

	public String getTitle()
	{
		return this.title;
	}

	public List<Element> getElements()
	{
		return elements;
	}
}
