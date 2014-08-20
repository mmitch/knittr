package de.cgarbs.lib.glue.type;

import java.io.File;

import javax.swing.JComponent;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.glue.TextFieldBinding;

public class FileBinding extends TextFieldBinding
{
	public FileBinding(DataAttribute attribute)
	{
		super(attribute);
	}

	@Override
	public void syncToView()
	{
		File value = (File)attribute.getValue();
		if (value == null)
		{
			jTextField.setText("");
		}
		else
		{
			jTextField.setText(value.getAbsolutePath());
		}
	}

	@Override
	public void syncToModel() throws DataException
	{
		String value = jTextField.getText();
		if (value == null)
		{
			attribute.setValue(null);
		}
		else
		{
			attribute.setValue(new File(value));
		}
	}

	@Override
	protected JComponent createDataEntryComponent()
	{
		// FIXME: add FileChooser button
		// FIXME: add FileChooser handling
		super.createDataEntryComponent();
		jTextField.setEditable(false);
		return jTextField;
	}


}
