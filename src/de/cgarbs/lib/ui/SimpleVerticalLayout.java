package de.cgarbs.lib.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.cgarbs.lib.exception.GlueException;

public class SimpleVerticalLayout extends AutoLayout
{
	// Builder pattern start
	public static class Builder extends AutoLayout.Builder<Builder>
	{

		@Override
		public JComponent build() throws GlueException
		{
			JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());

			int line = 0;
			for (Group group : (List<Group>) groups) // FIXME why cast here?!
			{
				panel.add(new JLabel(group.getTitle()), position(0, line, 2, 1));
				line++;
				for (Element element: group.getElements())
				{
					element.addToComponent(panel, 0, line);
					line++;
				}
			}

			return new JScrollPane(panel);
		}

		private GridBagConstraints position(int x, int y, int w, int h)
		// FIXME move somewhere better, duplicate code with Elements!
		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = x;
			gbc.gridy = y;
			gbc.gridwidth = w;
			gbc.gridheight = h;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			return gbc;
		}
	}

	public static Builder builder()
	{
		return new Builder();
	}

	private SimpleVerticalLayout(Builder builder)
	{
		super(builder);
	}

	// Builder pattern end

}
