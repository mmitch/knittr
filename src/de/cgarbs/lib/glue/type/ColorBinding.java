package de.cgarbs.lib.glue.type;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.glue.Binding;
import de.cgarbs.lib.i18n.Resource;

public class ColorBinding extends Binding
{
	protected JPanel jPanel;
	protected JButton jButton;

	public ColorBinding(DataAttribute attribute, Resource resource, String label)
	{
		super(attribute, resource, label);
	}

	@Override
	public void syncToView()
	{
		Color value = (Color)attribute.getValue();
		jPanel.setBackground(value);
	}

	@Override
	public void syncToModel() throws DataException
	{
		attribute.setValue(jPanel.getBackground());
	}

	@Override
	protected JComponent createDataEntryComponent()
	{
		jPanel = new JPanel();
		jButton = new JButton("...");

		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				Color newColor = JColorChooser.showDialog(
						jButton,
						"choose "+txtLabel, // FIXME i18n
						jPanel.getBackground());
				if (newColor != null)
				{
					setViewValue(newColor);
				}
			}
		});

		JPanel combiPanel = new JPanel();
		combiPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.weightx = 1;
		gbc.weighty = 1;
		combiPanel.add(jPanel, gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = null;
		gbc.weightx = 0.2;
		combiPanel.add(jButton);
		return combiPanel;
	}

	@Override
	public void setViewValue(Object value)
	{
		super.setViewValue(value);

		Color c = (Color) value;

		jPanel.setBackground(c);
	}
}
