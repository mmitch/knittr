package de.cgarbs.lib.glue.type;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

import de.cgarbs.lib.data.DataAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.glue.Binding;
import de.cgarbs.lib.i18n.Resource;

public class ImageBinding extends Binding
{
	protected JLabel jLabel;

	protected final static Border border = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	public ImageBinding(DataAttribute attribute, Resource resource, String label)
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
		// output only!
	}

	@Override
	protected JComponent createDataEntryComponent()
	{
		jLabel = new JLabel();
		jLabel.setBorder(border);
		return jLabel;
	}

	private void setViewValue(File value)
	{
		if (value == null)
		{
			jLabel.setIcon(null);
		}
		else
		{
			try
			{
				BufferedImage img = ImageIO.read(value);
				jLabel.setIcon(new ImageIcon(img));
			}
			catch (IOException e)
			{
				// FIXME empty try-catch-block!
			}
		}
	}

}
