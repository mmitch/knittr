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
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.data.type.FileAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.glue.Binding;
import de.cgarbs.lib.i18n.Resource;

public class FileBinding extends Binding
{
	protected JButton jButton;
	protected JTextField jTextField;
	protected File file;

	public FileBinding(DataAttribute attribute, Resource resource, String label)
	{
		super(attribute, resource, label);
	}

	@Override
	public void syncToModel() throws DataException
	{
		attribute.setValue(file);
	}

	@Override
	protected JComponent createDataEntryComponent()
	{
		jTextField = new JTextField();
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

	public void setViewValue(Object value)
	{
		super.setViewValue(value);

		this.file = (File) value;

		if (file == null)
		{
			jTextField.setText("");
		}
		else
		{
			String workingDir = System.getProperty("user.dir"); // FIXME keep this static from program startup?
			if (! workingDir.endsWith(File.separator))
			{
				workingDir += File.separator;
			}
			String s = file.getAbsolutePath();
			if (s.startsWith(workingDir))
			{
				s = s.substring(workingDir.length());
			}
			jTextField.setText(s);
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
