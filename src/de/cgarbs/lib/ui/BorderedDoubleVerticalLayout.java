package de.cgarbs.lib.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import de.cgarbs.lib.exception.GlueException;

public class BorderedDoubleVerticalLayout extends BorderedVerticalLayout
{
	// Builder pattern start
	public static class Builder extends BorderedVerticalLayout.Builder
	{

		@Override
		public JComponent build() throws GlueException
		{
			JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;
			gbc.weighty = 1;

			for (Group group : (List<Group>) groups) // FIXME why cast here?!
			{
				JPanel groupPanel = new JPanel();
				groupPanel.setLayout(new GridBagLayout());
				Border newBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), group.getTitle());
				groupPanel.setBorder(newBorder);
				int line = 0;
				int col  = 0;

				for (Element element : group.getElements())
				{
					element.addToComponent(groupPanel, col, line);

					col+=2;
					if (col > 2)
					{
						col = 0;
						line++;
					}
				}

				panel.add(groupPanel, gbc);
				gbc.gridy++;
			}

			return panel;
		}
	}

	public static Builder builder()
	{
		return new Builder();
	}

	protected BorderedDoubleVerticalLayout(Builder builder)
	{
		super(builder);
	}

	// Builder pattern end

}
