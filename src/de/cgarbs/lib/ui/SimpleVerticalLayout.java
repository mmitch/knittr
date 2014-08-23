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

			GridBagConstraints gbc_title = new GridBagConstraints();
			gbc_title.gridx = 0;
			gbc_title.gridwidth = 2;
			gbc_title.gridheight = 1;
			gbc_title.weightx = 1;
			gbc_title.weighty = 1;
			gbc_title.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc_title.fill = GridBagConstraints.HORIZONTAL;

			int line = 0;
			for (Group group : (List<Group>) groups) // FIXME why cast here?!
			{
				gbc_title.gridy = line;
				panel.add(new JLabel(group.getTitle()), gbc_title);
				line++;
				for (Element element: group.getElements())
				{
					element.addToComponent(panel, 0, line);
					line++;
				}
			}

			JScrollPane scroller = new JScrollPane(panel);
			return scroller;
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
