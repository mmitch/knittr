package de.cgarbs.lib.ui;

import java.awt.Container;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

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

		protected Container wrapInScrollPane(JComponent component)
		{
			final JScrollPane scrollPane = new JScrollPane(component);

			// move scroll pane to top left needs to be async :-/
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					JScrollBar v = scrollPane.getVerticalScrollBar();
					JScrollBar h = scrollPane.getHorizontalScrollBar();
					v.setValue(v.getMinimum());
					h.setValue(h.getMinimum());
				}
			});

			return scrollPane;
		}

		abstract public Container build() throws GlueException;
	}

	protected AutoLayout(Builder<?> builder)
	{
	}
	// Builder pattern end

	/**
	 * ensure that a component is visible within an AutoLayout
	 * This method will work upwards through all parent objects and
	 * scroll as needed.
	 *
	 * @param component the component to show
	 */
	public static void showComponent(JComponent component)
	{
		Rectangle r     = component.getBounds();
		Container c     = component.getParent();
		Container cLast = component;

		while (c != null)
		{
			if (c instanceof JTabbedPane)
			{
				JTabbedPane tp = (JTabbedPane) c;
				int tabCounts = tp.getTabCount();
				for (int tab = 0; tab < tabCounts; tab++)
				{
					if (tp.getComponent(tab) == cLast)
					{
						tp.setSelectedIndex(tab);
						break;
					}
				}
			}
			else if (c instanceof JComponent)
			{
				((JComponent) c).scrollRectToVisible(r);
			}

			cLast = c;
			r = c.getBounds();
			c = c.getParent();
		}
	}
}
