package de.cgarbs.lib.ui;

import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import de.cgarbs.lib.exception.GlueException;

public class BorderedVerticalLayout extends AutoLayout
{
	// Builder pattern start
	public static class Builder extends AutoLayout.Builder<Builder>
	{

		@Override
		public JComponent build() throws GlueException
		{
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

			for (Group group : (List<Group>) groups) // FIXME why cast here?!
			{
				JPanel groupPanel = new JPanel();
				groupPanel.setLayout(new GridBagLayout());
				Border newBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), group.getTitle());
				groupPanel.setBorder(newBorder);
				int line = 0;

				for (Element element : group.getElements())
				{
					element.addToComponent(groupPanel, 0, line);
					line++;
				}

				panel.add(groupPanel);
			}

			return panel;
		}
	}

	public static Builder builder()
	{
		return new Builder();
	}

	private BorderedVerticalLayout(Builder builder)
	{
		super(builder);
	}

	// Builder pattern end

}
