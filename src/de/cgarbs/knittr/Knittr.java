package de.cgarbs.knittr;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.cgarbs.knittr.data.Project;
import de.cgarbs.knittr.ui.MainWindow;
import de.cgarbs.lib.exception.DataException;

public class Knittr
{

	static Project p;
	static File f;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{

		setLookAndFeel(0);

		try
		{
			p = new Project();

			if (args.length == 1)
			{
				System.out.println("Using commandline argument 1 as file name");

				// convert to absolute path
				f = new File(args[0]).getAbsoluteFile();

				if (! f.exists())
				{
					System.err.println("File does not exist!");
				}
				else if (! f.canRead())
				{
					System.err.println("File is not readable!");
				}
				else
				{
					try
					{
						p.readFromFile(f);
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
					catch (ClassNotFoundException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		catch (DataException e)
		{
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow(p, f);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static String[] lookAndFeels = new String[]
			{
		"com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel",
		"com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
		"com.sun.java.swing.plaf.gtk.GTKLookAndFeel",
		"com.sun.java.swing.plaf.motif.MotifLookAndFeel"
			};

	/**
	 * UIManager.getSystemLookAndFeelClassName() does not return the GTK on my Ubuntu
	 * so fiddle around manually :-(
	 * @param i try index
	 */
	private static void setLookAndFeel(int i)
	{
		if (i < lookAndFeels.length)
		{
			try
			{
				UIManager.setLookAndFeel(lookAndFeels[i]);
			}
			catch (Exception e)
			{
				setLookAndFeel(i+1);
			}
		}
	}

}
