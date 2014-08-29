package de.cgarbs.knittr;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.UIManager;

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
	public static void main(String[] arguments)
	{
		Deque<String> args = new ArrayDeque<String>();
		args.addAll(Arrays.asList(arguments));

		// set style if given or fall back to "try everything in order"
		if (!args.isEmpty() && args.getFirst().startsWith("--style="))
		{
			String style = args.removeFirst().substring(8);
			if (! setLookAndFeel(style))
			{
				tryAllLookAndFeels();
			}
		}
		else
		{
			tryAllLookAndFeels();
		}

		try
		{
			p = new Project();

			if (!args.isEmpty())
			{
				String filename = args.removeFirst();
				System.out.println("Using commandline argument `"+filename+"' as file name");

				// convert to absolute path
				f = new File(filename).getAbsoluteFile();

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

	private static Map<String,String> lookAndFeels = new LinkedHashMap<String,String>();
	static {
		lookAndFeels.put("Nimbus", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		lookAndFeels.put("Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		lookAndFeels.put("GTK", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		lookAndFeels.put("Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		lookAndFeels.put("Metal", "javax.swing.plaf.metal.MetalLookAndFeel");
	};

	/**
	 * UIManager.getSystemLookAndFeelClassName() does not return the GTK on my Ubuntu
	 * so fiddle around manually :-(
	 */
	private static void tryAllLookAndFeels()
	{
		for (String key: lookAndFeels.keySet())
		{
			if (setLookAndFeel(key))
			{
				return;
			}
		}
	}


	private static boolean setLookAndFeel(String key)
	{
		try
		{
			UIManager.setLookAndFeel(lookAndFeels.get(key));
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}

}
