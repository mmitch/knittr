package de.cgarbs.knittr.data;
import java.awt.Color;

import de.cgarbs.lib.data.DataModel;
import de.cgarbs.lib.data.type.ColorAttribute;
import de.cgarbs.lib.data.type.FileAttribute;
import de.cgarbs.lib.data.type.FloatAttribute;
import de.cgarbs.lib.data.type.IntAttribute;
import de.cgarbs.lib.data.type.StringAttribute;
import de.cgarbs.lib.exception.DataException;


public class Project extends DataModel
{
	public static final String SOURCE_FILE = "SOURCE_FILE";
	public static final String TARGET_FILE = "TARGET_FILE";
	public static final String GRIDTEXTMOD = "GRIDTEXTMOD";
	public static final String GRIDWIDTHSMALL = "GRIDWIDTHSMALL";
	public static final String GRIDWIDTHBIG = "GRIDWIDTHBIG";
	public static final String FONTNAME = "FONTNAME";
	public static final String TEXTCOLOR = "TEXTCOLOR";
	public static final String OFFSET = "OFFSET";
	public static final String GRIDCOLOR = "GRIDCOLOR";
	public static final String TOTALSCALE = "TOTALSCALE";
	public static final String REIHEN = "REIHEN";
	public static final String MASCHEN = "MASCHEN";

	/**
	 * generate a project with default settings
	 */
	public Project() throws DataException
	{
		// IO
		addAttribute(SOURCE_FILE, FileAttribute.builder().setNullable(false).setMustExist(true).setMustRead(true).build());
		addAttribute(TARGET_FILE, FileAttribute.builder().setNullable(false).setMustWrite(true).build());

		setValue(SOURCE_FILE, "resource/image_test.png");
		setValue(TARGET_FILE, "/tmp/test.svg");

		// calculation
		addAttribute(TOTALSCALE, IntAttribute.builder().setNullable(false).setMinValue(1).build());
		addAttribute(MASCHEN, IntAttribute.builder().setNullable(false).setMinValue(1).build());
		addAttribute(REIHEN, IntAttribute.builder().setNullable(false).setMinValue(1).build());

		setValue(TOTALSCALE, 512);
		setValue(MASCHEN, 24);
		setValue(REIHEN, 32);

		// graphics
		addAttribute(GRIDWIDTHSMALL, FloatAttribute.builder().build());
		addAttribute(GRIDWIDTHBIG, FloatAttribute.builder().build());
		addAttribute(GRIDTEXTMOD, IntAttribute.builder().setNullable(false).setMinValue(1).build());
		addAttribute(GRIDCOLOR, ColorAttribute.builder().setNullable(false).build());
		addAttribute(TEXTCOLOR, ColorAttribute.builder().setNullable(false).build());
		addAttribute(FONTNAME, StringAttribute.builder().setNullable(false).setMinLength(1).build());
		addAttribute(OFFSET, IntAttribute.builder().setNullable(false).setMinValue(1).build());

		setValue(GRIDWIDTHSMALL, 0.5f);
		setValue(GRIDWIDTHBIG, 2f);
		setValue(GRIDTEXTMOD, 5);
		setValue(GRIDCOLOR, Color.BLACK);
		setValue(TEXTCOLOR, Color.BLACK);
		setValue(FONTNAME, "DialogInput");
		setValue(OFFSET, 3);

	}

	@Override
	public String getModelName()
	{
		return "KnittrProject";
	}

}
