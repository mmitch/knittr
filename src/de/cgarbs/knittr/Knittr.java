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
import de.cgarbs.lib.ui.AutoLayout.Builder;
import de.cgarbs.lib.ui.layout.BorderedDoubleVerticalLayout;
import de.cgarbs.lib.ui.layout.BorderedVerticalLayout;
import de.cgarbs.lib.ui.layout.DualColumnTabbedLayout;
import de.cgarbs.lib.ui.layout.DualColumnVerticalLayout;
import de.cgarbs.lib.ui.layout.SimpleTabbedLayout;
import de.cgarbs.lib.ui.layout.SimpleVerticalLayout;

public class Knittr
{

	private static Map<String,Builder<?>> layoutBuilders = new LinkedHashMap<String,Builder<?>>();
	static {
		layoutBuilders.put("SimpleTabbedLayout", SimpleTabbedLayout.builder());
		layoutBuilders.put("BorderedVerticalLayout", BorderedVerticalLayout.builder());
		layoutBuilders.put("SimpleVerticalLayout", SimpleVerticalLayout.builder());
		layoutBuilders.put("DualColumnTabbedLayout", DualColumnTabbedLayout.builder());
		layoutBuilders.put("DualColumnVerticalLayout", DualColumnVerticalLayout.builder());
		layoutBuilders.put("BorderedDoubleVerticalLayout", BorderedDoubleVerticalLayout.builder());
	}

	private static Map<String,String> lookAndFeels = new LinkedHashMap<String,String>();
	static {
		lookAndFeels.put("Nimbus", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		lookAndFeels.put("Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		lookAndFeels.put("GTK", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		lookAndFeels.put("Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		lookAndFeels.put("Metal", "javax.swing.plaf.metal.MetalLookAndFeel");
	};

	static Project p;
	static File f;
	static Builder<?> layoutBuilder;

	/**
	 * Launch the application.
	 */
	public static void main(String[] arguments)
	{
		// set defaults, overwrite later
		tryAllLookAndFeels();
		setLayout(0);

		// process arguments
		// FIXME either user getopt library or move to de.cgarbs.lib package
		Deque<String> args = new ArrayDeque<String>();
		args.addAll(Arrays.asList(arguments));
		while (!args.isEmpty() && args.getFirst().startsWith("--"))
		{
			String peek = args.removeFirst();
			int index = peek.indexOf("=");
			String param = index == -1 ? "" : peek.substring(index+1);

			if (peek.startsWith("--style="))
			{
				if (param.equals("help"))
				{
					showAllStyles();
					System.exit(0);
				}
				setLookAndFeel(param);
			}
			else if (peek.startsWith("--layout="))
			{
				if (param.equals("help"))
				{
					showAllLayouts();
					System.exit(0);
				}
				setLayout(param);
			}
			else if (peek.equals("--help"))
			{
				showHelp();
				System.exit(0);
			}
			else if (peek.equals("--"))
			{
				// file name separator, exit option parsing mode
				break;
			}
			else
			{
				System.err.println("unknown commandline option: "+args.removeFirst());
			}
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
					MainWindow frame = new MainWindow(p, f, layoutBuilder);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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

	private static void setLayout(String key)
	{
		if (layoutBuilders.containsKey(key))
		{
			layoutBuilder = layoutBuilders.get(key);
		}
	}

	private static void setLayout(int i)
	{
		layoutBuilder = layoutBuilders.values().iterator().next();
	}

	private static void showHelp()
	{
		// FIXME i18n!
		System.out.println("usage: knittr.jar [options ...] [--] [<inputfile>]");
		System.out.println();
		System.out.println("available options:");
		System.out.println("  --layout=<layout>    - set layout");
		System.out.println("  --style=<style>      - set look and feel");
		System.out.println("  --help               - show this help text");
		System.out.println();
		System.out.println("use `help' for <layout> or <style> to a list available options");
		System.out.println();
	}

	private static void showAllLayouts()
	{
		// FIXME i18n!
		System.out.println("available layouts:");
		for (String key: layoutBuilders.keySet())
		{
			System.out.println("   " + key);
		}
	}

	private static void showAllStyles()
	{
		// FIXME i18n!
		System.out.println("available styles:");
		for (String key: lookAndFeels.keySet())
		{
			System.out.println("   " + key);
		}
	}
}
