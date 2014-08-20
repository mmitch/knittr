package de.cgarbs.lib.ui;

import java.util.ArrayList;
import java.util.List;

import de.cgarbs.lib.glue.Binding;

public class Group
{
	private List<Binding> bindings = new ArrayList<Binding>();
	private String title;

	public Group(String title)
	{
		this.title = title;
	}

	public void addBinding(Binding binding)
	{
		bindings.add(binding);
	}

	public String getTitle()
	{
		return this.title;
	}

	public List<Binding> getBindings()
	{
		return bindings;
	}
}
