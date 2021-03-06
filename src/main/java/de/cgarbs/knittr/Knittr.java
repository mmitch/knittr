/*
 * Copyright 2014, 2020 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
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

import de.cgarbs.knittr.data.Project;
import de.cgarbs.knittr.ui.MainWindow;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.ui.AutoLayout.Builder;
import de.cgarbs.lib.ui.LookAndFeelChanger;
import de.cgarbs.lib.ui.LookAndFeelChanger.LookAndFeel;
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

	private static Map<String,LookAndFeel> lookAndFeels = new LinkedHashMap<String,LookAndFeel>();
	static {
		lookAndFeels.put("Nimbus", 	LookAndFeel.NIMBUS);
		lookAndFeels.put("Windows", LookAndFeel.WINDOWS);
		lookAndFeels.put("GTK", 	LookAndFeel.GTK);
		lookAndFeels.put("Motif", 	LookAndFeel.MOTIF);
		lookAndFeels.put("Metal", 	LookAndFeel.METAL);
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
		LookAndFeelChanger.setNiceLookAndFeel();
		setLayout(0);

		// process arguments
		// FIXME either use getopt library or move to de.cgarbs.lib package
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
				LookAndFeelChanger.setLookAndFeel(lookAndFeels.get(param));
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
			p.resetDirty(); // initial state is reproducible, no need to save it

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
					p.readFromFile(f);
					p.resetDirty();
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
