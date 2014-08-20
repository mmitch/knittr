package de.cgarbs.lib.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

import de.cgarbs.lib.exception.GlueException;
import de.cgarbs.lib.glue.Binding;

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
				
				for (Binding binding : group.getBindings())
				{
					panel.add(binding.getJLabel(), position(0, line, 1, 1));
					panel.add(binding.getJData(),  position(1, line, 1, 1));
					line++;
				}
				
				component.add(group.getTitle(), panel);
			}
	
			return component;
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

	private SimpleTabbedLayout(Builder builder)
	{
		super(builder);
	}

	// Builder pattern end

}
