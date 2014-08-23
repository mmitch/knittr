package de.cgarbs.lib.ui;

import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.cgarbs.lib.exception.GlueException;

public class SimpleTabbedLayout extends AutoLayout
{
	// Builder pattern start
	public static class Builder extends AutoLayout.Builder<Builder>
	{

		@Override
		public JComponent build() throws GlueException
		{
			JTabbedPane component = new JTabbedPane();

			for (Group group : (List<Group>) groups) // FIXME why cast here?!
			{
				JPanel panel = new JPanel();
				panel.setLayout(new GridBagLayout());
				int line = 0;

				for (Element element: group.getElements())
				{
					element.addToComponent(panel,  0,  line);
					line++;
				}

				component.add(group.getTitle(), panel);
			}

			return component;
		}
	}

	public static Builder builder()
	{
		return new Builder();
	}

	private SimpleTabbedLayout(Builder builder)
	{
		super(builder);
	}

	// Builder pattern end

}
