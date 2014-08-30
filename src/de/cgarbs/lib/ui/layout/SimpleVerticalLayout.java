package de.cgarbs.lib.ui.layout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.cgarbs.lib.exception.GlueException;
import de.cgarbs.lib.ui.AutoLayout;
import de.cgarbs.lib.ui.Element;
import de.cgarbs.lib.ui.Group;

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
			gbc_title.anchor = GridBagConstraints.CENTER;
			gbc_title.fill = GridBagConstraints.HORIZONTAL;
			gbc_title.insets = new Insets(0, 32, 0, 0);

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

			return wrapInScrollPane(panel);
		}
	}

	public static Builder builder()
	{
		return new Builder();
	}

	protected SimpleVerticalLayout(Builder builder)
	{
		super(builder);
	}

	// Builder pattern end

}
