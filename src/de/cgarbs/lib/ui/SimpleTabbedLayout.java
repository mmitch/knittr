package de.cgarbs.lib.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.Box;
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

				Component remainder = Box.createGlue();
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;
				gbc.gridy = line;
				gbc.gridwidth = 2;
				gbc.gridheight = 1;
				gbc.weightx = 1;
				gbc.weighty = 1;
				gbc.fill = GridBagConstraints.VERTICAL;
				panel.add(remainder, gbc);

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
