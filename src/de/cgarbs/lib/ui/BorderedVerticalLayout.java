package de.cgarbs.lib.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import de.cgarbs.lib.exception.GlueException;
import de.cgarbs.lib.glue.Binding;

public class BorderedVerticalLayout extends AutoLayout
{
	// Builder pattern start
	public static class Builder extends AutoLayout.Builder<Builder>
	{
		
		@Override
		public JComponent build() throws GlueException
		{
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout());
			// FIXME fill vertical missing
	
			for (Group group : (List<Group>) groups) // FIXME why cast here?!
			{
				JPanel groupPanel = new JPanel();
				groupPanel.setLayout(new GridBagLayout());
				Border newBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), group.getTitle());
				groupPanel.setBorder(newBorder);
				int line = 0;
				
				for (Binding binding : group.getBindings())
				{
					groupPanel.add(binding.getJLabel(), position(0, line, 1, 1));
					groupPanel.add(binding.getJData(),  position(1, line, 1, 1));
					line++;
				}
				
				panel.add(groupPanel);
			}
	
			return panel;
		}
		
		private GridBagConstraints position(int x, int y, int w, int h)
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

	private BorderedVerticalLayout(Builder builder)
	{
		super(builder);
	}

	// Builder pattern end

}
