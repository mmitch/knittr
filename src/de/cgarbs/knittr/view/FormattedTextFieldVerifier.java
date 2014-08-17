package de.cgarbs.knittr.view;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.UIManager;

class FormattedTextFieldVerifier extends InputVerifier
{
	@Override
	public boolean verify(JComponent input)
	{
		if (input instanceof JFormattedTextField)
		{
			JFormattedTextField ftf = (JFormattedTextField) input;
			JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
			if (formatter != null)
			{
				String text = ftf.getText();
				try
				{
					formatter.stringToValue(text);
					ftf.setBackground(getColorTextFieldOK());
					return true;
				}
				catch (ParseException pe)
				{
					ftf.setBackground(getColorTextFieldERROR());
					pe.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	public boolean shouldYieldFocus(JComponent input)
	{
		return verify(input);
	}

	/**
	 * get the default color for a textfield
	 * 
	 * @return the color
	 */
	Color getColorTextFieldOK()
	{
		return UIManager.getColor("TextField.background");
	}

	/**
	 * get the error color for a textfield
	 * 
	 * @return the color
	 */
	Color getColorTextFieldERROR()
	{
		return Color.RED;
	}
}
