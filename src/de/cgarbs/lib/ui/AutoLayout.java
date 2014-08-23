package de.cgarbs.lib.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import de.cgarbs.lib.exception.GlueException;
import de.cgarbs.lib.glue.Binding;

abstract public class AutoLayout
{

	// Builder pattern start
	public abstract static class Builder<T extends Builder<?>>
	{
		protected List<Group> groups = new ArrayList<Group>();

		public Builder<T> addAttribute(Binding binding)
		{
			getCurrentGroup().addBinding(binding);
			return this;
		}

		public Builder<T> addComponent(JComponent component)
		{
			getCurrentGroup().addComponent(component);
			return this;
		}

		public Builder<T> startNextGroup(String label)
		{
			groups.add(new Group(label));
			return this;
		}

		protected final Group getCurrentGroup()
		{
			if (groups.isEmpty())
			{
				startNextGroup(null);
			}
			return groups.get(groups.size() - 1);
		}

		abstract public JComponent build() throws GlueException;
	}

	protected AutoLayout(Builder<?> builder)
	{
	}
	// Builder pattern end

}
