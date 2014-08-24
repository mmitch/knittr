package de.cgarbs.knittr;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
	public static void main(String[] args) {

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

}
