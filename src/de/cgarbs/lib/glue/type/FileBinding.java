package de.cgarbs.lib.glue.type;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.glue.TextFieldBinding;

public class FileBinding extends TextFieldBinding
{
	protected JButton jButton;

	public FileBinding(DataAttribute attribute)
	{
		super(attribute);
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
				fc.setSelectedFile(getViewValue());
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
		combiPanel.add(jTextField);
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
