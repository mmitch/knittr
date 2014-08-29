package de.cgarbs.lib.glue.type;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.data.type.FileAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.glue.TextFieldBinding;
import de.cgarbs.lib.i18n.Resource;

public class FileBinding extends TextFieldBinding
{
	protected JButton jButton;

	public FileBinding(DataAttribute attribute, Resource resource, String label)
	{
		super(attribute, resource, label);
	}

	@Override
	public void syncToView()
	{
		setViewValue((File)attribute.getValue());
	}

	@Override
	public void syncToModel() throws DataException
	{
		attribute.setValue(getViewValue());
	}

	@Override
	protected JComponent createDataEntryComponent()
	{
		super.createDataEntryComponent();
		jTextField.setEditable(false);

		jButton = new JButton("...");

		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser fc = new JFileChooser();
				boolean first = true;
				for (FileFilter fileFilter: ((FileAttribute)attribute).getFileFilters())
				{
					if (first)
					{
						fc.setFileFilter(fileFilter);
						first = false;
					}
					else
					{
						fc.addChoosableFileFilter(fileFilter);
					}
				}
				fc.setSelectedFile(getViewValue());
				fc.setDialogTitle("choose "+txtLabel); // FIXME i18n
				if (fc.showDialog(
						jButton,
						"choose file") // FIXME i18n
						== JFileChooser.APPROVE_OPTION)
				{
					setViewValue(fc.getSelectedFile());
				}
			}
		});

		JPanel combiPanel = new JPanel();
		combiPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.weighty = 1;
		combiPanel.add(jTextField, gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.2;
		combiPanel.add(jButton);
		return combiPanel;
	}

	private void setViewValue(File value)
	{
		if (value == null)
		{
			jTextField.setText("");
		}
		else
		{
			jTextField.setText(value.getAbsolutePath());
		}
	}

	private File getViewValue()
	{
		String value = jTextField.getText();
		if (value == null || value.length() == 0)
		{
			return null;
		}
		else
		{
			return new File(value);
		}
	}


}
